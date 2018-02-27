package app.syntax.check;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SelectQueryCheckerTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private SyntaxChecker testObject;

    @Before
    public void setUp() {
        testObject = new SelectQueryChecker();
    }

    @Test
    public void test_not_select_query() {
        String expectExceptionMessage = "Support only SELECT query. Query must begin: SELECT <projections>";
        exception.expect(SyntaxParseException.class);
        exception.expectMessage(expectExceptionMessage);
        String notSelectQuery = "UPDATE test SET test.id = 3 counter WHERE test.id = 2";
        testObject.validateSqlQuery(notSelectQuery);
    }

    @Test()
    public void test_not_select_have_not_target_condition() {
        String expectExceptionMessage = "Query must be consist: FROM <target>";
        exception.expect(SyntaxParseException.class);
        exception.expectMessage(expectExceptionMessage);
        String notContainBlockFromQuery = "SELECT * WHERE id > 2";
        testObject.validateSqlQuery(notContainBlockFromQuery);
    }

    @After
    public void tearDown() throws Exception {
        testObject = null;
    }
}