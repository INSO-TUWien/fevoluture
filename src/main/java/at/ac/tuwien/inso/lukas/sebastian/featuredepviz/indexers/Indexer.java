package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.indexers;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.configs.ArangoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Indexer {

    @Autowired
    GitIndexer gitIndexer;
    @Autowired
    JiraIndexer jiraIndexer;
    @Autowired
    SourceCodeIndexer sourceCodeIndexer;
    @Autowired
    ArangoConfig arangoConfig;

    public void index() throws IOException {
        arangoConfig.createCollections();
        arangoConfig.createGraphs();
        jiraIndexer.index();
        gitIndexer.index();
        sourceCodeIndexer.index();
    }

    public void clearAll() {
        sourceCodeIndexer.clearExistingData();
        gitIndexer.clearExistingData();
        jiraIndexer.clearExistingData();
    }
}
