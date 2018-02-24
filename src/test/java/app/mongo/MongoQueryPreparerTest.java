package app.mongo;

import app.parser.Parser;
import com.mongodb.BasicDBObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class MongoQueryPreparerTest {

    private MongoQueryPreparer testObject;

    @Before
    public void setUp() {
        testObject = new MongoQueryPreparer(new Parser(Collections.emptyList()));
    }

    @Test
    public void test_preparedMongoQuery_method() {
        String testQuery = "SELECT a, b, c FROM someDb WHERE a > 2 OR b <> 5 AND c = test ORDER_BY a, b DESC c ASC SKIP 10 LIMIT 5";
        String adaptedQuery = MongoCharacterAdapter.convertCharacters(testQuery);
        PreparedMongoQuery expected = PreparedMongoQuery.builder()
                .target("someDb")
                .projections(Arrays.asList("a", "b", "c"))
                .conditions(BasicDBObject.parse("{ $and : [ { $or : [ { a : { $gt : \"2\"}} , { b : { $ne : \"5\"}}]} , { c : \"test\"}]}"))
                .fields(BasicDBObject.parse("{a : -1, b : -1, c : 1}"))
                .skipRecords(10)
                .limitRecords(5)
                .build();

        PreparedMongoQuery actual = testObject.prepareMongoQuery(adaptedQuery);
        assertEquals(expected, actual);
    }

    @After
    public void tearDown() throws Exception {
        testObject = null;
    }
}