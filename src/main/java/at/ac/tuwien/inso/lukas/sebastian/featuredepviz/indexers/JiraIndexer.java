package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.indexers;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.configs.ArangoConfig;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos.Repositories;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.Issue;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.IssueRelation;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.factories.IssueFactory;
import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.models.factories.IssueRelationFactory;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.auth.AnonymousAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;

@Service
public class JiraIndexer {


    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JiraRestClient jiraClient;

    @Value("${jira.url}")
    private String jiraUrl;

    @Value("${jira.credentials.username}")
    private String username;

    @Value("${jira.credentials.password}")
    private String password;

    @Value("${jira.project}")
    private String projectName;

    @Autowired
    private Repositories repositories;

    @Autowired
    private ArangoConfig arangoConfig;


    public void index() {
        logger.info("Start indexing issues...");
        storeIssues();
        storeIssueRelations();
        logger.info("Finished indexing issues...");
    }

    public void prepareCollections() {
        logger.info("Start preparing collections...");
        arangoConfig.createCollections();
        arangoConfig.createGraphs();
        logger.info("Finished preparing collections...");
    }

    public void storeIssues() {
        JiraRestClient jiraClient = getJiraRestClient();
        SearchRestClient searchClient = jiraClient.getSearchClient();
        IssueFactory issueFactory = new IssueFactory();

        int pageSize = 100;
        int currentIndex = 0;
        int total = 1;

        // store all issues
        while (currentIndex < total) {
            String query = "project=" + projectName;
            logger.info(String.format("Fetching issues %d to %d ...(total: %d)", currentIndex, currentIndex + pageSize, total));
            try {
                SearchResult searchResult = searchClient.searchJql(query, pageSize, currentIndex, null).claim();
                total = searchResult.getTotal();
                List<Issue> issues = new LinkedList<>();
                searchResult.getIssues().forEach(issue -> issues.add(issueFactory.buildIssue(issue)));
                repositories.issueRepository.saveAll(issues);
            } catch (RestClientException | NoSuchElementException exp) {
                exp.printStackTrace();
                logger.error(exp.getMessage());
            }
            currentIndex += pageSize;
        }
    }

    public void storeIssueRelations() {
        JiraRestClient jiraClient = getJiraRestClient();
        SearchRestClient searchClient = jiraClient.getSearchClient();

        IssueRelationFactory issueRelationFactory = new IssueRelationFactory();
        int pageSize = 100;
        int currentIndex = 0;
        int total = 1;
        // store all relationships
        while (currentIndex < total) {
            String query = "project=" + projectName;
            logger.info(String.format("Fetching issues %d to %d ...(total: %d)", currentIndex, currentIndex + pageSize, total));
            try {
                SearchResult searchResult = searchClient.searchJql(query, pageSize, currentIndex, null).claim();
                total = searchResult.getTotal();

                HashSet<String> issueKeys = new HashSet<>();
                searchResult.getIssues().forEach(sR -> {
                    issueKeys.add(sR.getKey());
                    if (sR.getIssueLinks() != null) {
                        sR.getIssueLinks().forEach(iL -> issueKeys.add(iL.getTargetIssueKey()));
                    }
                });

                List<Issue> pageIssues = repositories.issueRepository.findAllByKeyIn(issueKeys);
                HashMap<String, Issue> map = new HashMap<>();
                if (pageIssues != null) {
                    pageIssues.forEach(pI -> map.put(pI.getKey(), pI));
                }

                HashSet<IssueRelation> relations = new HashSet<>();
                searchResult.getIssues().forEach(sR -> {
                    if (sR.getIssueLinks() != null) {
                        sR.getIssueLinks().forEach(iL -> relations.add(issueRelationFactory
                                .buildIssueRelation(map.get(sR.getKey()),
                                        map.get(iL.getTargetIssueKey()), iL)));
                    }
                });
                repositories.issueRelationRepository.saveAll(relations);
            } catch (RestClientException | NoSuchElementException exp) {
                exp.printStackTrace();
                logger.error(exp.getMessage());
            }
            currentIndex += pageSize;
        }
    }
    public void clearExistingData() {
        logger.info("Clear issue data...");
        repositories.commitIssueRepository.deleteAll();
        repositories.issueRelationRepository.deleteAll();
        repositories.issueRelationRepository.deleteAll();
    }



    private JiraRestClient getJiraRestClient() {
        if (jiraClient == null) {
            jiraClient = new AsynchronousJiraRestClientFactory()
                    .create(getJiraUri(), new AnonymousAuthenticationHandler());
        }
        return jiraClient;
    }

    private URI getJiraUri() {
        return URI.create(this.jiraUrl);
    }
}
