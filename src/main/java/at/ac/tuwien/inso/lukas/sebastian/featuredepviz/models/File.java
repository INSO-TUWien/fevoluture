package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.HashIndexed;
import com.arangodb.springframework.annotation.Ref;
import org.springframework.data.annotation.Id;

import java.util.Objects;

@Document("files")
public class File {

    @Id
    private String id;

    @HashIndexed
    private String path;

    @HashIndexed
    private String name;

    protected Package aPackage;

    private String fileType;

    public String getId() {
        return id;
    }

    public File setId(String id) {
        this.id = id;
        return this;
    }

    public String getPath() {
        return path;
    }

    public File setPath(String path) {
        this.path = path;
        return this;
    }

    public String getFileType() {
        return fileType;
    }

    public File setFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public Package getaPackage() {
        return aPackage;
    }

    public File setaPackage(Package aPackage) {
        this.aPackage = aPackage;
        return this;
    }

    public String getName() {
        return name;
    }

    public File setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "File{" +
                "id='" + id + '\'' +
                ", path='" + path + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return path.equals(file.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
