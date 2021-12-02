package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.utils;

import com.atlassian.jira.rest.client.api.domain.Issue;

import java.util.LinkedList;
import java.util.List;

public class IssueUtils {

    public static String getAuthorName(Issue issue) {
        if (issue.getReporter() == null) {
            return "Anonymous";
        }
        String authorName = issue.getReporter().getDisplayName();
        if (authorName == null) {
            authorName = issue.getReporter().getName();
        }
        return authorName;
    }

    public static String getAuthorEMail(Issue issue) {
        if (issue.getReporter() == null) {
            return "Anonymous";
        }
        return issue.getReporter().getEmailAddress();
    }

    public static String getStatus(Issue issue) {
        return issue.getStatus().getName();
    }

    public static String getTracker(Issue issue) {
        return issue.getIssueType().getName();
    }

}
