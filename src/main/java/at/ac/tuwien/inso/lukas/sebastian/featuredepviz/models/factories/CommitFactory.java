package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.factories;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.jgit.JGitUtils;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.utils.CommitUtils;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos.Repositories;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Commit;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;

public class CommitFactory {


    public CommitFactory() {

    }

    public Commit buildCommit(RevCommit revCommit,
                              Repositories repositories,
                              String[] patterns) {
        Commit commit = new Commit();
        commit.setHash(revCommit.getName())
                .setAuthorName(revCommit.getAuthorIdent().getName())
                .setAuthorEmail(revCommit.getAuthorIdent().getEmailAddress())
                .setCommitTime(JGitUtils.getCommitTime(revCommit))
                .setMessage(revCommit.getFullMessage())
                .setIssues(repositories
                        .issueRepository
                        .findAllByKeyIn(CommitUtils.extractIssueKeys(revCommit.getFullMessage(), patterns)));
        return commit;
    }

    public Commit buildCommitWithChangedFiles(RevCommit revCommit,
                              RevCommit prevCommit,
                              Repository gitRepository,
                              Repositories repositories,
                              String[] patterns) throws IOException {
        Commit commit = new Commit();
        commit.setHash(revCommit.getName())
                .setAuthorName(revCommit.getAuthorIdent().getName())
                .setAuthorEmail(revCommit.getAuthorIdent().getEmailAddress())
                .setCommitTime(JGitUtils.getCommitTime(revCommit))
                .setMessage(revCommit.getFullMessage())
                .setIssues(repositories
                        .issueRepository
                        .findAllByKeyIn(CommitUtils.extractIssueKeys(revCommit.getFullMessage(), patterns)))
                .setChangedFiles(JGitUtils.getChangedFiles(revCommit, prevCommit, gitRepository));
        return commit;
    }
}
