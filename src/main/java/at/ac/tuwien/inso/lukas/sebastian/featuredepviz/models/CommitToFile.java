package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;

@Edge("commitToFile")
public class CommitToFile {

    @From
    private Commit commit;

    @To
    private File file;

    public Commit getCommit() {
        return commit;
    }

    public CommitToFile setCommit(Commit commit) {
        this.commit = commit;
        return this;
    }

    public File getFile() {
        return file;
    }

    public CommitToFile setFile(File file) {
        this.file = file;
        return this;
    }
}
