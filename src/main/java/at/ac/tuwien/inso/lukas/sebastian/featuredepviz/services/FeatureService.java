package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.services;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos.Repositories;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Feature;
import com.arangodb.ArangoDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@ComponentScan("at.ac.tuwien.inso.lukas.sebastian.featuredepviz")
public class FeatureService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Repositories repositories;

    public Page<Feature> getFeatures(Pageable pageable) {
        return this.repositories.featureRepository.findAll(pageable);
    }

    public Feature saveFeature(Feature feature) {
        return this.repositories.featureRepository.save(feature);
    }

    public void deleteFeature(String featureId) {
        try {
            this.repositories.featureRepository.deleteById(featureId);
        } catch (ArangoDBException arangoDBException) {
            if (arangoDBException.getErrorNum() == 1202) {
                logger.info(String.format("Feature %s already deleted", featureId));
            } else {
                throw arangoDBException;
            }
        }
    }
}
