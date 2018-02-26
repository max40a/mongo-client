package app.mongo;

import app.parser.SqlReservedWord;
import app.parser.Parser;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import java.util.*;
import java.util.stream.Collectors;

public class MongoQueryPreparer {

    private final static String AND = "$and";
    private final static String OR = "$or";
    private final static String DESC = "DESC";
    private final static String ASC = "ASC";

    private Parser parser;

    public MongoQueryPreparer(Parser parser) {
        this.parser = parser;
    }

    public PreparedMongoQuery prepareMongoQuery(String query) {
        Map<String, String> parsedSqlQueryMap = parser.parseSqlQuery(query);
        return PreparedMongoQuery.builder()
                .target(parsedSqlQueryMap.get(SqlReservedWord.FROM.getAdjective()))
                .projections(getProjection(parsedSqlQueryMap.get(SqlReservedWord.SELECT.getAdjective())))
                .conditions(getWhereConditions(parsedSqlQueryMap.get(SqlReservedWord.WHERE.getAdjective())))
                .fields(getSortCondition(parsedSqlQueryMap.get(SqlReservedWord.ORDER.getAdjective())))
                .skipRecords(getInt(parsedSqlQueryMap.get(SqlReservedWord.SKIP.getAdjective())))
                .limitRecords(getInt(parsedSqlQueryMap.get(SqlReservedWord.LIMIT.getAdjective())))
                .build();
    }

    private List<String> getProjection(String projection) {
        return (projection.contains("*")) ?
                Collections.emptyList() :
                Arrays.stream(projection.split(","))
                        .map(String::trim)
                        .collect(Collectors.toList());
    }

    private BasicDBObject getWhereConditions(String where) {
        BasicDBObject whereObject = new BasicDBObject();
        if (Objects.isNull(where)) {
            return new BasicDBObject();
        }
        if (where.contains(AND)) {
            BasicDBObject leftCondition = getWhereConditions(computeLeftCondition(where, AND));
            BasicDBObject rightCondition = getWhereConditions(computeRightCondition(where, AND));

            BasicDBList and = new BasicDBList();
            and.add(leftCondition);
            and.add(rightCondition);
            whereObject.put(AND, and);
        } else if (where.contains(OR)) {
            BasicDBObject leftCondition = getWhereConditions(computeLeftCondition(where, OR));
            BasicDBObject rightCondition = getWhereConditions(computeRightCondition(where, OR));

            BasicDBList or = new BasicDBList();
            or.add(leftCondition);
            or.add(rightCondition);
            whereObject.put(OR, or);
        } else {
            String[] mongoExpressions = {"$eq", "$gt", "$lt", "$gte", "$lte", "$ne", "$and", "$or"};
            String mongoExp = "";
            for (String exp : mongoExpressions) {
                mongoExp = where.contains(exp) ? exp : mongoExp;
            }
            String left = computeLeftCondition(where, mongoExp);
            String right = computeRightCondition(where, mongoExp);
            if (mongoExp.equals("$eq")) {
                whereObject.put(left, right);
                return whereObject;
            } else {
                whereObject.put(left, new BasicDBObject(mongoExp, right));
            }
        }
        return whereObject;
    }

    private String computeLeftCondition(String str, String delimiter) {
        return str.substring(0, str.indexOf(delimiter)).trim();
    }

    private String computeRightCondition(String str, String delimiter) {
        return str.substring(str.indexOf(delimiter) + delimiter.length()).trim();
    }

    private BasicDBObject getSortCondition(String orderBy) {
        BasicDBObject orderByObject = new BasicDBObject();
        if (Objects.isNull(orderBy)) {
            return orderByObject;
        }
        getSortConditionSupport(orderBy).forEach(orderByObject::append);
        return orderByObject;
    }

    private Map<String, Integer> getSortConditionSupport(String orderBy) {
        Map<String, Integer> orderByMap = new HashMap<>();
        orderBy = orderBy.replace(DESC, "-1").replace(ASC, "1");
        String[] split = orderBy.split("1");
        for (String str : split) {
            str = (str.startsWith(",")) ? str.substring(1, str.length()) : str;
            if (str.contains("-")) {
                str = str.replace("-", "").replace(" ", "");
                Arrays.stream(str.split(",")).forEach(s -> orderByMap.put(s.trim(), -1));
            } else {
                str = str.replace(" ", "");
                Arrays.stream(str.split(",")).forEach(s -> orderByMap.put(s.trim(), 1));
            }
        }
        return orderByMap;
    }

    private int getInt(String intNotation) {
        return (Objects.isNull(intNotation)) ? 0 : Integer.parseInt(intNotation);
    }
}