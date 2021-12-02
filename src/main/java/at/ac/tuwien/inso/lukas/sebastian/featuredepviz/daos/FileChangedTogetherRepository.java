package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Commit;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.FileChangedTogether;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.FileLogicalCoupling;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.joda.time.DateTime;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileChangedTogetherRepository extends ArangoRepository<FileChangedTogether, String> {


    @Query("FOR ct in #collection \n" +
            "    FILTER ct._from == @fileId\n" +
            "        RETURN ct")
    List<FileChangedTogether> findAllByFromId(@Param("fileId") String fromId);

    @Query("FOR fileId in @fileIds\n" +
            "LET c0=(FOR c IN INBOUND fileId GRAPH 'commitToFile'\n" +
            "FILTER c.commitTime <= @commitTime\n" +
            "RETURN c._id)\n" +
            "\n" +
            "LET couplings = (FOR vertex, edge IN ANY fileId GRAPH 'filesChangedTogether'\n" +
            "\n" +
            "\tLET filteredChangedIn = (FOR changedInCommitId IN edge.changedIn\n" +
            "\tFILTER changedInCommitId IN c0\n" +
            "\tRETURN changedInCommitId)\n" +
            "\t\n" +
            "\tLET c1=(FOR c IN INBOUND vertex._id GRAPH 'commitToFile'\n" +
            "    FILTER c.commitTime <=  @commitTime\n" +
            "    RETURN c._id)\n" +
            "\t\n" +
            "    FILTER (LENGTH(filteredChangedIn) >= @minCountFilter AND\n" +
            "    (LENGTH(filteredChangedIn) / COUNT(c0) >= @minLCFilter OR \n" +
            "    LENGTH(filteredChangedIn) / COUNT(c1) >= @minLCFilter)) OR \n" +
            "    (vertex._id in @fileIds AND LENGTH(filteredChangedIn) > 0) \n" +
            "    SORT LENGTH(filteredChangedIn) / COUNT(c0) DESC\n" +
            "    RETURN {\n" +
            "\t    file: vertex, \n" +
            "\t    countChangedTogether: LENGTH(filteredChangedIn),\n" +
            "\t    countRoot: COUNT(c0),\n" +
            "\t    countFile: COUNT(c1),\n" +
            "\t    confidence: LENGTH(filteredChangedIn) / COUNT(c0),\n" +
            "\t    support: LENGTH(filteredChangedIn) / (COUNT(c0) + COUNT(c1) - COUNT(filteredChangedIn))\n" +
            "\t})\n" +
            "RETURN DISTINCT {root: fileId, couplings: couplings}\n" +
            "\t")
    List<FileLogicalCoupling> findLogicalCouplingOfFilesBefore(
            @Param("fileIds") List<String> fileIds,
            @Param("commitTime") DateTime time,
            @Param("minCountFilter") int minCount,
            @Param("minLCFilter") double minLC);


    @Query("FOR fileId IN @fileIds\n" +
            "   LET c0=(FOR c IN INBOUND fileId GRAPH 'commitToFile' \n" +
            "       FILTER c.commitTime <= @commitTime \n" +
            "           RETURN c)\n" +
            "  FOR vertex, edge IN ANY fileId GRAPH 'changedTogether'\n" +
            "       RETURN edge.changedIn")
    List<Commit> findCommitsOfFilesBefore(List<String> fileIds, long dateTime);


    FileChangedTogether findAllByFile1AndFile2OrFile2AndFile1(
            String fileId11, String fileId21, String fileId12, String fileId22);

}
