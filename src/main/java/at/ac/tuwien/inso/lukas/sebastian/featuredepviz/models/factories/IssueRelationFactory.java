package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.factories;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Issue;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.IssueRelation;
import com.atlassian.jira.rest.client.api.domain.IssueLink;

public class IssueRelationFactory {

    public IssueRelation buildIssueRelation(Issue issue1, Issue issue2, IssueLink issueLink) {
        IssueRelation relation = new IssueRelation();
        relation.setFrom(issue1);
        relation.setTo(issue2);
        relation.setRelationType(issueLink.getIssueLinkType().getName());
        return  relation;
    }
}
