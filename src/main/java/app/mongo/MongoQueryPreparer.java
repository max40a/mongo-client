package app.mongo;

import app.parser.SqlOptionName;
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

    public PreparedMongoQuery preparedMongoQuery(String query) {
        Map<String, String> parsedSqlQueryMap = parser.parseSqlQuery(query);
        return PreparedMongoQuery.builder()
                .target(parsedSqlQueryMap.get(SqlOptionName.FROM.getPropertyName()))
                .projections(getProjection(parsedSqlQueryMap.get(SqlOptionName.SELECT.getPropertyName())))
                .conditions(getWhereConditions(parsedSqlQueryMap.get(SqlOptionName.WHERE.getPropertyName())))
                .fields(getSortCondition(parsedSqlQueryMap.get(SqlOptionName.ORDER_BY.getPropertyName())))
                .skipRecords(getNumberOfInts(parsedSqlQueryMap.get(SqlOptionName.SKIP.getPropertyName())))
                .limitRecords(getNumberOfInts(parsedSqlQueryMap.get(SqlOptionName.LIMIT.getPropertyName())))
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
            String[] split = where.split(" ");
            assert split.length == 3;
            if (split[1].equals("$eq")) {
                whereObject.put(split[0], split[2]);
                return whereObject;
            } else {
                whereObject.put(split[0], new BasicDBObject(split[1], split[2]));
            }
        }
        return whereObject;
    }

    private BasicDBObject getSortCondition(String orderBy) {
        if (Objects.isNull(orderBy)) {
            return new BasicDBObject();
        }
        BasicDBObject orderByObject = new BasicDBObject();
        String combineFields = computeLeftCondition(orderBy, (orderBy.contains(DESC)) ? DESC : ASC);
        String[] fields = combineFields.split(",");
        for (String field : fields) {
            orderByObject.append(field, orderBy.contains(DESC) ? -1 : 1);
        }
        return orderByObject;
    }

    private String computeLeftCondition(String str, String delimiter) {
        return str.substring(0, str.indexOf(delimiter)).trim();
    }

    private String computeRightCondition(String str, String delimiter) {
        return str.substring(str.indexOf(delimiter) + delimiter.length()).trim();
    }

    private int getNumberOfInts(String intNotation) {
        return (Objects.isNull(intNotation)) ? 0 : Integer.parseInt(intNotation);
    }
}