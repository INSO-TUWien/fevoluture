package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.IssueToCommit;
import com.arangodb.springframework.repository.ArangoRepository;

public interface CommitIssueRepository extends ArangoRepository<IssueToCommit, String> {
}
