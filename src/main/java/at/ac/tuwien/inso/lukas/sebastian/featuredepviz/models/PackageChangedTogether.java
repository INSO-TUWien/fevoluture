package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.Ref;
import com.arangodb.springframework.annotation.To;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;

@Edge("packagesChangedTogether")
public class PackageChangedTogether {
    
    @Id
    private String id;

    @From
    private Package package1;

    @To
    private Package package2;

    @Ref
    private List<Commit> changedIn;

    public String getId() {
        return id;
    }

    public PackageChangedTogether setId(String id) {
        this.id = id;
        return this;
    }

    public Package getPackage1() {
        return package1;
    }

    public PackageChangedTogether setPackage1(Package namespace1) {
        this.package1 = namespace1;
        return this;
    }

    public Package getPackage2() {
        return package2;
    }

    public PackageChangedTogether setPackage2(Package package2) {
        this.package2 = package2;
        return this;
    }

    public List<Commit> getChangedIn() {
        return changedIn;
    }

    public PackageChangedTogether setChangedIn(List<Commit> changedIn) {
        this.changedIn = changedIn;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageChangedTogether that = (PackageChangedTogether) o;
        return package1.equals(that.package1) &&
                package2.equals(that.package2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(package1, package2);
    }
}
