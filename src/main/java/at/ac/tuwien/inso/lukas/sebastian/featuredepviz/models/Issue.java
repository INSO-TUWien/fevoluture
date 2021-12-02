package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.HashIndexed;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;

import java.util.List;

@Document("issues")
public class Issue {

    @Id
    private String id;

    @HashIndexed
    private String key;

    private String title;

    private String description;

    private String status;

    private String authorName;

    private String authorEmail;

    private String tracker;

    private DateTime updatedAt;

    private DateTime creationDate;

    private DateTime updateDate;

    private DateTime dueDate;

    private String link;

    public String getId() {
        return id;
    }

    public Issue setId(String id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Issue setKey(String key) {
        this.key = key;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Issue setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Issue setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Issue setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Issue setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public Issue setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
        return this;
    }

    public String getTracker() {
        return tracker;
    }

    public Issue setTracker(String tracker) {
        this.tracker = tracker;
        return this;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public Issue setUpdatedAt(DateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public Issue setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public DateTime getUpdateDate() {
        return updateDate;
    }

    public Issue setUpdateDate(DateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public DateTime getDueDate() {
        return dueDate;
    }

    public Issue setDueDate(DateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Issue setLink(String link) {
        this.link = link;
        return this;
    }
}
