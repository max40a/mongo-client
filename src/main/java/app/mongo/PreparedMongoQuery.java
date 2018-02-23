package app.mongo;

import com.mongodb.BasicDBObject;
import lombok.*;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class PreparedMongoQuery {
    private String target;
    private List<String> projections;
    private BasicDBObject conditions;
    private BasicDBObject fields;
    private int skipRecords;
    private int limitRecords;
}