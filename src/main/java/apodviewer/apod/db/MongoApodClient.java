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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.mongodb.client.model.Aggregates.sample;

@Component
public class MongoApodClient implements ApodClient {

    private static final int APOD_COUNT = 30;

    @Autowired
    @Qualifier("apodCollection")
    private MongoCollection<Document> apodCollection;

    @Autowired
    @Qualifier("apodPostCache")
    private Cache<String, NasaApod> apodPostCache;

    @Autowired
    private MongoApodConverter mongoApodConverter;

    @Override
    public NasaApod getRandomApod() {
        NasaApod apod = apodCollection.aggregate(List.of(sample(1)))
                .map(document -> mongoApodConverter.convertDocumentToApod(document))
                .cursor()
                .next();

        apodPostCache.put(apod.getDate(), apod);
        return apod;
    }

    @Override
    public List<NasaApod> getApodPage(String offset, String limit) {
        int offsetVal = Integer.parseInt(offset);
        int limitVal = Integer.parseInt(limit);

        LocalDate today = LocalDate.now(ZoneId.of("America/Los_Angeles"));

        LocalDate endDate = today.minusDays(offsetVal);
        LocalDate startDate = endDate.minusDays(limitVal - 1);

        if (allApodInCache(startDate, endDate)) {
            return getAllApodFromCache(startDate, endDate);
        }

        return getApodFrom(startDate.toString());
    }

    @Override
    public NasaApod getApod(String date) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("date", date);
        MongoCursor<Document> cursor = apodCollection.find(searchQuery).cursor();

        NasaApod apod = apodPostCache.getIfPresent(date);
        if (apod == null) {
            if (cursor.hasNext()) {
                return mongoApodConverter.convertDocumentToApod(cursor.next());
            } else {
                return NasaApod.builder().build();
            }
        } else {
            return apod;
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
            results.add(apod);
        }

        return results;
    }

    private NasaApod getApodFromCache(String date) {
        try {
            NasaApod apod = apodPostCache.get(date, () -> getApod(date));
            apodPostCache.put(date, apod);
            return apod;
        } catch (ExecutionException e) {
            System.out.println("Failed to retrieve APOD for date " + date + " " + e);
        }

        return NasaApod.builder().build();
    }

    private boolean allApodInCache(LocalDate startDate, LocalDate endDate) {
        for(LocalDate i = startDate; !i.isAfter(endDate); i = i.plusDays(1)) {
            if (apodPostCache.getIfPresent(i.toString()) == null) return false;
        }

        return true;
    }

    private List<NasaApod> getAllApodFromCache(LocalDate startDate, LocalDate endDate) {
        List<NasaApod> results = new ArrayList<>();
        for(LocalDate i = startDate; !i.isAfter(endDate); i = i.plusDays(1)) {
            NasaApod apod = getApodFromCache(i.toString());
            results.add(apod);
        }

        return results;
    }
}
