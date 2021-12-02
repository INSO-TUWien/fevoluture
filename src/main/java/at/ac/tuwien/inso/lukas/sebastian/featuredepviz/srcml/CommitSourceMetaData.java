package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.srcml;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.File;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Method;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Package;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class CommitSourceMetaData {

    List<FilePackage> filePackages;
    List<File> files;
    List<FileMethod> fileMethods;

    public CommitSourceMetaData() {
        filePackages = new LinkedList<>();
        files = new LinkedList<>();
        fileMethods = new LinkedList<>();
    }

    public CommitSourceMetaData(List<FileMethod> fileMethods,
                                List<FilePackage> filePackages,
                                List<File> files) {
        this.fileMethods = fileMethods;
        this.filePackages = filePackages;
        this.files = files;
    }

    public List<String> getUniquePackageNames() {
        HashSet<String> packageNames = new HashSet<>();
        filePackages.forEach(filePackage -> {
            if (filePackage != null) {
                packageNames.add(filePackage.getSourcePackage());
            }
        });
        return new LinkedList<>(packageNames);
    }

    public List<Package> getChangedFilePackages() {
        HashMap<String, Package> packageHashMap = new HashMap<>();
        filePackages.forEach(filePackage -> {
            if (filePackage != null) {
                packageHashMap.put(filePackage.getSourcePackage(), filePackage.parseToPackage());
            }
        });

        return new LinkedList<>(packageHashMap.values());
    }

    public List<File> getChangedFiles(List<Package> changedPackages) {

        HashMap<String, Package> packageHashMap = new HashMap<>();
        HashMap<String, String> pathToPackageMap = new HashMap<>();

        filePackages.forEach(filePackage -> {
            if (filePackage != null) {
                pathToPackageMap.put(filePackage.getSourceFilePath(), filePackage.getSourcePackage());
                changedPackages
                        .stream()
                        .filter(p -> p.getName().equals(filePackage.getSourcePackage()))
                        .findFirst()
                        .ifPresent(aPackage -> packageHashMap.put(filePackage.getSourcePackage(), aPackage));
            }
        });
        files.forEach(file -> {
            file.setaPackage(packageHashMap.get(pathToPackageMap.get(file.getPath())));
            String packageName = "";

            if (file.getaPackage() != null) {
                packageName = file.getaPackage().getName();
            }

            String[] splitPath = file.getPath().split("/");
            String fileName = splitPath[splitPath.length - 1].split("\\.")[0];
            file.setName(packageName + '.' + fileName);
            if (file.getName().startsWith(".")) {
                file.setName(file.getName().substring(1));
            }
        });

        return files;
    }

    public List<Method> getChangedMethods(List<File> changedFiles) {
        HashMap<String, File> fileHashMap = new HashMap<>();
        changedFiles.forEach(file -> fileHashMap.put(file.getPath(), file));
        List<FileMethod> fileMethodList = fileMethods;
        List<Method> methods = new LinkedList<>();
        for (FileMethod fileMethod : fileMethodList) {

            Method method = new Method();
            method.setName(fileMethod.getMethodName());
            method.setFile(fileHashMap.get(fileMethod.getSourceFilePath()));
            methods.add(method);
        }
        return methods;
    }

    public List<FilePackage> getFilePackages() {
        return filePackages;
    }

    public CommitSourceMetaData setFilePackages(List<FilePackage> filePackages) {
        this.filePackages = filePackages;
        return this;
    }

    public List<File> getFiles() {
        return files;
    }

    public CommitSourceMetaData setFiles(List<File> files) {
        this.files = files;
        return this;
    }

    public List<FileMethod> getFileMethods() {
        return fileMethods;
    }

    public CommitSourceMetaData setFileMethods(List<FileMethod> fileMethods) {
        this.fileMethods = fileMethods;
        return this;
    }
}
