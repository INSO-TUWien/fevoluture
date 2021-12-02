package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Feature;
import com.arangodb.springframework.repository.ArangoRepository;

public interface FeatureRepository extends ArangoRepository<Feature, String> {
}
