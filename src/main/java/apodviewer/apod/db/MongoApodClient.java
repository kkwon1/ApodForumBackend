package apodviewer.apod.db;

import apodviewer.apod.model.NasaApod;
import com.google.common.cache.Cache;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class MongoApodClient implements ApodClient {

    private static final int APOD_COUNT = 30;

    @Autowired
    @Qualifier("apodCollection")
    private MongoCollection<Document> apodCollection;

    @Autowired
    private MongoApodConverter mongoApodConverter;

    @Autowired
    @Qualifier("apodListCache")
    private Cache<String, List<NasaApod>> apodListCache;

    @Autowired
    @Qualifier("apodPostCache")
    private Cache<String, NasaApod> apodPostCache;

    @Override
    public List<NasaApod> getLatestApods() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(APOD_COUNT - 1);
        if (apodListCache.getIfPresent(startDate.toString()) != null) {
            return apodListCache.getIfPresent(startDate.toString());
        } else {
            List<NasaApod> result = getApodFrom(startDate.toString());
            apodListCache.put(startDate.toString(), result);
            return result;
        }
    }

    @Override
    public NasaApod getApod(String date) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("date", date);
        MongoCursor<Document> cursor = apodCollection.find(searchQuery).cursor();

        if (cursor.hasNext()) {
            if (apodPostCache.getIfPresent(date) != null) {
                return apodPostCache.getIfPresent(date);
            } else {
                NasaApod apod = mongoApodConverter.convertDocumentToApod(cursor.next());
                apodPostCache.put(date, apod);
                return apod;
            }
        } else {
            return NasaApod.builder().build();
        }
    }

    @Override
    public List<NasaApod> searchApod(String searchString) {
        FindIterable<Document> iterable = apodCollection.find();
        MongoCursor<Document> cursor = iterable.cursor();

        List<NasaApod> results = new ArrayList<>();
        while (cursor.hasNext()) {
            NasaApod currentApod = mongoApodConverter.convertDocumentToApod(cursor.next());
            if (currentApod.getExplanation().contains(searchString)) {
                results.add(currentApod);
            }
        }

        return results;
    }

    @Override
    public List<NasaApod> getApodFrom(String startDate) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("date", new BasicDBObject()
                .append("$gte", startDate));

        MongoCursor<Document> cursor = apodCollection.find(searchQuery).cursor();

        return buildResults(cursor);
    }

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
    public List<NasaApod> getRandomApods(Integer count) {
        BasicDBObject randomSample = new BasicDBObject();
        randomSample.append("$sample", new BasicDBObject()
                .append("size", count));

        MongoCursor<Document> cursor = apodCollection.aggregate(List.of(randomSample)).cursor();
        return buildResults(cursor);
    }

    private NasaApod searchByDate(LocalDate date) {
        BasicDBObject searchQuery = new BasicDBObject();

        searchQuery.append("date", date.toString());

        MongoCursor<Document> cursor = apodCollection.find(searchQuery).cursor();

        if (cursor.hasNext()) {
            return mongoApodConverter.convertDocumentToApod(cursor.next());
        } else {
            LocalDate previousDay = date.minusDays(1);
            return searchByDate(previousDay);
        }
    }

    private List<NasaApod> buildResults(MongoCursor<Document> cursor) {
        List<NasaApod> results = new ArrayList<>();
        while (cursor.hasNext()) {
            results.add(mongoApodConverter.convertDocumentToApod(cursor.next()));
        }

        return results;
    }
}
