package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.factories;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Package;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.*;

public class CommitToSourceCodeFactory {

    public CommitToFile buildCommitToFile(Commit commit, File file) {
        CommitToFile commitToFile = new CommitToFile();
        return  commitToFile.setCommit(commit).setFile(file);
    }

    public CommitToMethod buildCommitToMethod(Commit commit, Method method) {
        CommitToMethod commitToMethod = new CommitToMethod();
        return  commitToMethod.setCommit(commit).setMethod(method);
    }

    public CommitToPackage buildCommitToPackage(Commit commit, Package aPackage) {
        CommitToPackage commitToPackage = new CommitToPackage();
        return  commitToPackage.setCommit(commit).setaPackage(aPackage);
    }
}
