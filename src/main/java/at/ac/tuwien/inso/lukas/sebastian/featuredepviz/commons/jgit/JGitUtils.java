package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.jgit;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.File;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.factories.FileFactory;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.srcml.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.joda.time.DateTime;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class JGitUtils {

    private static final Set<String> allowedFileEndings = new HashSet<>(Arrays.asList("cpp", "java"));

    public static RevCommit getRevCommit(String hash, Repository repository) {

        try (RevWalk walk = new RevWalk(repository)) {
            walk.reset();
            ObjectId id = repository.resolve(hash);
            RevCommit resultCommit = walk.parseCommit(id);
            walk.dispose();
            return resultCommit;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RevCommit getPreviousCommit(RevCommit commit, Repository repository) {
        try (RevWalk walk = new RevWalk(repository)) {
            walk.reset();
            walk.markStart(commit);
            walk.next();
            return walk.next();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDiffAsString(RevCommit commit, Repository repository) throws IOException {

        //Get commit that is previous to the current one.
        RevCommit previousCommit = getPreviousCommit(commit, repository);
        if (previousCommit == null) {
            return "Start of repo";
        }
        //Use treeIterator to diff.
        AbstractTreeIterator oldTreeIterator = getCanonicalTreeParser(previousCommit, repository);
        AbstractTreeIterator newTreeIterator = getCanonicalTreeParser(commit, repository);
        OutputStream outputStream = new ByteArrayOutputStream();
        try (DiffFormatter formatter = new DiffFormatter(outputStream)) {
            formatter.setContext(12);
            formatter.setRepository(repository);
            formatter.format(oldTreeIterator, newTreeIterator);
        }
        return outputStream.toString();
    }

    public static DateTime getCommitTime(RevCommit commit) {
        return new DateTime(commit.getAuthorIdent().getWhen().getTime());
    }

    public static List<File> getChangedFiles(RevCommit commit, RevCommit prevCommit, Repository repository) throws
            IOException {
        List<File> results = new LinkedList<>();
        List<DiffEntry> diffEntries = getDiffEntries(commit, prevCommit, repository);
        FileFactory fileFactory = new FileFactory(repository);

        for (DiffEntry diffEntry : diffEntries) {
            File file = fileFactory.buildFile(diffEntry);
            results.add(file);
        }
        return results;
    }

    public static CommitSourceMetaData getCommitSourceMetaData(RevCommit commit, RevCommit prevCommit, Repository repository) throws IOException, XPathExpressionException, TransformerException {

        ObjectReader reader = repository.newObjectReader();
        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
        oldTreeIter.reset(reader, commit.getTree());
        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();

        if (prevCommit != null) {
            newTreeIter.reset(reader, prevCommit.getTree());
        }

        DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
        diffFormatter.setRepository(repository);
        diffFormatter.setContext(0);
        List<DiffEntry> entries = diffFormatter.scan(newTreeIter, oldTreeIter);

        HashSet<FileMethod> editedMethods = new HashSet<>();
        HashSet<FilePackage> editedPackages = new HashSet<>();

        for (DiffEntry entry : entries) {
            String changedFilePath = entry.getNewPath();

            if (!JGitUtils.canGenerateASTForFile(changedFilePath)) {
                continue;
            }

            FileHeader fileHeader = diffFormatter.toFileHeader(entry);
            List<EditRange> editRanges = EditRange.generateEditRange(fileHeader);
            FileSourceMetaData metaData = JGitUtils.getFileSourceMetaDataAtCommit(commit, changedFilePath, repository);
            editedPackages.add(metaData.getFilePackage());
            List<FileMethod> fileMethods = metaData.getFileMethods();

            for (EditRange range : editRanges) {
                editedMethods.addAll(range.getEditedMethods(fileMethods));
            }
        }

        List<File> editedFiles = JGitUtils.getChangedFiles(commit, prevCommit, repository);
        return new CommitSourceMetaData(
                new LinkedList<>(editedMethods),
                new LinkedList<>(editedPackages),
                editedFiles);
    }

    public static List<DiffEntry> getDiffs(RevCommit commit, Repository repository) throws IOException {
        return getDiffEntries(commit, getPreviousCommit(commit, repository), repository);
    }

    public static List<DiffEntry> getDiffEntries(RevCommit commit, RevCommit prevCommit, Repository repository) throws
            IOException {
        DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
        diffFormatter.setRepository(repository);
        diffFormatter.setDetectRenames(true);

        return diffFormatter.scan(prevCommit, commit);
    }

    public static boolean isRenamed(DiffEntry diffEntry) {
        return diffEntry.getChangeType() == DiffEntry.ChangeType.RENAME;
    }

    public static FileSourceMetaData getFileSourceMetaDataAtCommit(RevCommit commit, String path, Repository
            repository)
            throws IOException, XPathExpressionException, TransformerException {

        if (!JGitUtils.canGenerateASTForFile(path)) {
            return new FileSourceMetaData();
        }

        FileSourceMetaData fileSourceMetaData = new FileSourceMetaData();

        String fileContent = JGitUtils.getFileContentAtCommit(
                commit,
                path,
                repository);

        SrcML srcML = new SrcML();
        String xmlString;

        java.io.File file = new java.io.File("tmp/" + path);
        org.apache.commons.io.FileUtils.writeStringToFile(file, fileContent);
        xmlString = srcML.generateASTAsXMLString("tmp/" + path);
        if (!JGitUtils.canGenerateASTForFile(path)) {
            return new FileSourceMetaData();
        }
        file.deleteOnExit();
        fileSourceMetaData.setFileMethods(srcML.getFileMethods(xmlString, path));
        fileSourceMetaData.setFilePackage(srcML.getFilePackage(xmlString, path));
        return fileSourceMetaData;
    }

    public static String getFileContentAtCommit(RevCommit commit, String path, Repository repository) throws
            IOException {
        try (TreeWalk treeWalk = TreeWalk.forPath(repository, path, commit.getTree())) {
            ObjectId blobId = treeWalk.getObjectId(0);
            try (ObjectReader objectReader = repository.newObjectReader()) {
                ObjectLoader objectLoader = objectReader.open(blobId);
                byte[] bytes = objectLoader.getBytes();
                return new String(bytes, StandardCharsets.UTF_8);
            }
        }
    }

    public static int getTotalCommitCount(java.io.File localPath) {
        try {Git git = Git.init().setDirectory(localPath).call();
        Iterable<RevCommit> commits = null;

            commits = git.log().call();

        int count = 0;
        for( RevCommit commit : commits ) {
            count++;
        }
        return count;
        } catch (GitAPIException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static AbstractTreeIterator getCanonicalTreeParser(ObjectId commitId, Repository repository) throws
            IOException {

        if (commitId == null) {
            return new EmptyTreeIterator();
        }

        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(commitId);
            ObjectId treeId = commit.getTree().getId();
            try (ObjectReader reader = repository.newObjectReader()) {
                return new CanonicalTreeParser(null, reader, treeId);
            }
        }
    }

    private static boolean canGenerateASTForFile(String path) {

        for (String fileEnding : JGitUtils.allowedFileEndings) {
            if (path.endsWith(fileEnding)) {
                return true;
            }
        }
        return false;
    }
}



