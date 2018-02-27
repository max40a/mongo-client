package app.syntax.check;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class NumberInputCheckerTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private SyntaxChecker testObject;

    @Before
    public void setUp() {
        testObject = new NumberInputChecker();
    }

    @Test()
    public void test_query_has_not_numeric_characters_in_skip_block() {
        String expectExceptionMessage = "Block <SKIP> or <LIMIT> should be contain only numeric data. Your input : \"abc\"";
        exception.expect(SyntaxParseException.class);
        exception.expectMessage(expectExceptionMessage);
        String testQuery = "SELECT * FROM counter WHERE id > 2 SKIP abc";
        testObject.validateSqlQuery(testQuery);
    }

    @Test()
    public void test_query_has_not_numeric_characters_in_limit_block() {
        String expectExceptionMessage = "Block <SKIP> or <LIMIT> should be contain only numeric data. Your input : \"test\"";
        exception.expect(SyntaxParseException.class);
        exception.expectMessage(expectExceptionMessage);
        String testQuery = "SELECT * FROM counter WHERE id > 2 LIMIT test";
        testObject.validateSqlQuery(testQuery);
    }

    @Test()
    public void test_query_has_not_numeric_characters_in_skip_and_limit_block() {
        String expectExceptionMessage = "Block <SKIP> or <LIMIT> should be contain only numeric data. Your input : \"abctest\"";
        exception.expect(SyntaxParseException.class);
        exception.expectMessage(expectExceptionMessage);
        String testQuery = "SELECT * FROM counter WHERE id > 2 SKIP abc LIMIT test";
        testObject.validateSqlQuery(testQuery);
    }

    @After
    public void tearDown() throws Exception {
        testObject = null;
    }
}