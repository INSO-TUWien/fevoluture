package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Ref;
import com.arangodb.springframework.annotation.SkiplistIndexed;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;

import java.util.List;

@Document("commits")
public class Commit {

    @Id
    private String hash;

    private String authorName;

    private String authorEmail;

    private String message;

    private boolean indexed;

    @SkiplistIndexed
    private DateTime commitTime;

    @Ref(lazy = true)
    private List<Issue> issues;

    @Ref(lazy = true)
    private List<File> changedFiles;

    @Ref(lazy = true)
    private List<Package> changedPackages;

    @Ref(lazy = true)
    private List<Method> changedMethods;

    public String getHash() {
        return hash;
    }

    public Commit setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Commit setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public Commit setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Commit setMessage(String message) {
        this.message = message;
        return this;
    }

    public DateTime getCommitTime() {
        return commitTime;
    }

    public Commit setCommitTime(DateTime commitTime) {
        this.commitTime = commitTime;
        return this;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public Commit setIssues(List<Issue> issues) {
        this.issues = issues;
        return this;
    }

    public List<File> getChangedFiles() {
        return changedFiles;
    }

    public Commit setChangedFiles(List<File> changedFiles) {
        this.changedFiles = changedFiles;
        return this;
    }

    public List<Package> getChangedPackages() {
        return changedPackages;
    }

    public Commit setChangedPackages(List<Package> changedPackages) {
        this.changedPackages = changedPackages;
        return this;
    }

    public List<Method> getChangedMethods() {
        return changedMethods;
    }

    public Commit setChangedMethods(List<Method> changedMethods) {
        this.changedMethods = changedMethods;
        return this;
    }

    public boolean isIndexed() {
        return indexed;
    }

    public Commit setIndexed(boolean indexed) {
        this.indexed = indexed;
        return this;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "hash='" + hash + '\'' +
                ", authorName='" + authorName + '\'' +
                ", authorEmail='" + authorEmail + '\'' +
                ", message='" + message + '\'' +
                ", commitTime=" + commitTime +
                ", issues=" + issues +
                ", changedFiles=" + changedFiles +
                ", changedPackages=" + changedPackages +
                ", changedMethods=" + changedMethods +
                '}';
    }
}
