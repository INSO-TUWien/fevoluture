package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.srcml;

import java.util.List;

public class FileSourceMetaData {

    FilePackage filePackage;
    List<FileMethod> fileMethods;

    public FileSourceMetaData() {
    }

    public FileSourceMetaData(FilePackage filePackage, List<FileMethod> fileMethods) {
        this.filePackage = filePackage;
        this.fileMethods = fileMethods;
    }

    public FilePackage getFilePackage() {
        return filePackage;
    }

    public FileSourceMetaData setFilePackage(FilePackage filePackage) {
        this.filePackage = filePackage;
        return this;
    }

    public List<FileMethod> getFileMethods() {
        return fileMethods;
    }

    public FileSourceMetaData setFileMethods(List<FileMethod> fileMethods) {
        this.fileMethods = fileMethods;
        return this;
    }
}



