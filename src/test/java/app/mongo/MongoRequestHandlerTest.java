package app.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class MongoRequestHandlerTest {

    private static final String PATH_TO_DB_PROPERTIES = "target/classes/database.properties";
    private static final String PATH_TO_TEST_DATA = "target/classes/mongo-test-data.txt";

    private static final String MONGO_URI = "mongo.uri";
    private static final String MONGO_DATABASE_NAME = "mongo.database.name";
    private static final String MONGO_COLLECTION_NAME = "mongo.collection.name";

    private static Properties properties;
    private static String mongoClientUri;
    private static String testCollectionName;
    private static MongoClient client;
    private static MongoDatabase database;
    private static MongoCollection<Document> testCollection;
    private static List<Document> documents;

    private MongoRequestHandler testObject;

    @BeforeClass
    public static void setUpClass() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream(PATH_TO_DB_PROPERTIES));

        mongoClientUri = properties.getProperty(MONGO_URI) + "/" + properties.getProperty(MONGO_DATABASE_NAME);
        testCollectionName = properties.getProperty(MONGO_COLLECTION_NAME);

        MongoClientURI mongoClientURI = new MongoClientURI(mongoClientUri);
        client = new MongoClient(mongoClientURI);
        database = client.getDatabase(mongoClientURI.getDatabase());
        database.createCollection(testCollectionName);
        testCollection = database.getCollection(testCollectionName);
        documents = Files
                .lines(Paths.get(PATH_TO_TEST_DATA))
                .map(Document::parse)
                .collect(Collectors.toList());
    }

    @Before
    public void setUp() throws IOException {
        testCollection.insertMany(documents);
        testObject = new MongoRequestHandler(database);
    }

    @Test
    public void test_select_star_from_query() {
        //equals SELECT * FROM test-mongo-client
        List<Document> expectedList = new ArrayList<Document>() {{
            add(Document.parse("{ \"_id\" : \"1\", \"x\" : 1 , \"y\": \"1\", \"name\": \"Anton\"}"));
            add(Document.parse("{ \"_id\" : \"2\", \"x\" : 2 , \"y\": \"1\", \"name\": \"Bob\"}"));
            add(Document.parse("{ \"_id\" : \"3\", \"x\" : 3 , \"y\": \"2\", \"name\": \"Cen\"}"));
            add(Document.parse("{ \"_id\" : \"4\", \"x\" : 4 , \"y\": \"3\", \"name\": \"Don\"}"));
            add(Document.parse("{ \"_id\" : \"5\", \"x\" : 5 , \"y\": \"2\", \"name\": \"Eva\"}"));
            add(Document.parse("{ \"_id\" : \"6\", \"x\" : 6 , \"y\": \"3\", \"name\": \"Fin\"}"));
            add(Document.parse("{ \"_id\" : \"7\", \"x\" : 7 , \"y\": \"4\", \"name\": \"Georg\"}"));
            add(Document.parse("{ \"_id\" : \"8\", \"x\" : 8 , \"y\": \"5\", \"name\": \"Hi\"}"));
            add(Document.parse("{ \"_id\" : \"9\", \"x\" : 9 , \"y\": \"3\", \"name\": \"Ivan\"}"));
            add(Document.parse("{ \"_id\" : \"10\", \"x\" : 10 ,\"y\": \"3\", \"name\": \"Jon\"}"));
            add(Document.parse("{ \"_id\" : \"11\", \"x\" : 11 ,\"y\": \"1\", \"name\": \"Bill\"}"));
            add(Document.parse("{ \"_id\" : \"12\", \"x\" : 12 ,\"y\": \"3\", \"name\": \"Inga\"}"));
        }};
        PreparedMongoQuery query = PreparedMongoQuery.builder()
                .target(testCollectionName)
                .projections(Collections.emptyList())
                .conditions(new BasicDBObject())
                .fields(new BasicDBObject())
                .skipRecords(0)
                .limitRecords(0)
                .build();

        List<Document> actualList = testObject.doQuery(query);
        assertEquals(12, actualList.size());
        for (int i = 0; i < actualList.size(); i++) {
            assertEquals(actualList.get(i), expectedList.get(i));
        }
    }

    @Test
    public void test_select_has_projections_from_query() {
        //equals SELECT x, name FROM test-mongo-client
        List<Document> expectedList = new ArrayList<Document>() {{
            add(Document.parse("{ \"_id\" : \"1\", \"x\" : 1 , \"name\": \"Anton\"}"));
            add(Document.parse("{ \"_id\" : \"2\", \"x\" : 2 , \"name\": \"Bob\"}"));
            add(Document.parse("{ \"_id\" : \"3\", \"x\" : 3 , \"name\": \"Cen\"}"));
            add(Document.parse("{ \"_id\" : \"4\", \"x\" : 4 , \"name\": \"Don\"}"));
            add(Document.parse("{ \"_id\" : \"5\", \"x\" : 5 , \"name\": \"Eva\"}"));
            add(Document.parse("{ \"_id\" : \"6\", \"x\" : 6 , \"name\": \"Fin\"}"));
            add(Document.parse("{ \"_id\" : \"7\", \"x\" : 7 , \"name\": \"Georg\"}"));
            add(Document.parse("{ \"_id\" : \"8\", \"x\" : 8 , \"name\": \"Hi\"}"));
            add(Document.parse("{ \"_id\" : \"9\", \"x\" : 9 , \"name\": \"Ivan\"}"));
            add(Document.parse("{ \"_id\" : \"10\", \"x\" : 10 ,\"name\": \"Jon\"}"));
            add(Document.parse("{ \"_id\" : \"11\", \"x\" : 11 ,\"name\": \"Bill\"}"));
            add(Document.parse("{ \"_id\" : \"12\", \"x\" : 12 ,\"name\": \"Inga\"}"));
        }};
        PreparedMongoQuery query = PreparedMongoQuery.builder()
                .target(testCollectionName)
                .projections(Arrays.asList("x", "name"))
                .conditions(new BasicDBObject())
                .fields(new BasicDBObject())
                .skipRecords(0)
                .limitRecords(0)
                .build();

        List<Document> actualList = testObject.doQuery(query);
        assertEquals(12, actualList.size());
        for (int i = 0; i < actualList.size(); i++) {
            assertEquals(actualList.get(i), expectedList.get(i));
        }
    }

    @Test
    public void test_select_has_projections_from_query_and_one_where_condition() {
        //equals SELECT x, name FROM test-mongo-client WHERE x >= 5
        List<Document> expectedList = new ArrayList<Document>() {{
            add(Document.parse("{ \"_id\" : \"5\", \"x\" : 5 , \"name\": \"Eva\"}"));
            add(Document.parse("{ \"_id\" : \"6\", \"x\" : 6 , \"name\": \"Fin\"}"));
            add(Document.parse("{ \"_id\" : \"7\", \"x\" : 7 , \"name\": \"Georg\"}"));
            add(Document.parse("{ \"_id\" : \"8\", \"x\" : 8 , \"name\": \"Hi\"}"));
            add(Document.parse("{ \"_id\" : \"9\", \"x\" : 9 , \"name\": \"Ivan\"}"));
            add(Document.parse("{ \"_id\" : \"10\", \"x\" : 10 ,\"name\": \"Jon\"}"));
            add(Document.parse("{ \"_id\" : \"11\", \"x\" : 11 ,\"name\": \"Bill\"}"));
            add(Document.parse("{ \"_id\" : \"12\", \"x\" : 12 ,\"name\": \"Inga\"}"));
        }};
        PreparedMongoQuery query = PreparedMongoQuery.builder()
                .target(testCollectionName)
                .projections(Arrays.asList("x", "name"))
                .conditions(BasicDBObject.parse("{\"x\" : {$gte : 5}}"))
                .fields(new BasicDBObject())
                .skipRecords(0)
                .limitRecords(0)
                .build();

        List<Document> actualList = testObject.doQuery(query);
        assertEquals(8, actualList.size());
        for (int i = 0; i < actualList.size(); i++) {
            assertEquals(actualList.get(i), expectedList.get(i));
        }
    }

    @Test
    public void test_select_has_projections_from_query_and_complex_where_condition_1() {
        //equals SELECT x, name FROM test-mongo-client WHERE x >= 5 AND x < 7
        List<Document> expectedList = new ArrayList<Document>() {{
            add(Document.parse("{ \"_id\" : \"5\", \"x\" : 5 , \"name\": \"Eva\"}"));
            add(Document.parse("{ \"_id\" : \"6\", \"x\" : 6 , \"name\": \"Fin\"}"));
        }};
        PreparedMongoQuery query = PreparedMongoQuery.builder()
                .target(testCollectionName)
                .projections(Arrays.asList("x", "name"))
                .conditions(BasicDBObject.parse("{ \"$and\" : [ { \"x\" : { \"$gte\" : 5}} , { \"x\" : { \"$lt\" : 7}}]}"))
                .fields(new BasicDBObject())
                .skipRecords(0)
                .limitRecords(0)
                .build();

        List<Document> actualList = testObject.doQuery(query);
        assertEquals(2, actualList.size());
        for (int i = 0; i < actualList.size(); i++) {
            assertEquals(actualList.get(i), expectedList.get(i));
        }
    }

    @Test
    public void test_select_has_projections_from_query_and_complex_where_condition_2() {
        //equals SELECT x, name FROM test-mongo-client WHERE x >= 8 OR name = Bob
        List<Document> expectedList = new ArrayList<Document>() {{
            add(Document.parse("{ \"_id\" : \"2\", \"x\" : 2 , \"name\": \"Bob\"}"));
            add(Document.parse("{ \"_id\" : \"8\", \"x\" : 8 , \"name\": \"Hi\"}"));
            add(Document.parse("{ \"_id\" : \"9\", \"x\" : 9 , \"name\": \"Ivan\"}"));
            add(Document.parse("{ \"_id\" : \"10\", \"x\" : 10 ,\"name\": \"Jon\"}"));
            add(Document.parse("{ \"_id\" : \"11\", \"x\" : 11 ,\"name\": \"Bill\"}"));
            add(Document.parse("{ \"_id\" : \"12\", \"x\" : 12 ,\"name\": \"Inga\"}"));
        }};
        PreparedMongoQuery query = PreparedMongoQuery.builder()
                .target(testCollectionName)
                .projections(Arrays.asList("x", "name"))
                .conditions(BasicDBObject.parse("{ \"$or\" : [ { \"x\" : { \"$gte\" : 8}} , { \"name\" : { \"$eq\" : \"Bob\"}}]}"))
                .fields(new BasicDBObject())
                .skipRecords(0)
                .limitRecords(0)
                .build();

        List<Document> actualList = testObject.doQuery(query);
        assertEquals(6, actualList.size());
        for (int i = 0; i < actualList.size(); i++) {
            assertEquals(actualList.get(i), expectedList.get(i));
        }
    }

    @Test
    public void test_select_has_star_from_query_and_order_by_fields() {
        //equals SELECT * FROM test-mongo-client ORDER BY y ASK name DESC
        List<Document> expectedList = new ArrayList<Document>() {{
            add(Document.parse("{ \"_id\" : \"2\", \"x\" : 2 , \"y\": \"1\", \"name\": \"Bob\"}"));
            add(Document.parse("{ \"_id\" : \"11\", \"x\" : 11 ,\"y\": \"1\", \"name\": \"Bill\"}"));
            add(Document.parse("{ \"_id\" : \"1\", \"x\" : 1 , \"y\": \"1\", \"name\": \"Anton\"}"));
            add(Document.parse("{ \"_id\" : \"5\", \"x\" : 5 , \"y\": \"2\", \"name\": \"Eva\"}"));
            add(Document.parse("{ \"_id\" : \"3\", \"x\" : 3 , \"y\": \"2\", \"name\": \"Cen\"}"));
            add(Document.parse("{ \"_id\" : \"10\", \"x\" : 10 ,\"y\": \"3\", \"name\": \"Jon\"}"));
            add(Document.parse("{ \"_id\" : \"9\", \"x\" : 9 , \"y\": \"3\", \"name\": \"Ivan\"}"));
            add(Document.parse("{ \"_id\" : \"12\", \"x\" : 12 ,\"y\": \"3\", \"name\": \"Inga\"}"));
            add(Document.parse("{ \"_id\" : \"6\", \"x\" : 6 , \"y\": \"3\", \"name\": \"Fin\"}"));
            add(Document.parse("{ \"_id\" : \"4\", \"x\" : 4 , \"y\": \"3\", \"name\": \"Don\"}"));
            add(Document.parse("{ \"_id\" : \"7\", \"x\" : 7 , \"y\": \"4\", \"name\": \"Georg\"}"));
            add(Document.parse("{ \"_id\" : \"8\", \"x\" : 8 , \"y\": \"5\", \"name\": \"Hi\"}"));
        }};
        PreparedMongoQuery query = PreparedMongoQuery.builder()
                .target(testCollectionName)
                .projections(Collections.emptyList())
                .conditions(new BasicDBObject())
                .fields(BasicDBObject.parse("{y: 1, name: -1}"))
                .skipRecords(0)
                .limitRecords(0)
                .build();

        List<Document> actualList = testObject.doQuery(query);
        assertEquals(12, actualList.size());
        for (int i = 0; i < actualList.size(); i++) {
            assertEquals(actualList.get(i), expectedList.get(i));
        }
    }

    @Test
    public void test_select_has_star_from_query_and_skip() {
        //equals SELECT * FROM test-mongo-client SKIP 7
        List<Document> expectedList = new ArrayList<Document>() {{
            add(Document.parse("{ \"_id\" : \"8\", \"x\" : 8 , \"y\": \"5\", \"name\": \"Hi\"}"));
            add(Document.parse("{ \"_id\" : \"9\", \"x\" : 9 , \"y\": \"3\", \"name\": \"Ivan\"}"));
            add(Document.parse("{ \"_id\" : \"10\", \"x\" : 10 ,\"y\": \"3\", \"name\": \"Jon\"}"));
            add(Document.parse("{ \"_id\" : \"11\", \"x\" : 11 ,\"y\": \"1\", \"name\": \"Bill\"}"));
            add(Document.parse("{ \"_id\" : \"12\", \"x\" : 12 ,\"y\": \"3\", \"name\": \"Inga\"}"));
        }};
        PreparedMongoQuery query = PreparedMongoQuery.builder()
                .target(testCollectionName)
                .projections(Collections.emptyList())
                .conditions(new BasicDBObject())
                .fields(new BasicDBObject())
                .skipRecords(7)
                .limitRecords(0)
                .build();

        List<Document> actualList = testObject.doQuery(query);
        assertEquals(5, actualList.size());
        for (int i = 0; i < actualList.size(); i++) {
            assertEquals(actualList.get(i), expectedList.get(i));
        }
    }

    @Test
    public void test_select_has_star_from_query_and_skip_and_limit() {
        //equals SELECT * FROM test-mongo-client SKIP 7 LIMIT 3
        List<Document> expectedList = new ArrayList<Document>() {{
            add(Document.parse("{ \"_id\" : \"8\", \"x\" : 8 , \"y\": \"5\", \"name\": \"Hi\"}"));
            add(Document.parse("{ \"_id\" : \"9\", \"x\" : 9 , \"y\": \"3\", \"name\": \"Ivan\"}"));
            add(Document.parse("{ \"_id\" : \"10\", \"x\" : 10 ,\"y\": \"3\", \"name\": \"Jon\"}"));
        }};
        PreparedMongoQuery query = PreparedMongoQuery.builder()
                .target(testCollectionName)
                .projections(Collections.emptyList())
                .conditions(new BasicDBObject())
                .fields(new BasicDBObject())
                .skipRecords(7)
                .limitRecords(3)
                .build();

        List<Document> actualList = testObject.doQuery(query);
        assertEquals(3, actualList.size());
        for (int i = 0; i < actualList.size(); i++) {
            assertEquals(actualList.get(i), expectedList.get(i));
        }
    }

    @Test
    public void test_select_complex_query() {
        //equals SELECT * FROM test-mongo-client WHERE x <= 10 AND x <> 2 ORDER BY name DESC SKIP 2 LIMIT 5
        List<Document> expectedList = new ArrayList<Document>() {{
            add(Document.parse("{ \"_id\" : \"8\", \"x\" : 8 , \"y\": \"5\", \"name\": \"Hi\"}"));
            add(Document.parse("{ \"_id\" : \"7\", \"x\" : 7 , \"y\": \"4\", \"name\": \"Georg\"}"));
            add(Document.parse("{ \"_id\" : \"6\", \"x\" : 6 , \"y\": \"3\", \"name\": \"Fin\"}"));
            add(Document.parse("{ \"_id\" : \"5\", \"x\" : 5 , \"y\": \"2\", \"name\": \"Eva\"}"));
            add(Document.parse("{ \"_id\" : \"4\", \"x\" : 4 , \"y\": \"3\", \"name\": \"Don\"}"));
        }};
        PreparedMongoQuery query = PreparedMongoQuery.builder()
                .target(testCollectionName)
                .projections(Collections.emptyList())
                .conditions(BasicDBObject.parse("{ \"$and\" : [ { \"x\" : { \"$lte\" : 10}} , { \"x\" : { \"$ne\" : 2}}]}"))
                .fields(BasicDBObject.parse("{name: -1}"))
                .skipRecords(2)
                .limitRecords(5)
                .build();

        List<Document> actualList = testObject.doQuery(query);
        assertEquals(5, actualList.size());
        for (int i = 0; i < actualList.size(); i++) {
            assertEquals(actualList.get(i), expectedList.get(i));
        }
    }

    @After
    public void tearDown() throws Exception {
        testCollection.deleteMany(new BasicDBObject());
        testObject = null;
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        testCollection.drop();
        client.close();
        properties = null;
    }
}