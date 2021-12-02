package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.factories;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.utils.IssueUtils;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Issue;

public class IssueFactory {

    public Issue buildIssue(com.atlassian.jira.rest.client.api.domain.Issue issue) {
        Issue newIssue = new Issue();
        newIssue.setTitle(issue.getSummary())
                .setId(issue.getId().toString())
                .setKey(issue.getKey())
                .setDescription(issue.getDescription())
                .setTracker(IssueUtils.getTracker(issue))
                .setStatus(IssueUtils.getStatus(issue))
                .setAuthorName(IssueUtils.getAuthorName(issue))
                .setAuthorEmail(IssueUtils.getAuthorEMail(issue))
                .setCreationDate(issue.getCreationDate())
                .setDueDate(issue.getDueDate())
                .setUpdatedAt(issue.getUpdateDate())
                .setLink(issue.getSelf().toString());
        return newIssue;
    }
}
