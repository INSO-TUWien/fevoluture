package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.indexers;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.exceptions.ServiceException;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.jgit.HeadMarkedRevWalk;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.jgit.JGitUtils;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos.Repositories;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Package;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.*;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.factories.ChangedTogetherFactory;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.factories.CommitToSourceCodeFactory;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.srcml.CommitSourceMetaData;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SourceCodeIndexer {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${localRepositoryPath}")
    private String clonedRepositoryPath;

    @Autowired
    Repositories dbRepositories;

    private Repository repository;

    public void index() throws IOException {
        logger.info("Start indexing files...");
        setupGitRepository();
        indexSourceCode();
        logger.info("Finished indexing files...");
    }

    public void indexSourceCode() {
        try {
            RevWalk walk = new HeadMarkedRevWalk(repository, RevSort.REVERSE);
            int count = 1;

            for (RevCommit rev : walk) {
                int totalCommitNumber = JGitUtils.getTotalCommitCount(getRepositoryPath().toFile());
                logger.info(count + "/" + totalCommitNumber + ": " + rev.toString());
                try {
                    dbRepositories
                            .commitRepository
                            .findOneByHash(rev.getName())
                            .ifPresent(commit1 -> storeCommitSourceCode(commit1, rev));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                count++;
            }

            walk.dispose();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }


    public void clearExistingData() {
        logger.info("Clear source code data...");
        dbRepositories.methodChangedTogetherRepository.deleteAll();
        dbRepositories.fileChangedTogetherRepository.deleteAll();
        dbRepositories.packageChangedTogetherRepository.deleteAll();

        dbRepositories.methodCommitRepository.deleteAll();
        dbRepositories.fileCommitRepository.deleteAll();
        dbRepositories.packageCommitRepository.deleteAll();

        dbRepositories.commitRepository.findAll().forEach(commit -> {
            commit.setChangedFiles(new LinkedList<>());
            commit.setChangedPackages(new LinkedList<>());
            commit.setChangedMethods(new LinkedList<>());
            dbRepositories.commitRepository.save(commit);
        });

        dbRepositories.methodRepository.deleteAll();
        dbRepositories.fileRepository.deleteAll();
        dbRepositories.packageRepository.deleteAll();

        List<Commit> commitList = new LinkedList<>();
        dbRepositories.commitRepository.findAll().forEach(commit -> {
            commit.setIndexed(false);
            commitList.add(commit);
        });
        dbRepositories.commitRepository.saveAll(commitList);

    }

    public void storeCommitSourceCode(Commit c, RevCommit current)  {

        int maxAmountOfChangedFiles = 100;
        // To avoid indexing source code (which is resource intense)
        //  which is already indexed to prevent invalid data.
        if (c.isIndexed()) {
            logger.info(c.getHash() + " was already indexed. Skipped...");
            return;
        }

        logger.info("Store source code of commit " + c.getHash());
        RevCommit commit = JGitUtils.getRevCommit(c.getHash(),
                repository);
        RevCommit prev = JGitUtils.getPreviousCommit(commit, repository);

        if (prev != null) {
            int amountChangedFiles = 0;
            try {
                amountChangedFiles = JGitUtils.getChangedFiles(commit, prev, repository).size();
            } catch (IOException exp) {
                amountChangedFiles = maxAmountOfChangedFiles +1;
                logger.error("Could not get amount of changed files during indexing. Skip commit " + c.getHash());
                exp.printStackTrace();
            }

            if (amountChangedFiles <= maxAmountOfChangedFiles) {
                CommitSourceMetaData sourceMetaData = getCommitSourceMetaData(
                        commit,
                        prev,
                        repository);
                List<Package> changedPackages = storePackages(sourceMetaData.getChangedFilePackages());
                List<File> changedFiles = storeFiles(sourceMetaData.getChangedFiles(changedPackages));
                List<Method> changedMethods = storeMethods(sourceMetaData.getChangedMethods(changedFiles));
                c.setChangedPackages(changedPackages);
                c.setChangedFiles(changedFiles);
                c.setChangedMethods(changedMethods);
                dbRepositories.commitRepository.save(c);
                storeSourceCodeToCommitRelation(c);
                storeChangedTogetherSourceCode(c);
                c.setIndexed(true);
                dbRepositories.commitRepository.save(c);

            } else {
                logger.info(String.format("Skiped %s due to size of changed files (%d)", commit.getName(), amountChangedFiles));
                c.setChangedFiles(new LinkedList<>());
                c.setChangedMethods(new LinkedList<>());
                c.setChangedPackages(new LinkedList<>());
                c.setIndexed(true);
                dbRepositories.commitRepository.save(c);
            }
        } else {
            logger.info("No prev found");
        }
    }

    private List<Package> storePackages(List<Package> packages) {
        HashSet<Package> buffer = new HashSet<>();
        List<Package> existingPackages = dbRepositories.packageRepository.findAllByNames(packages);

        packages.stream().filter(aPackage -> !existingPackages.contains(aPackage)).forEach(buffer::add);
        if (buffer.size() > 0) {
            dbRepositories.packageRepository.saveAll(buffer);
        }
        List<Package> result = new LinkedList<>();
        result.addAll(existingPackages);
        result.addAll(buffer);
        return result;
    }

    private List<File> storeFiles(List<File> files) {
        HashSet<File> buffer = new HashSet<>();
        List<String> filePaths = files.stream().map(File::getPath).collect(Collectors.toList());
        List<File> existingFiles = dbRepositories.fileRepository.findAllByPathIn(filePaths);

        files.stream().filter(file -> !existingFiles.contains(file)).forEach(buffer::add);
        if (buffer.size() > 0) {
            dbRepositories.fileRepository.saveAll(buffer);
        }
        List<File> result = new LinkedList<>();
        result.addAll(existingFiles);
        result.addAll(buffer);
        result.sort(Comparator.comparing(File::getPath));
        return result;
    }

    private List<Method> storeMethods(List<Method> methods) {
        List<Method> buffer = new LinkedList<>();

        List<Method> existingMethods = dbRepositories.methodRepository.findAllByNameAndFileId(methods);

        methods.stream().filter(method -> !existingMethods.contains(method)).forEach(buffer::add);
        if (buffer.size() > 0) {
            dbRepositories.methodRepository.saveAll(buffer);
        }
        List<Method> result = new LinkedList<>();
        result.addAll(existingMethods);
        result.addAll(buffer);
        return result;
    }

    private void storeSourceCodeToCommitRelation(Commit commit) {

        CommitToSourceCodeFactory factory = new CommitToSourceCodeFactory();
        List<CommitToFile> commitToFiles = commit.getChangedFiles()
                .stream()
                .map(file -> factory.buildCommitToFile(commit, file))
                .collect(Collectors.toList());
        if (commitToFiles.size() > 0) {
            dbRepositories.fileCommitRepository.saveAll(commitToFiles);
        }

        List<CommitToMethod> commitToMethods = commit.getChangedMethods()
                .stream()
                .map(method -> factory.buildCommitToMethod(commit, method))
                .collect(Collectors.toList());
        if (commitToMethods.size() > 0) {
            dbRepositories.methodCommitRepository.saveAll(commitToMethods);
        }

        List<CommitToPackage> commitToPackages = commit.getChangedPackages()
                .stream()
                .map(aPackage -> factory.buildCommitToPackage(commit, aPackage))
                .collect(Collectors.toList());
        if (commitToPackages.size() > 0) {
            dbRepositories.packageCommitRepository.saveAll(commitToPackages);
        }
    }

    private void storeChangedTogetherSourceCode(Commit commit) {
        if (commit.getChangedFiles().size() <= 50 && commit.getChangedMethods().size() <= 50) {
            storeChangedTogetherFiles(commit);
            storeChangedTogetherMethods(commit);
            storeChangedTogetherPackages(commit);
        }
    }

    private void storeChangedTogetherFiles(Commit commit) {
        Set<FileChangedTogether> buffer = new HashSet<>();
        List<File> changedFiles = commit.getChangedFiles();
        ChangedTogetherFactory fctFactory = new ChangedTogetherFactory();
        // Store cross product of entries. Runtime  ((N-1) * N)  / 2, where N is the amount of changed files.
        // Example [A,B,C] = (A,B), (A,C), (B,C)
        for (int i = 0; i < changedFiles.size(); i++) {
            File file1 = changedFiles.get(i);

            List<FileChangedTogether> havingFromFile1 = dbRepositories.fileChangedTogetherRepository
                    .findAllByFromId("files/" + file1.getId());

            if (havingFromFile1 == null) {
                havingFromFile1 = new LinkedList<>();
            }

            HashMap<String, FileChangedTogether> map = new HashMap<>();
            havingFromFile1.forEach(fct -> {
                String file2Path = fct.getFile2().getPath();
                map.put(file2Path, fct);
            });

            for (int j = (i + 1); j < changedFiles.size(); j++) {
                File file2 = changedFiles.get(j);
                if (map.containsKey(file2.getPath())) {
                    FileChangedTogether fct = map.get(file2.getPath());
                    fct.getChangedIn().add(commit);
                    buffer.add(fct);
                } else {
                    LinkedList<Commit> changedIn = new LinkedList<>();
                    changedIn.add(commit);
                    FileChangedTogether fct = fctFactory.buildFileChangedTogether(file1, file2, changedIn);
                    buffer.add(fct);
                    map.put(file2.getPath(), fct);
                }
            }
            if (buffer.size() > 0) {
                dbRepositories.fileChangedTogetherRepository.saveAll(buffer);
            }
        }
    }

    public void storeChangedTogetherMethods(Commit commit) {
        Set<MethodChangedTogether> buffer = new HashSet<>();
        List<Method> changedMethods = commit.getChangedMethods();
        changedMethods.sort(Comparator.comparing(Method::getId));
        ChangedTogetherFactory fctFactory = new ChangedTogetherFactory();
        // Store cross product of entries. Runtime  ((N-1) * N)  / 2, where N is the amount of changed files.
        // Example [A,B,C] = (A,B), (A,C), (B,C)
        for (int i = 0; i < changedMethods.size(); i++) {
            Method method1 = changedMethods.get(i);

            List<MethodChangedTogether> havingFromMethod1 = dbRepositories.methodChangedTogetherRepository
                    .findAllByFromId("methods/" + method1.getId());

            if (havingFromMethod1 == null) {
                havingFromMethod1 = new LinkedList<>();
            }

            HashMap<String, MethodChangedTogether> map = new HashMap<>();
            havingFromMethod1.forEach(mct -> {
                String method2Name = getFileMethodeName(mct.getMethod2());
                map.put(method2Name, mct);
            });

            for (int j = (i + 1); j < changedMethods.size(); j++) {
                Method method2 = changedMethods.get(j);
                if (map.containsKey(getFileMethodeName(method2))) {
                    MethodChangedTogether mct = map.get(getFileMethodeName(method2));
                    if (!mct.getChangedIn().contains(commit)) {
                        mct.getChangedIn().add(commit);
                    }
                    buffer.add(mct);
                } else {
                    HashSet<Commit> changedIn = new HashSet<>();
                    changedIn.add(commit);
                    MethodChangedTogether fct = fctFactory.buildMethodChangedTogether(method1, method2, new LinkedList<>(changedIn));
                    buffer.add(fct);
                    map.put(getFileMethodeName(method2), fct);
                }
            }
            if (buffer.size() > 0) {
                dbRepositories.methodChangedTogetherRepository.saveAll(buffer);
            }
        }
    }

    public void storeChangedTogetherPackages(Commit commit) {
        Set<PackageChangedTogether> buffer = new HashSet<>();
        List<Package> changedPackages = commit.getChangedPackages();
        changedPackages.sort(Comparator.comparing(Package::getId));
        ChangedTogetherFactory fctFactory = new ChangedTogetherFactory();
        // Store cross product of entries. Runtime  ((N-1) * N)  / 2, where N is the amount of changed files.
        // Example [A,B,C] = (A,B), (A,C), (B,C)
        for (int i = 0; i < changedPackages.size(); i++) {
            Package package1 = changedPackages.get(i);

            List<PackageChangedTogether> havingFromPackage1 = dbRepositories.packageChangedTogetherRepository
                    .findAllByFromId("packages/" + package1.getId());

            if (havingFromPackage1 == null) {
                havingFromPackage1 = new LinkedList<>();
            }

            HashMap<String, PackageChangedTogether> map = new HashMap<>();
            havingFromPackage1.forEach(nct -> {
                String package2Name = nct.getPackage2().getName();
                map.put(package2Name, nct);
            });

            for (int j = (i + 1); j < changedPackages.size(); j++) {
                Package package2 = changedPackages.get(j);
                if (map.containsKey(package2.getName())) {
                    PackageChangedTogether nct = map.get(package2.getName());
                    if (nct.getChangedIn().contains(commit)) {
                        nct.getChangedIn().add(commit);
                    }
                    buffer.add(nct);
                } else {
                    HashSet<Commit> changedIn = new HashSet<>();
                    changedIn.add(commit);
                    PackageChangedTogether fct = fctFactory.buildPackageChangedTogether(package1, package2, new LinkedList<>(changedIn));
                    buffer.add(fct);
                    map.put(package2.getName(), fct);
                }
            }
            if (buffer.size() > 0) {
                dbRepositories.packageChangedTogetherRepository.saveAll(buffer);
            }
        }
    }

    public CommitSourceMetaData getCommitSourceMetaData(
            RevCommit currentCommit,
            RevCommit prevCommit,
            Repository repository) {
        try {
            return JGitUtils.getCommitSourceMetaData(currentCommit, prevCommit, repository);
        } catch (IOException | XPathExpressionException | TransformerException e) {
            e.printStackTrace();
            return new CommitSourceMetaData();
        }
    }

    /**
     * Sets the reference to the Git repository.
     *
     * @throws IOException
     */
    public void setupGitRepository() throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        repository = builder.setGitDir(Paths.get(getRepositoryPath().toString(), ".git").toFile())
                .readEnvironment()
                .findGitDir()
                .build();
        logger.info("Repository successfully initialized.");
    }

    private Path getRepositoryPath() {
        return new java.io.File(clonedRepositoryPath).toPath();
    }

    private String getFileMethodeName(Method method) {
        return method.getFile().getName() +'.'+ method.getName();
    }
}
