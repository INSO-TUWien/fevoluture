package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.factories;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Package;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.*;

import java.util.LinkedList;
import java.util.List;

public class ChangedTogetherFactory {

    public FileChangedTogether buildFileChangedTogether(File file1, File file2) {
        FileChangedTogether fileChangedTogether = new FileChangedTogether();
        fileChangedTogether.setFile1(file1);
        fileChangedTogether.setFile2(file2);
        fileChangedTogether.setChangedIn(new LinkedList<>());
        return fileChangedTogether;
    }

    public FileChangedTogether buildFileChangedTogether(File file1, File file2, List<Commit> changedIn) {
        FileChangedTogether fileChangedTogether = buildFileChangedTogether(file1, file2);
        fileChangedTogether.setChangedIn(changedIn);
        return fileChangedTogether;
    }

    public MethodChangedTogether buildMethodChangedTogether(Method method1, Method method2) {
        MethodChangedTogether methodChangedTogether = new MethodChangedTogether();
        methodChangedTogether.setMethod1(method1);
        methodChangedTogether.setMethod2(method2);
        methodChangedTogether.setChangedIn(new LinkedList<>());
        return methodChangedTogether;
    }

    public MethodChangedTogether buildMethodChangedTogether(Method method1, Method method2, List<Commit> changedIn) {
        MethodChangedTogether methodChangedTogether = buildMethodChangedTogether(method1, method2);
        methodChangedTogether.setChangedIn(changedIn);
        return methodChangedTogether;
    }

    public PackageChangedTogether buildPackageChangedTogether(Package package1, Package package2) {
        PackageChangedTogether packageChangedTogether = new PackageChangedTogether();
        packageChangedTogether.setPackage1(package1);
        packageChangedTogether.setPackage2(package2);
        return packageChangedTogether;
    }

    public PackageChangedTogether buildPackageChangedTogether(Package package1, Package package2, List<Commit> changedIn) {
        PackageChangedTogether packageChangedTogether = buildPackageChangedTogether(package1, package2);
        packageChangedTogether.setChangedIn(changedIn);
        return packageChangedTogether;
    }
}
