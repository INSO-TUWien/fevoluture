package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.services;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.exceptions.ElementNotFoundException;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.exceptions.ServiceException;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.jgit.JGitUtils;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos.Repositories;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.indexers.GitIndexer;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Commit;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@ComponentScan("at.ac.tuwien.inso.lukas.sebastian.featuredepviz")
public class GitService {

    @Autowired
    private GitIndexer gitIndexer;

    @Autowired
    private Repositories repositories;

    public Page<Commit> getCommits(Pageable pageable) {
        Pageable p = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "commitTime")
        );
        return repositories.commitRepository.findAll(p);
    }

    public Commit getCommit(String commitId) {
        Optional<Commit> result = repositories.commitRepository.findById(commitId);
        if (result.isPresent()) {
            return result.get();
        }
        throw new ElementNotFoundException(String.format("Commit '%s' not found", commitId));
    }

    public List<Commit> getCommitsOfIssue(String issueId) {
        return repositories.commitRepository.findCommitsContainingIssue("issues/" + issueId);
    }


    public List<Commit> getCommitsOfIssues(List<String> issueIds, DateTime dateTime) {
        return repositories.commitRepository.findCommitsContainingIssuesBefore(issueIds, dateTime);
    }

    public List<Commit> getCommitsOfIssues(List<String> issueIds) {
        return repositories.commitRepository.findCommitsContainingIssues(issueIds);
    }

    public String getDiff(String commitHash) throws ServiceException {
        try {
            String diff = JGitUtils.getDiffAsString(JGitUtils.getRevCommit(commitHash.substring(0, 12), gitIndexer.getRepository()), gitIndexer.getRepository());
            return diff;
        } catch (IOException exception) {
            throw new ServiceException(exception.getMessage());
        }
    }

    public List<String> getCommitAllCommitAuthors() {
        return repositories.commitRepository.findAllCommitAuthors();
    }

    public Page<Commit> queryCommits(Pageable pageable, Map<String, Object> filter) {
        Pageable p = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "commit.commitTime")
        );

        // Differ between calls with fileName and without because query with fileName is slow
        if (filter.containsKey("fileName") && filter.get("fileName").equals("%%")) {
            filter.remove("fileName");
            return repositories.commitRepository.query(p, filter);
        }

        String fileName = filter.get("fileName").toString();
        filter.remove("fileName");
        return repositories.commitRepository.queryWithFilename(p, filter, fileName);
    }
    
    public Map<String, Object> mapToCommitFilter(Map<String, Object> filterMap) {
        // TODO: Refactor
        if (filterMap.containsKey("fromDate")) {
            filterMap.put("fromDate", parseToDateTime(filterMap.get("fromDate")));
        } else {
            filterMap.putIfAbsent("fromDate", DateTime.parse("2000-10-20T01:35:48.000+02:00"));
        }

        if (filterMap.containsKey("toDate")) {
            filterMap.put("toDate", parseToDateTime(filterMap.get("toDate")));
        } else {
            filterMap.putIfAbsent("toDate", DateTime.now());
        }

        filterMap.putIfAbsent("msg", "");
        filterMap.putIfAbsent("hash", "");
        filterMap.putIfAbsent("author", "");
        filterMap.putIfAbsent("fileName", "");
        filterMap.keySet().forEach(key -> {
            if (!key.contains("Date")) {
                filterMap.put(key, "%" + filterMap.get(key) + "%");
            }
        });

        return filterMap;
    }

    private DateTime parseToDateTime(Object date) {
        return new DateTime(Long.parseLong(date.toString()));
    }
}
