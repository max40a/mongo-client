package app.mongo;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MongoRequestHandler {

    private MongoDatabase database;

    public MongoRequestHandler(MongoDatabase database) {
        this.database = database;
    }

    public List<Document> doQuery(PreparedMongoQuery preparedQuery) {
        MongoCollection<Document> collection = database.getCollection(preparedQuery.getTarget());
        FindIterable<Document> resultOfQuery = collection.find()
                .projection(Projections.include(preparedQuery.getProjections()))
                .filter(preparedQuery.getConditions())
                .sort(preparedQuery.getFields())
                .skip(preparedQuery.getSkipRecords())
                .limit(preparedQuery.getLimitRecords());
        return StreamSupport.stream(resultOfQuery.spliterator(), false)
                .collect(Collectors.toList());
    }
}