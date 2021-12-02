package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;

@Edge("commitToMethod")
public class CommitToMethod {

    @From
    private Commit commit;

    @To
    private Method method;

    public Commit getCommit() {
        return commit;
    }

    public CommitToMethod setCommit(Commit commit) {
        this.commit = commit;
        return this;
    }

    public Method getMethod() {
        return method;
    }

    public CommitToMethod setMethod(Method method) {
        this.method = method;
        return this;
    }
}
