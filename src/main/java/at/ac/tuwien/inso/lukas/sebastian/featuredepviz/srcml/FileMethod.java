package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.srcml;

import java.util.Objects;

public class FileMethod {
    private String sourceFilePath;
    private String methodName;
    private int startLine;
    private int endLine;

    public FileMethod(String sourceFilePath, String methodName, int startLine, int endLine) {
        this.sourceFilePath = sourceFilePath;
        this.methodName = methodName;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    public String getMethodName() {
        return methodName;
    }

    public FileMethod setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public int getStartLine() {
        return startLine;
    }

    public FileMethod setStartLine(int startLine) {
        this.startLine = startLine;
        return this;
    }

    public int getEndLine() {
        return endLine;
    }

    public FileMethod setEndLine(int endLine) {
        this.endLine = endLine;
        return this;
    }

    public String getSourceFilePath() {
        return sourceFilePath;
    }

    public FileMethod setSourceFilePath(String sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMethod that = (FileMethod) o;
        return Objects.equals(sourceFilePath, that.sourceFilePath) && Objects.equals(methodName, that.methodName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceFilePath, methodName);
    }

    @Override
    public String toString() {
        return String.format("%s[%d:%d] in %s",
                methodName,
                startLine,
                endLine,
                sourceFilePath);
    }
}



