package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;

@Edge("issueToCommit")
public class IssueToCommit {

    @From
    private Issue issue;

    @To
    private Commit commit;

    public Issue getIssue() {
        return issue;
    }

    public IssueToCommit setIssue(Issue issue) {
        this.issue = issue;
        return this;
    }

    public Commit getCommit() {
        return commit;
    }

    public IssueToCommit setCommit(Commit commit) {
        this.commit = commit;
        return this;
    }
}
