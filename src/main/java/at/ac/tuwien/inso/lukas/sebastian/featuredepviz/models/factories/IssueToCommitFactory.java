package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.factories;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Commit;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Issue;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.IssueToCommit;

public class IssueToCommitFactory {

    public IssueToCommit buildIssueToCommit(Commit commit, Issue issue) {
        IssueToCommit issueToCommit = new IssueToCommit();
        return issueToCommit.setCommit(commit).setIssue(issue);
    }
}
