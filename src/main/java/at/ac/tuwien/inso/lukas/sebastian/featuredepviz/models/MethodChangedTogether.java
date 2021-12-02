package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.Ref;
import com.arangodb.springframework.annotation.To;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;

@Edge("methodsChangedTogether")
public class MethodChangedTogether {

    @Id
    private String id;

    @From
    private Method method1;

    @To
    private Method method2;

    @Ref(lazy = true)
    private List<Commit> changedIn;


    public String getId() {
        return id;
    }

    public MethodChangedTogether setId(String id) {
        this.id = id;
        return this;
    }

    public Method getMethod1() {
        return method1;
    }

    public MethodChangedTogether setMethod1(Method method1) {
        this.method1 = method1;
        return this;
    }

    public Method getMethod2() {
        return method2;
    }

    public MethodChangedTogether setMethod2(Method method2) {
        this.method2 = method2;
        return this;
    }

    public List<Commit> getChangedIn() {
        return changedIn;
    }

    public MethodChangedTogether setChangedIn(List<Commit> changedIn) {
        this.changedIn = changedIn;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodChangedTogether that = (MethodChangedTogether) o;
        return method1.equals(that.method1) &&
                method2.equals(that.method2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method1, method2);
    }
}
