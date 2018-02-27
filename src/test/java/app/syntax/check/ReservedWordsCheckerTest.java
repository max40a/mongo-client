package app.syntax.check;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class ReservedWordsCheckerTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private SyntaxChecker testObject;

    @Before
    public void setUp() {
        testObject = new ReservedWordsChecker();
    }

    @Test()
    public void test_query_has_more_than_one_reserved_word() {
        String expectExceptionMessage = "Must be only one reserved words : [SELECT, FROM, WHERE, ORDER BY, SKIP, LIMIT] you have FROM : 2";
        exception.expect(SyntaxParseException.class);
        exception.expectMessage(expectExceptionMessage);
        String notSelectQuery = "SELECT * FROM FROM counter WHERE id > 2";
        testObject.validateSqlQuery(notSelectQuery);
    }

    @After
    public void tearDown() {
        testObject = null;
    }
}