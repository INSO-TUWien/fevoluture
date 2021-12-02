package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.File;
import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import org.joda.time.DateTime;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends ArangoRepository<File, String> {

    Optional<File> findOneByPath(String path);

    List<File> findAllByPathIn(List<String> paths);

    List<File> findAllByNameIn(List<String> names);


    // +-----------------------------------------------------------+
    // | Issue queries are used to detect files of related issues. |
    // +-----------------------------------------------------------+

    @Query(" LET issueFiles = UNIQUE(FLATTEN(\n" +
            "    FOR vertex IN OUTBOUND @issueId GRAPH 'issueToCommit'\n" +
            "        RETURN vertex.changedFiles\n" +
            "        ))\n" +
            "FOR f in files\n" +
            "    FILTER f._id IN issueFiles\n" +
            "        RETURN f")
    List<File> findFilesOfIssue(@Param("issueId") String issueId);

    @Query(" LET issueFiles = UNIQUE(FLATTEN(\n" +
            "    FOR vertex IN OUTBOUND @issueId GRAPH 'issueToCommit'\n" +
            "        FILTER vertex.commitTime <= @commitTime\n" +
            "        RETURN vertex.changedFiles\n" +
            "        ))\n" +
            "FOR f in files\n" +
            "    FILTER f._id IN issueFiles\n" +
            "        RETURN DISTINCT f")
    List<File> findFilesOfIssueBefore(@Param("issueId") String issueId, @Param("commitTime") DateTime date);


    @Query("FOR issueId IN @issueIds\n" +
            "LET issueFiles = UNIQUE(FLATTEN(\n" +
            "    FOR vertex IN OUTBOUND issueId GRAPH 'issueToCommit'\n" +
            "        RETURN vertex.changedFiles\n" +
            "        ))\n" +
            "FOR f in files\n" +
            "    FILTER f._id IN issueFiles\n" +
            "        RETURN f")
    List<File> findFilesOfIssues(@Param("issueIds") List<String> issueIds);

    @Query("FOR issueId IN @issueIds\n" +
            "LET issueFiles = UNIQUE(FLATTEN(\n" +
            "    FOR vertex IN OUTBOUND issueId GRAPH 'issueToCommit'\n" +
            "        FILTER vertex.commitTime <= @commitTime\n" +
            "        RETURN vertex.changedFiles\n" +
            "        ))\n" +
            "FOR f in files\n" +
            "    FILTER f._id IN issueFiles\n" +
            "        RETURN DISTINCT f")
    List<File> findFilesOfIssuesBefore(@Param("issueIds") List<String> issueIds, @Param("commitTime") DateTime date);


}
