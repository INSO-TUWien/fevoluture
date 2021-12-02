package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Just holds all repositories
 */
@Component
public class Repositories {

    @Autowired
    public CommitRepository commitRepository;

    @Autowired
    public FileRepository fileRepository;

    @Autowired
    public IssueRepository issueRepository;

    @Autowired
    public FileChangedTogetherRepository fileChangedTogetherRepository;

    @Autowired
    public MethodChangedTogetherRepository methodChangedTogetherRepository;

    @Autowired
    public PackageChangedTogetherRepository packageChangedTogetherRepository;

    @Autowired
    public IssueRelationRepository issueRelationRepository;

    @Autowired
    public CommitIssueRepository commitIssueRepository;

    @Autowired
    public FileCommitRepository fileCommitRepository;

    @Autowired
    public MethodRepository methodRepository;

    @Autowired
    public PackageRepository packageRepository;

    @Autowired
    public MethodCommitRepository methodCommitRepository;

    @Autowired
    public PackageCommitRepository packageCommitRepository;

    @Autowired public FeatureRepository featureRepository;
}
