package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.configs;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.CollectionType;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.model.CollectionCreateOptions;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.AbstractArangoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

@Configuration
@EnableArangoRepositories(basePackages = {"at.ac.tuwien.inso.lukas.sebastian.featuredepviz.daos"})
public class ArangoConfig extends AbstractArangoConfiguration {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${arangodb.host}")
    private String databaseHost;

    @Value("${arangodb.port}")
    private int databasePort;

    @Value("${arangodb.user}")
    private String userName;

    @Value("${arangodb.password}")
    private String userPassword;

    @Value("${arangodb.database}")
    private String databaseName;

    @Override
    public ArangoDB.Builder arango() {
        return new ArangoDB.Builder()
                .host(databaseHost, databasePort)
                .user(userName)
                .password(userPassword);
    }

    @Override
    public String database() {
        return databaseName;
    }

    public void createCollections() {
        ArangoDB arango = arango().build();
        ArangoDatabase db = arango.db(databaseName);

        String[] documentCollectionsNames = {
                "commits",
                "files",
                "issues",
                "packages",
                "methods",
                "features"
        };
        String[] edgeCollectionNames = {
                "filesChangedTogether",
                "methodsChangedTogether",
                "packagesChangedTogether",
                "relatedIssues",
                "commitToFile",
                "commitToMethod",
                "commitToPackage",
                "issueToCommit"
        };

        for (String collectionName : documentCollectionsNames) {
            if (!db.collection(collectionName).exists()) {
                db.collection(collectionName).create();
                logger.info("Created document collection " + collectionName);
            }
        }

        for (String collectionName : edgeCollectionNames) {
            if (!db.collection(collectionName).exists()) {
                CollectionCreateOptions options = new CollectionCreateOptions();
                options.type(CollectionType.EDGES);
                db.collection(collectionName).create(options);
                logger.info("Created edge collection " + collectionName);
            }
        }
    }

    public void createGraphs() {
        createGraph("issueToCommit", "issueToCommit", "issues", "commits");
        createGraph("commitToFile", "commitToFile", "commits", "files");
        createGraph("commitToMethod", "commitToMethod", "commits", "methods");
        createGraph("commitToPackage", "commitToPackage", "commits", "packages");
        createGraph("relatedIssues", "relatedIssues", "issues", "issues");
        createGraph("filesChangedTogether", "filesChangedTogether", "files", "files");
        createGraph("methodsChangedTogether", "methodsChangedTogether", "methods", "methods");
        createGraph("packagesChangedTogether", "packagesChangedTogether", "packages", "packages");
    }

    private void createGraph(String graphName, String edgeCollection, String from, String to) {
        ArangoDB arango = arango().build();
        ArangoDatabase db = arango.db(databaseName);
        List<EdgeDefinition> edgeDefinitions = new LinkedList<>();
        EdgeDefinition edgeDefinition = new EdgeDefinition();
        edgeDefinition.collection(edgeCollection);
        edgeDefinition.from(from);
        edgeDefinition.to(to);
        edgeDefinitions.add(edgeDefinition);
        if (!db.graph(graphName).exists()) {
            db.createGraph(graphName, edgeDefinitions);
            logger.info("Created graph " + graphName);
        }
    }
}
