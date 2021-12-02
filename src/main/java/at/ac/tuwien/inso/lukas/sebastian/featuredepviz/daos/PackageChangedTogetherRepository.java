package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.*;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Package;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.joda.time.DateTime;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PackageChangedTogetherRepository extends ArangoRepository<PackageChangedTogether, String> {

    @Query("FOR ct in #collection \n" +
            "    FILTER ct._from == @packageId\n" +
            "        RETURN ct")
    List<PackageChangedTogether> findAllByFromId(@Param("packageId") String fromId);

    @Query("FOR ct in #collection \n" +
            "    FILTER ct._from == @packageFrom AND ct._to == @packageTo\n" +
            "        RETURN ct")
    List<PackageChangedTogether> findAllByFromIdAndToId(@Param("packageFrom") String fromId, @Param("packageTo") String toId);

    @Query("FOR ct in #collection \n" +
            "    FILTER ct._from == @packageId\n" +
            "        RETURN DISTINCT ct._to")
    List<String> findAllDistinctToPackages(@Param("packageId") String fromId);

    @Query("FOR packageId in @packageIds\n" +
            "LET c0=(FOR c IN INBOUND packageId GRAPH 'commitToPackage'\n" +
            "FILTER c.commitTime <= @commitTime\n" +
            "RETURN c._id)\n" +
            "\n" +
            "LET couplings = (FOR vertex, edge IN ANY packageId GRAPH 'packagesChangedTogether'\n" +
            "\n" +
            "\tLET filteredChangedIn = (FOR changedInCommitId IN edge.changedIn\n" +
            "\tFILTER changedInCommitId IN c0\n" +
            "\tRETURN changedInCommitId)\n" +
            "\t\n" +
            "\tLET c1=(FOR c IN INBOUND vertex._id GRAPH 'commitToPackage'\n" +
            "    FILTER c.commitTime <=  @commitTime\n" +
            "    RETURN c._id)\n" +
            "\t\n" +
            "    FILTER (LENGTH(filteredChangedIn) >= @minCountFilter AND\n" +
            "    (LENGTH(filteredChangedIn) / COUNT(c0) >= @minLCFilter OR \n" +
            "    LENGTH(filteredChangedIn) / COUNT(c1) >= @minLCFilter)) OR \n" +
            "    (vertex._id in @packageIds AND LENGTH(filteredChangedIn) > 0) \n" +
            "    SORT LENGTH(filteredChangedIn) / COUNT(c0) DESC\n" +
            "    RETURN {\n" +
            "\t    package: vertex, \n" +
            "\t    countChangedTogether: LENGTH(filteredChangedIn),\n" +
            "\t    countRoot: COUNT(c0),\n" +
            "\t    countPackage: COUNT(c1),\n" +
            "\t    confidence: LENGTH(filteredChangedIn) / COUNT(c0),\n" +
            "\t    support: LENGTH(filteredChangedIn) / (COUNT(c0) + COUNT(c1) - COUNT(filteredChangedIn))\n" +
            "\t})\n" +
            "RETURN DISTINCT {root: packageId, couplings: couplings}\n" +
            "\t")
    List<PackageLogicalCoupling> findLogicalCouplingOfPackagesBefore(
            @Param("packageIds") List<String> packageIds,
            @Param("commitTime") DateTime time,
            @Param("minCountFilter") int minCount,
            @Param("minLCFilter") double minLC);


    @Query("FOR packageId IN @packageIds\n" +
            "   LET c0=(FOR c IN INBOUND packageId GRAPH 'packagesChangedTogether' \n" +
            "       FILTER c.commitTime <= @commitTime \n" +
            "           RETURN c)\n" +
            "  FOR vertex, edge IN ANY packageId GRAPH 'packagesChangedTogether'\n" +
            "       RETURN edge.changedIn")
    List<Commit> findCommitsOfPackagesBefore(List<String> packageIds, long dateTime);

    PackageChangedTogether findAllByPackage1AndPackage2OrPackage2AndPackage1(String packageId11, String packageId21, String packageId12, String packageId22);

}
