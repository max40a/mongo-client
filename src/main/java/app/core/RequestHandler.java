package app.core;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.Document;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RequestHandler {

    private final static String AND = "$and";
    private final static String OR = "$or";
    private final static String DESC = "DESC";
    private final static String ASC = "ASC";

    private Parser parser;
    private MongoDatabase database;

    public RequestHandler(Parser parser, MongoDatabase database) {
        this.parser = parser;
        this.database = database;
    }

    public List<String> doQuery(String query) {
        Map<String, String> parsedSqlQuery = parser.parseSqlQuery(query);
        MongoCollection<Document> collection = database.getCollection(parsedSqlQuery.get(OptionName.FROM.getPropertyName()));
        FindIterable<Document> resultOfQuery = collection.find()
                .projection(Projections.include(getProjection(parsedSqlQuery.get(OptionName.SELECT.getPropertyName()))))
                .filter(getWhereConditions(parsedSqlQuery.get(OptionName.WHERE.getPropertyName())))
                .sort(getSortCondition(parsedSqlQuery.get(OptionName.ORDER_BY.getPropertyName())))
                .skip(getNumberOfInts(parsedSqlQuery.get(OptionName.SKIP.getPropertyName())))
                .limit(getNumberOfInts(parsedSqlQuery.get(OptionName.LIMIT.getPropertyName())));
        return StreamSupport.stream(resultOfQuery.spliterator(), false)
                .map(Document::entrySet)
                .map(Objects::toString)
                .collect(Collectors.toList());
    }

    private List<String> getProjection(String projection) {
        return (projection.contains("*")) ?
                Collections.emptyList() :
                Arrays.stream(projection.split(","))
                        .map(String::trim)
                        .collect(Collectors.toList());
    }

    private BasicDBObject getWhereConditions(String where) {
        BasicDBObject basicDBObject = new BasicDBObject();
        if (Objects.isNull(where)) {
            return new BasicDBObject();
        }
        if (where.contains(AND)) {
            BasicDBObject leftCondition = getWhereConditions(computeLeftCondition(where, AND));
            BasicDBObject rightCondition = getWhereConditions(computeRightCondition(where, AND));

            BasicDBList and = new BasicDBList();
            and.add(leftCondition);
            and.add(rightCondition);
            basicDBObject.put(AND, and);
        } else if (where.contains(OR)) {
            BasicDBObject leftCondition = getWhereConditions(computeLeftCondition(where, OR));
            BasicDBObject rightCondition = getWhereConditions(computeRightCondition(where, OR));

            BasicDBList or = new BasicDBList();
            or.add(leftCondition);
            or.add(rightCondition);
            basicDBObject.put(OR, or);
        } else {
            String[] split = where.split(" ");
            assert split.length == 3;
            if (split[1].equals("$eq")) {
                basicDBObject.put(split[0], split[2]);
                return basicDBObject;
            } else {
                basicDBObject.put(split[0], new BasicDBObject(split[1], split[2]));
            }
        }
        return basicDBObject;
    }

    private BasicDBObject getSortCondition(String orderBy) {
        if (Objects.isNull(orderBy)) {
            return new BasicDBObject();
        }
        BasicDBObject result = new BasicDBObject();
        String combineFields = computeLeftCondition(orderBy, (orderBy.contains(DESC)) ? DESC : ASC);
        String[] fields = combineFields.split(",");
        for (String field : fields) {
            result.append(field, orderBy.contains(DESC) ? -1 : 1);
        }
        return result;
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