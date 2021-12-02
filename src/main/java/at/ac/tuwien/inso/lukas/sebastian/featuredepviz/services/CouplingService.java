package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.services;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.controllers.wrappers.CouplingRequest;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos.Repositories;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouplingService {

    @Autowired
    private Repositories repositories;

    public List<FileLogicalCoupling> getLogicalCouplingOfFiles(CouplingRequest request, long dateTime) {
        List<String> fileIds = request.getSourceEntityIds().stream().map(fileId -> "files/" + fileId).collect(Collectors.toList());
        return repositories.fileChangedTogetherRepository
                .findLogicalCouplingOfFilesBefore(
                        fileIds,
                        new DateTime(dateTime),
                        request.getMinCountFilter(),
                        request.getMinLCFilter());
    }

    public List<MethodLogicalCoupling> getLogicalCouplingOfMethods(CouplingRequest request, long dateTime) {
        List<String> methodIds = request.getSourceEntityIds().stream().map(methodId -> "methods/" + methodId).collect(Collectors.toList());
        return repositories.methodChangedTogetherRepository
                .findLogicalCouplingOfMethodsBefore(
                        methodIds,
                        new DateTime(dateTime),
                        request.getMinCountFilter(),
                        request.getMinLCFilter());
    }

    public List<PackageLogicalCoupling> getLogicalCouplingOfPackages(CouplingRequest request, long dateTime) {
        List<String> packageIds = request.getSourceEntityIds().stream().map(fileId -> "packages/" + fileId).collect(Collectors.toList());
        return repositories.packageChangedTogetherRepository
                .findLogicalCouplingOfPackagesBefore(
                        packageIds,
                        new DateTime(dateTime),
                        request.getMinCountFilter(),
                        request.getMinLCFilter());
    }

    public List<Commit> getCommitOfCoupling(CouplingRequest request, long dateTime) {
        List<String> fileIds = convertSourceCodeEntitiesTo(request, "files");
        return repositories.fileChangedTogetherRepository.findCommitsOfFilesBefore(fileIds, dateTime);
    }

    public List<String> convertSourceCodeEntitiesTo(CouplingRequest request, String prefix) {
        return request.getSourceEntityIds().stream().map(fileId -> prefix + "/" + fileId).collect(Collectors.toList());
    }

    public List<Commit> getCommitsCausingFileCoupling(String fileId1, String fileId2) {
        FileChangedTogether fct = repositories.fileChangedTogetherRepository
                .findAllByFile1AndFile2OrFile2AndFile1("files/" + fileId1, "files/" + fileId2, "files/" + fileId1, "files/" + fileId2);
        if (fct != null) {
            return fct.getChangedIn();
        }
        return new LinkedList<>();
    }

    public List<Commit> getCommitsCausingPackageCoupling(String packageId1, String packageId2) {
        PackageChangedTogether pct = repositories.packageChangedTogetherRepository
                .findAllByPackage1AndPackage2OrPackage2AndPackage1("packages/" + packageId1, "packages/" + packageId2, "packages/" + packageId1, "packages/" + packageId2);
        if (pct != null) {
            return pct.getChangedIn();
        }
        return new LinkedList<>();
    }

    public List<Commit> getCommitsCausingMethodCoupling(String fileId1, String fileId2) {
        MethodChangedTogether mct = repositories.methodChangedTogetherRepository
                .findAllByMethod1AndMethod2OrMethod2AndMethod1("methods/" + fileId1, "methods/" + fileId2, "methods/" + fileId1, "methods/" + fileId2);
        if (mct != null) {
            return mct.getChangedIn();
        }
        return new LinkedList<>();
    }

}
