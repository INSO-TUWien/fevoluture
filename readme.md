# Feature Dependency Evolution Visualization

## Prequesits
- Java 8
- Maven 3.3
- arangoDB (https://www.arangodb.com/)
- SrcML (https://www.srcml.org/#download)

Node and npm is installed locally during Maven build process.

## Running application
- Copy `src/main/resources/example.application.properties` and rename it to` application.properties`.
- Edit `application.properties` according to your settings.
- Ensure arangoDB is running.
- Execute `mvn clean package`
- Execute `java -jar target/feature-dep-viz-0.0.1-SNAPSHOT.jar`
- Open browser and go to  https://localhost:8080.

## Database import
While indexing might need multiple hours for larger repositories,
one might want to export/import an already available database.
This can be done by using the following commands.

### Export
Using [arangodump](https://www.arangodb.com/docs/stable/programs-arangodump.html):

`arangodump --output-directory "dump" --server.database <db-name> --server.username <username> --server-password <userpassword>`

### Import
Using [arangorestore](https://www.arangodb.com/docs/stable/programs-arangorestore.html):

`arangorestore --input-directory "dump" --server.database <db-name> --server.username <username> --server-password <userpassword`

### Index
To index everything call localhost:8080/api/setup.
Dependent on the size of the repository and history this can take multiple hours!
