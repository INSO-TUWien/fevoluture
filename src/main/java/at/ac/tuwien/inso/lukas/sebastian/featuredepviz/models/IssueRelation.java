package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.springframework.data.annotation.Id;

@Edge("relatedIssues")
public class IssueRelation {

    @Id
    private String id;

    @From
    private Issue from;

    @To
    private Issue to;

    private String relationType;

    public String getId() {
        return id;
    }

    public IssueRelation setId(String id) {
        this.id = id;
        return this;
    }

    public Issue getFrom() {
        return from;
    }

    public IssueRelation setFrom(Issue from) {
        this.from = from;
        return this;
    }

    public Issue getTo() {
        return to;
    }

    public IssueRelation setTo(Issue to) {
        this.to = to;
        return this;
    }

    public String getRelationType() {
        return relationType;
    }

    public IssueRelation setRelationType(String relationType) {
        this.relationType = relationType;
        return this;
    }
}
