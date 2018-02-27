package app.mongo;

import org.junit.Test;

import static org.junit.Assert.*;

public class MongoCharacterAdapterTest {

    @Test
    public void test_convertCharacters_method() {
        String testQuery = "SELECT a, b, c FROM someDb WHERE a > 2 OR b <> 5 AND c = test ORDER BY a, b DESC c ASC SKIP 10 LIMIT 5";
        String expected = "SELECT a, b, c FROM someDb WHERE a $gt 2 $or b $ne 5 $and c $eq test ORDER BY a, b DESC c ASC SKIP 10 LIMIT 5";
        assertEquals(expected, MongoCharacterAdapter.convertCharacters(testQuery));
    }

    @Test
    public void test_all_characters_convert() {
        String testCharacters = " = > < >= <= <> AND OR ";
        String expected = " $eq $gt $lt $gte $lte $ne $and $or ";
        assertEquals(expected, MongoCharacterAdapter.convertCharacters(testCharacters));
    }
}