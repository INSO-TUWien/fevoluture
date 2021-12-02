package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Commit;
import com.arangodb.springframework.annotation.BindVars;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommitRepository extends ArangoRepository<Commit, String> {

    Optional<Commit> findOneByHash(String hash);

    @Query("FOR vertex IN OUTBOUND @issueId GRAPH 'issueToCommit'\n" +
            "    RETURN vertex")
    List<Commit> findCommitsContainingIssue(@Param("issueId") String issueId);

    @Query("FOR vertex IN OUTBOUND @issueId GRAPH 'issueToCommit'\n" +
            "    FILTER vertex.commitTime <= @commitTime\n" +
            "    RETURN vertex")
    List<Commit> findCommitsContainingIssueBefore(@Param("issueId") String issueId, @Param("commitTime") DateTime dateTime);

    @Query("FOR issueId In @issueIds\n" +
            "   FOR vertex IN OUTBOUND issueId GRAPH 'issueToCommit'\n" +
            "       RETURN DISTINCT vertex")
    List<Commit> findCommitsContainingIssues(@Param("issueIds") List<String> issueIds);

    @Query("FOR issueId In @issueIds\n" +
            "   FOR vertex IN OUTBOUND issueId GRAPH 'issueToCommit'\n" +
            "       FILTER vertex.commitTime <= @commitTime\n" +
            "           RETURN DISTINCT vertex")
    List<Commit> findCommitsContainingIssuesBefore(@Param("issueIds") List<String> issueIds, @Param("commitTime") DateTime dateTime);

    List<Commit> findByCommitTimeIsLessThanEqual(DateTime date);

    @Query("FOR commit IN commits\n" +
            "    SORT commit.authorName ASC\n" +
            "    RETURN DISTINCT commit.authorName")
    List<String> findAllCommitAuthors();

    @Query("FOR commit IN commits FILTER\n" +
            " commit.commitTime >= @fromDate AND\n" +
            " commit.commitTime <= @toDate AND\n" +
            " commit.authorName LIKE @author AND\n" +
            " commit.message LIKE @msg AND\n" +
            " commit._id LIKE @hash" +
            " #pageable RETURN commit")
    Page<Commit> query(Pageable page, @BindVars Map<String, Object> bindVars);



    @Query("LET fileMatches = (\n" +
            "FOR file IN files\n" +
            "FILTER file.path LIKE @fileName\n" +
            "RETURN file\n" +
            ")\n" +
            "\n" +
            "LET commitsWithFileName = (\n" +
            "FOR f in fileMatches\n" +
            "    FOR vertex, edge\n" +
            "    IN INBOUND f\n" +
            "    GRAPH 'commitToFile'\n" +
            "    FILTER vertex.commitTime >= @fromDate AND\n" +
            "    vertex.commitTime <= @toDate AND\n" +
            "    vertex.authorName LIKE @author AND\n" +
            "    vertex.message LIKE @msg AND\n" +
            "    vertex._id LIKE @hash\n" +
            "    RETURN DISTINCT vertex\n" +
            "    )\n" +
            "\n" +
            "FOR commit IN commitsWithFileName\n" +
            "#pageable RETURN commit")
    Page<Commit> queryWithFilename(Pageable page,
                                   @BindVars Map<String, Object> bindVars,
                                   @Param("fileName") String fileName);
}
