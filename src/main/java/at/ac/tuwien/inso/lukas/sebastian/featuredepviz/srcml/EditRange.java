package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.srcml;

import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class EditRange {
    private int startLine;
    private int endLine;
    private Edit.Type changeType;

    public static List<EditRange> generateEditRange(FileHeader fileHeader) {
        List<EditRange> editRanges = new LinkedList<>();
        for (HunkHeader hunk : fileHeader.getHunks()) {
            editRanges.addAll(EditRange.generateEditRange(hunk));
        }
        return editRanges;
    }

    public static List<EditRange> generateEditRange(HunkHeader hunk) {
        List<EditRange> editRanges = new LinkedList<>();
        for (Edit edit : hunk.toEditList()) {
            editRanges.add(EditRange.generateEditRange(edit));
        }
        return editRanges;
    }

    public static EditRange generateEditRange(Edit edit) {
        EditRange editRange = new EditRange();
        editRange.changeType = edit.getType();
        editRange.startLine = edit.getBeginB() + 1;
        editRange.endLine = edit.getEndB();

        if (edit.getType().equals(Edit.Type.DELETE)) {
            editRange.endLine++;
        }

        if (editRange.endLine < editRange.startLine) {
            int buffer = editRange.startLine;
            editRange.startLine = editRange.endLine;
            editRange.endLine = buffer;
        }
        return editRange;
    }

    public LinkedList<FileMethod> getEditedMethods(List<FileMethod> fileMethods) {
        return fileMethods
                .stream()
                .filter(this::editsMethod)
                .distinct()
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public boolean editsMethod(FileMethod fileMethod) {
        return startLine <= fileMethod.getEndLine() && endLine >= fileMethod.getStartLine();
    }

    public int getStartLine() {
        return startLine;
    }

    public EditRange setStartLine(int startLine) {
        this.startLine = startLine;
        return this;
    }

    public Edit.Type isAdded() {
        return changeType;
    }

    public EditRange setAdded(Edit.Type changeType) {
        this.changeType = changeType;
        return this;
    }

    public int getEndLine() {
        return endLine;
    }

    public EditRange setEndLine(int endLine) {
        this.endLine = endLine;
        return this;
    }

    public Edit.Type getChangeType() {
        return changeType;
    }

    public String toString() {
        String type = changeType.toString().charAt(0) + "";
        return String.format("%s[%d:%d]", type, startLine, endLine);
    }
}
