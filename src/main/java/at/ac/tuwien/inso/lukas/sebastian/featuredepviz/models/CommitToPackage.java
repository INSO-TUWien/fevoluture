package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;

@Edge("commitToPackage")
public class CommitToPackage {

    @From
    private Commit commit;

    @To
    private Package aPackage;

    public Commit getCommit() {
        return commit;
    }

    public CommitToPackage setCommit(Commit commit) {
        this.commit = commit;
        return this;
    }

    public Package getaPackage() {
        return aPackage;
    }

    public CommitToPackage setaPackage(Package aPackage) {
        this.aPackage = aPackage;
        return this;
    }
}
