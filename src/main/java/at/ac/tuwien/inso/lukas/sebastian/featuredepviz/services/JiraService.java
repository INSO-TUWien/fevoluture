package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.services;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.exceptions.ElementNotFoundException;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.exceptions.ServiceException;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos.Repositories;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.File;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Issue;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@ComponentScan("at.ac.tuwien.inso.lukas.sebastian.featuredepviz")
public class JiraService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Repositories repositories;

    public Page<Issue> getIssues(Pageable pageable) {
        Pageable p = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "updatedAt"));
        return repositories.issueRepository.findAll(p);
    }

    public Issue getIssue(String id) throws ServiceException {
        Optional<Issue> result = repositories.issueRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new ElementNotFoundException(String.format("Issue '%s' not found", id), HttpStatus.NOT_FOUND);
    }

    public Issue getIssueByKey(String issueKey) throws  ServiceException {
        Optional<Issue> result = repositories.issueRepository.findOneByKey(issueKey);
        if (result.isPresent()) {
            return result.get();
        }
        throw new ElementNotFoundException(String.format("IssueKey '%s' not found", issueKey), HttpStatus.NOT_FOUND);
    }

    public List<File> getFilesOfIssuesBefore(List<String> issueIds, DateTime dateTime) {
        return  repositories.fileRepository.findFilesOfIssuesBefore(issueIds, dateTime);
    }

    public List<Issue> getRelatedIssues(String issueId, int maxDepth) {
        return repositories.issueRepository.findAllRelatedIssues(issueId, maxDepth);
    }

    public List<String> getAllIssueAuthors(String partialAuthorName) {
        return repositories.issueRepository.findIssueAuthors("%" + partialAuthorName + "%");
    }

    public List<String> getAllIssueTrackers() {
        return repositories.issueRepository.findAllTrackers();
    }

    public List<String> getAllIssueStatus() {
        return repositories.issueRepository.findAllStatus();
    }

    public Page<Issue> queryIssues(Pageable pageable, Map<String, Object> filter) {
        Pageable p = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "issue.updatedAt")
        );
        return repositories.issueRepository.query(p, filter);
    }

    public Map<String, Object> mapToCommitFilter(Map<String, Object> filterMap) {
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

        filterMap.putIfAbsent("status", "");
        filterMap.putIfAbsent("tracker", "");
        filterMap.putIfAbsent("author", "");
        filterMap.putIfAbsent("issueId", "");
        filterMap.putIfAbsent("text", "");
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
