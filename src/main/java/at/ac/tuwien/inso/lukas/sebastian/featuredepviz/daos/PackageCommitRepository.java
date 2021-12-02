package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.CommitToPackage;
import com.arangodb.springframework.repository.ArangoRepository;

public interface PackageCommitRepository extends ArangoRepository<CommitToPackage, String> {
}
