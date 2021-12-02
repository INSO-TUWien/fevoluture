package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.srcml;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Package;

import java.util.Objects;

public class FilePackage {
    private String sourceFilePath;
    private String sourcePackage;

    public FilePackage(String sourceFilePath, String sourcePackage) {
        this.sourceFilePath = sourceFilePath;
        this.sourcePackage = sourcePackage;
    }

    public String getSourceFilePath() {
        return sourceFilePath;
    }

    public FilePackage setSourceFilePath(String sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
        return this;
    }

    public String getSourcePackage() {
        return sourcePackage;
    }

    public FilePackage setSourcePackage(String sourcePackage) {
        this.sourcePackage = sourcePackage;
        return this;
    }

    public Package parseToPackage() {
        Package aPackage = new Package();
        aPackage.setName(getSourcePackage());
        return aPackage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilePackage that = (FilePackage) o;
        return Objects.equals(sourceFilePath, that.sourceFilePath) &&
                Objects.equals(sourcePackage, that.sourcePackage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceFilePath, sourcePackage);
    }

    @Override
    public String toString() {
        return "FilePackage{" +
                "sourceFilePath='" + sourceFilePath + '\'' +
                ", sourcePackage='" + sourcePackage + '\'' +
                '}';
    }
}
