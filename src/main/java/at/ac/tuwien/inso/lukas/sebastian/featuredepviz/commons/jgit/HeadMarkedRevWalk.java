package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.jgit;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;

public class HeadMarkedRevWalk extends RevWalk {

    public HeadMarkedRevWalk(Repository repo) throws IOException {
        super(repo);
        startAtHead(repo);
    }

    public HeadMarkedRevWalk(Repository repo, RevSort sorting) throws IOException {
        super(repo);
        startAtHead(repo, sorting);
    }

    private void startAtHead(Repository repo) throws IOException {
        Ref head = repo.exactRef("refs/heads/master");
        RevCommit commit = parseCommit(head.getObjectId());
        markStart(commit);
    }

    private void startAtHead(Repository repo, RevSort sorting) throws IOException {
        Ref head = repo.exactRef("refs/heads/master");
        RevCommit commit = parseCommit(head.getObjectId());
        sort(sorting);
        markStart(commit);
    }
}
