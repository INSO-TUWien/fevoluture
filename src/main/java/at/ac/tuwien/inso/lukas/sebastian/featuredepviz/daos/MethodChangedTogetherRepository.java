package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Commit;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.MethodChangedTogether;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.MethodLogicalCoupling;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.joda.time.DateTime;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MethodChangedTogetherRepository extends ArangoRepository<MethodChangedTogether, String> {

    @Query("FOR ct in #collection \n" +
            "    FILTER ct._from == @methodId\n" +
            "        RETURN ct")
    List<MethodChangedTogether> findAllByFromId(@Param("methodId") String fromId);

    @Query("FOR methodId in @methodIds\n" +
            "LET c0=(FOR c IN INBOUND methodId GRAPH 'commitToMethod'\n" +
            "FILTER c.commitTime <= @commitTime\n" +
            "RETURN c._id)\n" +
            "\n" +
            "LET couplings = (FOR vertex, edge IN ANY methodId GRAPH 'methodsChangedTogether'\n" +
            "\n" +
            "\tLET filteredChangedIn = (FOR changedInCommitId IN edge.changedIn\n" +
            "\tFILTER changedInCommitId IN c0\n" +
            "\tRETURN changedInCommitId)\n" +
            "\t\n" +
            "\tLET c1=(FOR c IN INBOUND vertex._id GRAPH 'commitToMethod'\n" +
            "    FILTER c.commitTime <=  @commitTime\n" +
            "    RETURN c._id)\n" +
            "\t\n" +
            "    FILTER (LENGTH(filteredChangedIn) >= @minCountFilter AND\n" +
            "    (LENGTH(filteredChangedIn) / COUNT(c0) >= @minLCFilter OR \n" +
            "    LENGTH(filteredChangedIn) / COUNT(c1) >= @minLCFilter)) OR \n" +
            "    (vertex._id in @methodIds AND LENGTH(filteredChangedIn) > 0) \n" +
            "    SORT LENGTH(filteredChangedIn) / COUNT(c0) DESC\n" +
            "    RETURN {\n" +
            "\t    method: vertex, \n" +
            "\t    countChangedTogether: LENGTH(filteredChangedIn),\n" +
            "\t    countRoot: COUNT(c0),\n" +
            "\t    countMethod: COUNT(c1),\n" +
            "\t    confidence: LENGTH(filteredChangedIn) / COUNT(c0),\n" +
            "\t    support: LENGTH(filteredChangedIn) / (COUNT(c0) + COUNT(c1) - COUNT(filteredChangedIn))\n" +
            "\t})\n" +
            "RETURN DISTINCT {root: methodId, couplings: couplings}\n" +
            "\t")
    List<MethodLogicalCoupling> findLogicalCouplingOfMethodsBefore(
            @Param("methodIds") List<String> methodIds,
            @Param("commitTime") DateTime time,
            @Param("minCountFilter") int minCount,
            @Param("minLCFilter") double minLC

    );


    @Query("FOR methodId IN @methodIds\n" +
            "   LET c0=(FOR c IN INBOUND methodId GRAPH 'commitToMethod' \n" +
            "       FILTER c.commitTime <= @commitTime \n" +
            "           RETURN c)\n" +
            "  FOR vertex, edge IN ANY methodId GRAPH 'methodsChangedTogether'\n" +
            "       RETURN edge.changedIn")
    List<Commit> findCommitsOfMethodsBefore(List<String> methodIds, long dateTime);

    MethodChangedTogether findAllByMethod1AndMethod2OrMethod2AndMethod1(String methodId11, String methodId21,
                                                                        String methodId12, String methodId22);

}
