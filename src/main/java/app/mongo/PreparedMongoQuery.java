package app.mongo;

import com.mongodb.BasicDBObject;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PreparedMongoQuery {
    private String target;
    private List<String> projections;
    private BasicDBObject conditions;
    private BasicDBObject fields;
    private int skipRecords;
    private int limitRecords;
}