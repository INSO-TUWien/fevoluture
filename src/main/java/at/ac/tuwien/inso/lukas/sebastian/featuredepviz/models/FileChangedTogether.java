package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.Ref;
import com.arangodb.springframework.annotation.To;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;

@Edge("filesChangedTogether")
public class FileChangedTogether {

    @Id
    private String id;

    @From
    private File file1;

    @To
    private File file2;

    @Ref(lazy = true)
    private List<Commit> changedIn;

    public String getId() {
        return id;
    }

    public FileChangedTogether setId(String id) {
        this.id = id;
        return this;
    }

    public File getFile1() {
        return file1;
    }

    public FileChangedTogether setFile1(File file1) {
        this.file1 = file1;
        return this;
    }

    public File getFile2() {
        return file2;
    }

    public FileChangedTogether setFile2(File file2) {
        this.file2 = file2;
        return this;
    }

    public List<Commit> getChangedIn() {
        return changedIn;
    }

    public FileChangedTogether setChangedIn(List<Commit> changedIn) {
        this.changedIn = changedIn;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileChangedTogether that = (FileChangedTogether) o;
        return file1.equals(that.file1) &&
                file2.equals(that.file2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file1, file2);
    }
}
