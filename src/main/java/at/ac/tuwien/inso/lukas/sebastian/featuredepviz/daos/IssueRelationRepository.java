package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Commit;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.IssueRelation;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.joda.time.DateTime;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueRelationRepository extends ArangoRepository<IssueRelation, String> {

    @Query("FOR vertex IN OUTBOUND @issueId GRAPH 'commitIssues'\n" +
            "    FILTER vertex.commitTime <= @commitTime\n" +
            "    RETURN vertex")
    List<Commit> findCommitsContainingIssueBefore(@Param("issueId") String issueId,@Param("commitTime") DateTime dateTime);


}
