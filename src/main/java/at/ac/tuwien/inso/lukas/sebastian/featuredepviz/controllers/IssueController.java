package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.controllers;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.exceptions.ServiceException;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Commit;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.File;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Issue;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.services.GitService;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.services.JiraService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class IssueController extends APIController {

    @Autowired
    private JiraService jiraService;

    @Autowired
    private GitService gitService;

    @RequestMapping("/api/issues")
    public ResponseEntity getIssues(
            @RequestParam Map<String, Object> allRequestParams) {
        if (allRequestParams.containsKey("issueKey")) {
            return ResponseEntity.ok(jiraService.getIssueByKey(allRequestParams.get("issueKey").toString()));
        }

        int page = Integer.parseInt(allRequestParams.get("page").toString());
        int pageSize = Integer.parseInt(allRequestParams.get("pageSize").toString());
        allRequestParams.remove("page");
        allRequestParams.remove("pageSize");
        return ResponseEntity.ok(jiraService.queryIssues(
                generatePageable(page, pageSize),
                this.jiraService.mapToCommitFilter(allRequestParams)));
    }

    @RequestMapping("/api/issues/{issueId}")
    public ResponseEntity<Issue> getIssue(@PathVariable String issueId) {
        try {
            return ResponseEntity.ok(jiraService.getIssue(issueId));
        } catch (ServiceException e) {
            return generateErrorResponseEntity(e);
        }
    }

    @RequestMapping("/api/issues/{issueId}/commits")
    public ResponseEntity<List<Commit>> getCommitsOfIssue(@PathVariable String issueId) {
        return ResponseEntity.ok(gitService.getCommitsOfIssue(issueId));
    }

    @RequestMapping("/api/issues/{issueId}/related")
    public ResponseEntity<List<Issue>> getRelatedIssuesOf(@PathVariable String issueId, @RequestParam(required = false) Integer depth) {
        if (depth == null) {
            depth = 1;
        }
        return ResponseEntity.ok(jiraService.getRelatedIssues("issues/" + issueId, depth));
    }

    @RequestMapping("/api/issues/files")
    public ResponseEntity<List<File>> getFilesOfIssue(@RequestParam("issueIds[]") List<String> issueIds, @RequestParam long dateTime) {
        issueIds = issueIds.stream().map(issueId -> "issues/" + issueId).collect(Collectors.toList());
        return ResponseEntity.ok(jiraService.getFilesOfIssuesBefore(issueIds, new DateTime(dateTime)));
    }

    @RequestMapping("/api/issues/commits")
    public ResponseEntity<List<Commit>> getCommitsOfIssues(@RequestParam("issueIds[]") List<String> issueIds, @RequestParam(required = false) Long dateTime) {
        issueIds = issueIds.stream().map(issueId -> "issues/" + issueId).collect(Collectors.toList());
        if (dateTime != null) {
            return ResponseEntity.ok(gitService.getCommitsOfIssues(issueIds, new DateTime(dateTime)));
        } else {
            return ResponseEntity.ok(gitService.getCommitsOfIssues(issueIds));
        }
    }

    @RequestMapping("/api/issues/authors")
    public ResponseEntity<List<String>> getAllAuthors(@RequestParam String partialAuthorName) {
        return ResponseEntity.ok(jiraService.getAllIssueAuthors(partialAuthorName));
    }

    @RequestMapping("/api/issues/trackers")
    public ResponseEntity<List<String>> getAllTrackers() {
        return ResponseEntity.ok(jiraService.getAllIssueTrackers());
    }

    @RequestMapping("/api/issues/status")
    public ResponseEntity<List<String>> getAllStatus() {
        return ResponseEntity.ok(jiraService.getAllIssueStatus());
    }
}
