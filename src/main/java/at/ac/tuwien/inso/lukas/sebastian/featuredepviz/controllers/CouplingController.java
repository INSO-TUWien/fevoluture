package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.controllers;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.controllers.wrappers.CouplingRequest;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.*;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.services.CouplingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class CouplingController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CouplingService couplingService;

    @RequestMapping(value  = "/api/lcs/files",  method = RequestMethod.POST)
    public ResponseEntity getLogicalCouplingOfFiles(@RequestBody CouplingRequest request, @RequestParam long dateTime) {
        List<FileLogicalCoupling> couplings = couplingService.getLogicalCouplingOfFiles(request, dateTime);
        return ResponseEntity.ok(couplings);
    }

    @RequestMapping(value  = "/api/lcs/methods",  method = RequestMethod.POST)
    public ResponseEntity getLogicalCouplingOfMethods(@RequestBody CouplingRequest request, @RequestParam long dateTime) {
        List<MethodLogicalCoupling> couplings = couplingService.getLogicalCouplingOfMethods(request, dateTime);
        return ResponseEntity.ok(couplings);
    }

    @RequestMapping(value  = "/api/lcs/packages",  method = RequestMethod.POST)
    public ResponseEntity getLogicalCouplingOfPackages(@RequestBody CouplingRequest request, @RequestParam long dateTime) {
        List<PackageLogicalCoupling> couplings = couplingService.getLogicalCouplingOfPackages(request, dateTime);
        return ResponseEntity.ok(couplings);
    }

    @RequestMapping(value = "/api/lcs/commits", method = RequestMethod.POST)
    public ResponseEntity getCommitsOfFiles(@RequestBody CouplingRequest request, @RequestParam long dateTime) {
        List<Commit> commits = couplingService.getCommitOfCoupling(request, dateTime);
        return ResponseEntity.ok(commits);
    }

    @RequestMapping(value  = "/api/relations/files",  method = RequestMethod.GET)
    public ResponseEntity getLogicalCouplingOfFiles(@RequestParam String fileId1, @RequestParam String fileId2) {
        return ResponseEntity.ok(couplingService.getCommitsCausingFileCoupling(fileId1, fileId2));
    }

    @RequestMapping(value  = "/api/relations/methods",  method = RequestMethod.GET)
    public ResponseEntity getLogicalCouplingOfMethods(@RequestParam String methodId1, @RequestParam String methodId2) {
        return ResponseEntity.ok(couplingService.getCommitsCausingMethodCoupling(methodId1, methodId2));
    }

    @RequestMapping(value  = "/api/relations/packages",  method = RequestMethod.GET)
    public ResponseEntity getLogicalCouplingOfpackages(@RequestParam String packageId1, @RequestParam String packageId2) {
        return ResponseEntity.ok(couplingService.getCommitsCausingPackageCoupling(packageId1, packageId2));
    }
}
