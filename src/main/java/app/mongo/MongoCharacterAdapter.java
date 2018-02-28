package app.mongo;

import java.util.HashMap;
import java.util.Map;

public class MongoCharacterAdapter {

    private static Map<String, String> expressionsMap = new HashMap<String, String>() {{
        put(" = ", " $eq ");
        put(" > ", " $gt ");
        put(" < ", " $lt ");
        put(" >= ", " $gte ");
        put(" <= ", " $lte ");
        put(" <> ", " $ne ");
        put(" AND ", " $and ");
        put(" OR ", " $or ");
    }};

    private MongoCharacterAdapter() {
    }

    public static String convertCharacters(String arg) {
        for (String s : expressionsMap.keySet()) {
            if (arg.contains(s)) {
                arg = arg.replace(s, expressionsMap.get(s));
            }
        }
        return arg;
    }
}