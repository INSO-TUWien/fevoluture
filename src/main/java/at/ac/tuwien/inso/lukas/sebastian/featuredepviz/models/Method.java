package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.HashIndexed;
import com.arangodb.springframework.annotation.Ref;
import org.springframework.data.annotation.Id;

import java.util.Objects;

@Document("methods")
public class Method {

    @Id
    private String id;

    @HashIndexed
    private String name;

    @Ref(lazy = true)
    private File file;

    public String getId() {
        return id;
    }

    public Method setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Method setName(String name) {
        this.name = name;
        return this;
    }

    public File getFile() {
        return file;
    }

    public Method setFile(File file) {
        this.file = file;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Method method = (Method) o;
        return name.equals(method.name) &&
                file.getId().equals(method.file.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, file);
    }

    @Override
    public String toString() {
        return "Method{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", file=" + file +
                '}';
    }
}
