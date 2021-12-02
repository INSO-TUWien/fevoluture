package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Commit;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.CommitToFile;
import com.arangodb.springframework.repository.ArangoRepository;

import java.util.List;

public interface FileCommitRepository extends ArangoRepository<CommitToFile, String> {
    List<CommitToFile> getAllByCommit_Hash(String hash);
}
