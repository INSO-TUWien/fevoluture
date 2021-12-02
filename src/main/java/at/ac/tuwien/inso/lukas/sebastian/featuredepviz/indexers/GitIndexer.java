package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.indexers;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.exceptions.ServiceException;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.jgit.HeadMarkedRevWalk;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.configs.ArangoConfig;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos.Repositories;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Commit;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.IssueToCommit;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.factories.CommitFactory;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.factories.IssueToCommitFactory;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

@Service
public class GitIndexer {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${github.credentials.user}")
    private String userName;

    @Value("${github.credentials.password}")
    private String userPassword;

    @Value("${github.url}")
    private String repoURL;

    @Value("${localRepositoryPath}")
    private String clonedRepositoryPath;

    @Autowired
    private ArangoConfig arangoConfig;

    @Autowired
    private Repositories dbRepositories;

    @Value("${issue.referencepattern}")
    private String issueReferencePattern;

    private Repository repository;


    public void index() {
        logger.info("Start indexing commits...");
        cloneRepository();
        storeCommits();
        storeIssueToCommitRelation();

        logger.info("Finished indexing commits...");
    }

    public Repository getRepository() {
        if (repository == null) {
            logger.info("Repository is not initialized. Initializing repository...");
            if (!getRepositoryPath().toFile().exists()) {
                logger.info("Repository not available. Cloning repository from " + repoURL + '.');
                cloneRepository();
            }
            try {
                initRepository();
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServiceException("Repository could not be initialized.");
            }
        }
        return repository;
    }

    public void clearExistingData() {
        logger.info("Clear commit data...");
        dbRepositories.commitIssueRepository.deleteAll();
        dbRepositories.commitRepository.deleteAll();
    }

    private void cloneRepository() throws ServiceException {
        try {
            logger.info("Start cloning repository...");
            org.apache.commons.io.FileUtils.deleteDirectory(new java.io.File(clonedRepositoryPath));
            Git.cloneRepository()
                    .setURI(repoURL)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(userName, userPassword))
                    .setDirectory(Paths.get(clonedRepositoryPath).toFile())
                    .call();
            logger.info("Repository successfully cloned...");
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
            logger.error("Repository could not be cloned.");
            throw new ServiceException(e.getMessage());
        }
    }

    private void storeCommits() throws ServiceException {
        repository = getRepository();
        logger.info("Store commits in database...");
        String[] referencePatterns = this.issueReferencePattern.split(";");

        try {
            RevWalk walk = new HeadMarkedRevWalk(repository, RevSort.REVERSE);
            for (RevCommit rev : walk) {
                Commit commit = new CommitFactory().buildCommit(rev, dbRepositories, referencePatterns);
                dbRepositories.commitRepository.save(commit);
            }
            walk.dispose();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    private void storeIssueToCommitRelation() {
        int currentPage = 0;
        int totalPages = 1;
        int pageSize = 100;
        IssueToCommitFactory factory = new IssueToCommitFactory();

        while (currentPage <= totalPages) {
            Page<Commit> page = this.dbRepositories.commitRepository.findAll(PageRequest.of(currentPage, pageSize));
            totalPages = page.getTotalPages();
            currentPage++;
            List<IssueToCommit> issueToCommits = new LinkedList<>();


            page.get().forEach(commit -> commit.getIssues().forEach(issue -> {
                issueToCommits.add(factory.buildIssueToCommit(commit, issue));
            }));

            logger.info(String.format("Store %d relations for page %d of %d", issueToCommits.size(), currentPage, totalPages));
            if (issueToCommits.size() > 0) {
                dbRepositories.commitIssueRepository.saveAll(issueToCommits);
            }
        }
    }

    private Path getRepositoryPath() {
        return new java.io.File(clonedRepositoryPath).toPath();
    }

    private void initRepository() throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        repository = builder.setGitDir(Paths.get(getRepositoryPath().toString(), ".git").toFile())
                .readEnvironment()
                .findGitDir()
                .build();
        logger.info("Repository successfully initialized.");
    }
}
