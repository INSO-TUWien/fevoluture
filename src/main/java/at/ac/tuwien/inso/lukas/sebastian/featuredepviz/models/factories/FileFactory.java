package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.factories;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.utils.FileUtils;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.File;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;

public class FileFactory {

    private Repository repository;

    public FileFactory(Repository repository) {
        this.repository = repository;
    }

    public File buildFile(DiffEntry diffEntry) throws IOException {

        File file = new File();
        String path = diffEntry.getNewPath();

        // If file is deleted
        if (diffEntry.getNewPath().equals("/dev/null")) {
            path = diffEntry.getOldPath();
        }
        file.setPath(path);
        file.setFileType(FileUtils.getFileTypeFromPath(path));
        return file;
    }

}

