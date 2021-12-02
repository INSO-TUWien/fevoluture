package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.utils;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.File;

import java.util.LinkedList;
import java.util.List;

public class FileUtils {

    public static String getNameFromPath(String path) {
        String name = path.substring(Math.max(path.lastIndexOf('/'), 0));
        return name.substring(name.indexOf('/') + 1);
    }

    public static String getFileTypeFromPath(String path) {
        String fileType = path.substring(Math.max(path.lastIndexOf('.'), 0));
        return fileType.substring(fileType.indexOf('.') + 1);
    }

    public static List<String> getFilePathsFromFiles(List<File> files) {
        List<String> paths = new LinkedList<>();
        files.forEach(file -> paths.add(file.getPath()));
        return paths;
    }
}
