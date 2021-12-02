package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Issue;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.IssueRelation;
import com.arangodb.springframework.annotation.BindVars;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IssueRepository extends ArangoRepository<Issue, String> {

    HashSet<Issue> findByKeyIn(HashSet<String> keys);

    Optional<Issue> findOneByKey(String key);

    List<Issue> findAllByKeyIn(List<String> keys);

    List<Issue> findAllByKeyIn(HashSet<String> keys);

    @Query("FOR vertex, edge IN 1..@depth ANY @issueId GRAPH 'relatedIssues'\n" +
            "    FILTER vertex._id != @issueId\n" +
            "       RETURN DISTINCT vertex")
    List<Issue> findAllRelatedIssues(@Param("issueId") String issueId, @Param("depth") int maxDepth);

    @Query("FOR vertex, edge IN 1..@depth OUTBOUND @issueId GRAPH 'relatedIssues'\n" +
            "    FILTER vertex._id != @issueId\n" +
            "       RETURN DISTINCT edge")
    List<IssueRelation> findAllRelatedIssues2(@Param("issueId") String issueId, @Param("depth") int maxDepth);

    @Query("FOR issue IN issues FILTER\n" +
            " issue.updatedAt >= @fromDate AND\n" +
            " issue.updatedAt <= @toDate AND\n" +
            " issue.authorName LIKE @author AND\n" +
            " issue.tracker LIKE @tracker AND\n" +
            " issue.status LIKE @status AND\n" +
            " issue.key LIKE @issueId AND\n" +
            " issue.title LIKE @text\n" +
            " #pageable RETURN issue")
    Page<Issue> query(Pageable page, @BindVars Map<String, Object> bindVars);


    @Query("FOR issue IN issues\n" +
            " FILTER issue.authorName LIKE @partialAuthorName\n" +
            "    SORT issue.authorName ASC\n" +
            "    RETURN DISTINCT issue.authorName")
    List<String> findIssueAuthors(@Param("partialAuthorName") String partialAuthorName);

    @Query("FOR issue IN issues\n" +
            "    SORT issue.tracker ASC\n" +
            "    RETURN DISTINCT issue.tracker")
    List<String> findAllTrackers();

    @Query("FOR issue IN issues\n" +
            "    SORT issue.status ASC\n" +
            "    RETURN DISTINCT issue.status")
    List<String> findAllStatus();
}
