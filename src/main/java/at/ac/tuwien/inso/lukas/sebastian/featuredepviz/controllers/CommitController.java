package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.controllers;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.exceptions.ServiceException;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Commit;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.services.GitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
public class CommitController extends APIController {

    @Autowired
    private GitService gitService;

    @RequestMapping("/api/commits")
    public ResponseEntity<Page<Commit>> getCommits(@RequestParam Map<String, Object> allRequestParams) {
        int page = Integer.parseInt(allRequestParams.get("page").toString());
        int pageSize = Integer.parseInt(allRequestParams.get("pageSize").toString());
        allRequestParams.remove("page");
        allRequestParams.remove("pageSize");
        return ResponseEntity.ok(gitService.queryCommits(
                generatePageable(page, pageSize),
                this.gitService.mapToCommitFilter(allRequestParams)));
    }

    @RequestMapping("/api/commits/{commitId}")
    public ResponseEntity<Commit> getCommit(@PathVariable String commitId) {
        try {
            return ResponseEntity.ok(gitService.getCommit(commitId));
        } catch (ServiceException e) {
            return generateErrorResponseEntity(e);
        }
    }

    @RequestMapping("/api/commits/authors")
    public ResponseEntity<List<String>> getAllCommitAuthors() {
        try {
            return ResponseEntity.ok(gitService.getCommitAllCommitAuthors());
        } catch (ServiceException e) {
            return generateErrorResponseEntity(e);
        }
    }


    @RequestMapping("/api/commits/{commitHash}/diff")
    public ResponseEntity<String> getDiff(@PathVariable String commitHash) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_HTML);
        try {
            return ResponseEntity.ok().headers(responseHeaders).body(gitService.getDiff(commitHash));
        } catch (ServiceException e) {
            return generateErrorResponseEntity(e);
        }
    }
}
