package app.parser;

import app.syntax.check.SyntaxParseException;
import org.hamcrest.collection.IsMapContaining;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Parser testObj;

    @Before
    public void setUp() {
        testObj = new Parser(Collections.emptyList());
    }

    @Test
    public void test_simple_sql_query() {
        String testQuery = "SELECT * FROM someDb";
        Map<String, String> expectedData = new HashMap<String, String>() {{
            put("PROJECTIONS", "*");
            put("TARGET", "someDb");
        }};
        assertTrue(testObj.parseSqlQuery(testQuery).size() == 2);
        assertThat(expectedData, is(testObj.parseSqlQuery(testQuery)));
        expectedData.forEach((k, v) -> assertThat(testObj.parseSqlQuery(testQuery), IsMapContaining.hasEntry(k, v)));
    }

    @Test
    public void test_simple_sql_query_has_projections() {
        String testQuery = "SELECT a, b FROM someDb";
        Map<String, String> expectedData = new HashMap<String, String>() {{
            put("PROJECTIONS", "a, b");
            put("TARGET", "someDb");
        }};
        assertTrue(testObj.parseSqlQuery(testQuery).size() == 2);
        assertThat(expectedData, is(testObj.parseSqlQuery(testQuery)));
        expectedData.forEach((k, v) -> assertThat(testObj.parseSqlQuery(testQuery), IsMapContaining.hasEntry(k, v)));
    }

    @Test
    public void test_sql_query_has_composite_where_condition() {
        String testQuery = "SELECT a, b, c FROM someDb WHERE a > 2 AND b <> 5 OR c = test";
        Map<String, String> expectedData = new HashMap<String, String>() {{
            put("PROJECTIONS", "a, b, c");
            put("TARGET", "someDb");
            put("CONDITION", "a > 2 AND b <> 5 OR c = test");
        }};
        assertTrue(testObj.parseSqlQuery(testQuery).size() == 3);
        assertThat(expectedData, is(testObj.parseSqlQuery(testQuery)));
        expectedData.forEach((k, v) -> assertThat(testObj.parseSqlQuery(testQuery), IsMapContaining.hasEntry(k, v)));
    }

    @Test
    public void test_sql_query_has_composite_where_condition_and_order_by_conditions() {
        String testQuery = "SELECT a, b, c FROM someDb WHERE a > 2 AND b <> 5 OR c = test ORDER BY a, b DESC c ASC";
        Map<String, String> expectedData = new HashMap<String, String>() {{
            put("PROJECTIONS", "a, b, c");
            put("TARGET", "someDb");
            put("CONDITION", "a > 2 AND b <> 5 OR c = test");
            put("FIELDS", "a, b DESC c ASC");
        }};
        assertTrue(testObj.parseSqlQuery(testQuery).size() == 4);
        assertThat(expectedData, is(testObj.parseSqlQuery(testQuery)));
        expectedData.forEach((k, v) -> assertThat(testObj.parseSqlQuery(testQuery), IsMapContaining.hasEntry(k, v)));
    }

    @Test
    public void test_sql_query_not_has_where_condition_has_order_by_conditions() {
        String testQuery = "SELECT a, b, c FROM someDb ORDER BY a, b DESC c ASC";
        Map<String, String> expectedData = new HashMap<String, String>() {{
            put("PROJECTIONS", "a, b, c");
            put("TARGET", "someDb");
            put("FIELDS", "a, b DESC c ASC");
        }};
        assertTrue(testObj.parseSqlQuery(testQuery).size() == 3);
        assertThat(expectedData, is(testObj.parseSqlQuery(testQuery)));
        expectedData.forEach((k, v) -> assertThat(testObj.parseSqlQuery(testQuery), IsMapContaining.hasEntry(k, v)));
    }

    @Test
    public void test_sql_query_has_while_order_by_skip_conditions() {
        String testQuery = "SELECT a, b, c FROM someDb WHERE a > 2 AND b <> 5 OR c = test ORDER BY a, b DESC c ASC SKIP 10";
        Map<String, String> expectedData = new HashMap<String, String>() {{
            put("PROJECTIONS", "a, b, c");
            put("TARGET", "someDb");
            put("CONDITION", "a > 2 AND b <> 5 OR c = test");
            put("FIELDS", "a, b DESC c ASC");
            put("SKIP_RECORDS", "10");
        }};
        assertTrue(testObj.parseSqlQuery(testQuery).size() == 5);
        assertThat(expectedData, is(testObj.parseSqlQuery(testQuery)));
        expectedData.forEach((k, v) -> assertThat(testObj.parseSqlQuery(testQuery), IsMapContaining.hasEntry(k, v)));
    }

    @Test
    public void test_sql_query_has_while_order_by_skip_limit_conditions() {
        String testQuery = "SELECT a, b, c FROM someDb WHERE a > 2 AND b <> 5 OR c = test ORDER BY a, b DESC c ASC SKIP 10 LIMIT 5";
        Map<String, String> expectedData = new HashMap<String, String>() {{
            put("PROJECTIONS", "a, b, c");
            put("TARGET", "someDb");
            put("CONDITION", "a > 2 AND b <> 5 OR c = test");
            put("FIELDS", "a, b DESC c ASC");
            put("SKIP_RECORDS", "10");
            put("MAX_RECORDS", "5");
        }};
        assertTrue(testObj.parseSqlQuery(testQuery).size() == 6);
        assertThat(expectedData, is(testObj.parseSqlQuery(testQuery)));
        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            assertThat(testObj.parseSqlQuery(testQuery), IsMapContaining.hasEntry(entry.getKey(), entry.getValue()));
        }
    }

    @After
    public void tearDown() throws Exception {
        testObj = null;
    }
}