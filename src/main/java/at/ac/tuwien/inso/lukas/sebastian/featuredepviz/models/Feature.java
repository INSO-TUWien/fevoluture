package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Ref;
import org.springframework.data.annotation.Id;

import java.util.List;

@Document("features")
public class Feature {

    @Id
    private String id;

    private String name;

    private Color color;

    private boolean visualized;

    @Ref(lazy = true)
    private List<Commit> commits;

    @Ref(lazy = true)
    private List<Issue> issues;

    public String getId() {
        return id;
    }

    public Feature setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Feature setName(String name) {
        this.name = name;
        return this;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public Feature setCommits(List<Commit> commits) {
        this.commits = commits;
        return this;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public Feature setIssues(List<Issue> issues) {
        this.issues = issues;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public Feature setColor(Color color) {
        this.color = color;
        return this;
    }

    public boolean isVisualized() {
        return visualized;
    }

    public Feature setVisualized(boolean visualized) {
        this.visualized = visualized;
        return this;
    }
}
