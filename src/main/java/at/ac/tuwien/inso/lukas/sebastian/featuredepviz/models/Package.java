package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.HashIndexed;
import org.springframework.data.annotation.Id;

import java.util.Objects;

@Document("packages")
public class Package {

    @Id
    private String id;

    @HashIndexed
    private String name;

    public String getId() {
        return id;
    }

    public Package setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Package setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "Package{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Package aPackage = (Package) o;
        return Objects.equals(name, aPackage.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
