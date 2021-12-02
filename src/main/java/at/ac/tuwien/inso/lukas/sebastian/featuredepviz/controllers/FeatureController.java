package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.controllers;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.exceptions.ServiceException;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Feature;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.services.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
public class FeatureController extends APIController {

    @Autowired
    private FeatureService featureService;

    @RequestMapping("/api/features")
    public ResponseEntity<Page<Feature>> getAllFeatures(@RequestParam Map<String, Object> allRequestParams) {
        int page = Integer.parseInt(allRequestParams.get("page").toString());
        int pageSize = Integer.parseInt(allRequestParams.get("pageSize").toString());
        allRequestParams.remove("page");
        allRequestParams.remove("pageSize");
        return ResponseEntity.ok(featureService.getFeatures(
                generatePageable(page, pageSize)));
    }

    @PostMapping("/api/features")
    public ResponseEntity<Feature> saveFeature(@RequestBody Feature feature) {
        Feature storedFeature = this.featureService.saveFeature(feature);
        try {
            return ResponseEntity.ok(storedFeature);
        } catch (ServiceException e) {
            return generateErrorResponseEntity(e);
        }
    }

    @DeleteMapping("/api/features/{featureId}")
    public ResponseEntity deleteFeature(@PathVariable String featureId) {
        featureService.deleteFeature(featureId);
        return ResponseEntity.ok().build();
    }


}
