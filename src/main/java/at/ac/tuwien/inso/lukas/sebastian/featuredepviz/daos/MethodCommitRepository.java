package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.CommitToMethod;
import com.arangodb.springframework.repository.ArangoRepository;

public interface MethodCommitRepository extends ArangoRepository<CommitToMethod, String> {
}
