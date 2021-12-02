package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.Ref;
import com.arangodb.springframework.annotation.To;

import java.util.List;
import java.util.Objects;

@Edge("fileImports")
public class FileImportRelation {

    @From
    File file;

    @To
    File importedFile;

    @Ref
    List<Commit> commits;

    public File getFile() {
        return file;
    }

    public FileImportRelation setFile(File file) {
        this.file = file;
        return this;
    }

    public File getImportedFile() {
        return importedFile;
    }

    public FileImportRelation setImportedFile(File importedFile) {
        this.importedFile = importedFile;
        return this;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public FileImportRelation setCommits(List<Commit> commits) {
        this.commits = commits;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileImportRelation that = (FileImportRelation) o;
        return Objects.equals(file, that.file) &&
                Objects.equals(importedFile, that.importedFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, importedFile);
    }

    @Override
    public String toString() {
        return "FileImportRelation{" +
                "file=" + file +
                ", importedFile=" + importedFile +
                '}';
    }
}
