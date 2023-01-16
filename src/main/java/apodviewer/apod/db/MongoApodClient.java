package apodviewer.apod.db;

import apodviewer.apod.cache.ApodCacheWrapper;
import apodviewer.apod.model.NasaApod;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Aggregates.sample;

@Component
public class MongoApodClient implements ApodClient {

    private static final int APOD_COUNT = 30;

    @Autowired
    @Qualifier("apodCollection")
    private MongoCollection<Document> apodCollection;

    @Autowired
    private MongoApodConverter mongoApodConverter;

    @Autowired
    private ApodCacheWrapper apodCacheWrapper;

    @Override
    public NasaApod getRandomApod() {
        return apodCollection.aggregate(List.of(sample(1)))
                .map(document -> mongoApodConverter.convertDocumentToApod(document))
                .cursor()
                .next();
    }

    @Override
    public List<NasaApod> getApodPage(String offset, String limit) {
        int offsetVal = Integer.parseInt(offset);
        int limitVal = Integer.parseInt(limit);

        LocalDate today = LocalDate.now();

        LocalDate endDate = today.minusDays(offsetVal);
        LocalDate startDate = endDate.minusDays(limitVal - 1);

        if (apodCacheWrapper.containsApodList(startDate, endDate)) {
            System.out.println("Cache hit from " + startDate + " to " + endDate);
            return apodCacheWrapper.getApodList(startDate, endDate);
        } else {
            System.out.println("Cache missed from " + startDate + " to " + endDate);
            return getApodFromTo(startDate.toString(), endDate.toString());
        }
    }

    @Override
    public NasaApod getApod(String date) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("date", date);
        MongoCursor<Document> cursor = apodCollection.find(searchQuery).cursor();

        if (apodCacheWrapper.containsApod(date)) {
            return apodCacheWrapper.getApod(date);
        } else {
            if (cursor.hasNext()) {
                return mongoApodConverter.convertDocumentToApod(cursor.next());
            } else {
                return NasaApod.builder().build();
            }
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
            NasaApod apod = mongoApodConverter.convertDocumentToApod(cursor.next());
            apodCacheWrapper.addToCache(apod);
            System.out.println("Added APOD date " + apod.getDate() + " to Cache");
            results.add(apod);
        }

        return results;
    }
}
