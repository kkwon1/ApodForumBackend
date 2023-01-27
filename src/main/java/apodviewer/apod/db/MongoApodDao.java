package apodviewer.apod.db;

import apodviewer.apod.model.NasaApod;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Aggregates.sample;
import static com.mongodb.client.model.Filters.eq;

@Component
public class MongoApodDao implements ApodDao {

    @Autowired
    @Qualifier("apodCollection")
    private MongoCollection<Document> apodCollection;

    @Autowired
    private MongoApodConverter mongoApodConverter;

    @Override
    public NasaApod getApod(String postId) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("date", postId);
        MongoCursor<Document> cursor = apodCollection.find(searchQuery).cursor();

        if (cursor.hasNext()) {
            return mongoApodConverter.convertDocumentToApod(cursor.next());
        } else {
            return NasaApod.builder().build();
        }
    }

    @Override
    public List<NasaApod> getAllApod() {
        FindIterable<Document> iterable = apodCollection.find();
        MongoCursor<Document> cursor = iterable.cursor();

        List<NasaApod> results = new ArrayList<>();
        while (cursor.hasNext()) {
            NasaApod currentApod = mongoApodConverter.convertDocumentToApod(cursor.next());
            results.add(currentApod);
        }

        return results;
    }

    /**
     * Return all APOD entries for a given range of date inclusive
     *
     * @param startDate - StartDate represented in String format yyyy-mm-dd
     * @param endDate - EndDate represented in String format yyyy-mm-dd
     * @return
     */
    @Override
    public List<NasaApod> getApodFromTo(String startDate, String endDate) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("date", new BasicDBObject()
                .append("$gte", startDate)
                .append("$lte", endDate));

        MongoCursor<Document> cursor = apodCollection.find(searchQuery).cursor();

        return buildResults(cursor);
    }

    @Override
    public NasaApod getRandomApod() {
        NasaApod apod = apodCollection.aggregate(List.of(sample(1)))
                .map(document -> mongoApodConverter.convertDocumentToApod(document))
                .cursor()
                .next();

        return apod;
    }

    @Override
    public List<NasaApod> getRandomApods(Integer count) {
        BasicDBObject randomSample = new BasicDBObject();
        randomSample.append("$sample", new BasicDBObject()
                .append("size", count));

        MongoCursor<Document> cursor = apodCollection.aggregate(List.of(randomSample)).cursor();
        return buildResults(cursor);
    }

    @Override
    public void incrementCommentCount(String postId) {
        // Update comment count for the APOD
        Bson query = eq("date", postId);
        Bson updates = Updates.inc("commentCount", 1);
        UpdateOptions options = new UpdateOptions().upsert(true);

        apodCollection.updateOne(query, updates, options);
    }

    private List<NasaApod> buildResults(MongoCursor<Document> cursor) {
        List<NasaApod> results = new ArrayList<>();
        while (cursor.hasNext()) {
            NasaApod apod = mongoApodConverter.convertDocumentToApod(cursor.next());
            results.add(apod);
        }

        return results;
    }
}
