package apodviewer.apod;

import apodviewer.apod.model.NasaApod;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class MongoApodClient implements ApodClient {
    private static final MongoClient MONGO_CLIENT = MongoClients.create("mongodb://localhost:27017");
    private static final MongoDatabase APOD_DATABASE = MONGO_CLIENT.getDatabase("apodDB");
    private static final MongoCollection<Document> APOD_COLLECTION = APOD_DATABASE.getCollection("apod");

    @Override
    public NasaApod getLatestApod() {
        return searchByDate(LocalDate.now());
    }

    @Override
    public NasaApod getApod(String date) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("date", date);
        MongoCursor<Document> cursor = APOD_COLLECTION.find(searchQuery).cursor();

        if (cursor.hasNext()) {
            return convertDocumentToApod(cursor.next());
        } else {
            return NasaApod.builder().build();
        }
    }

    @Override
    public List<NasaApod> searchApod(String searchString) {
        FindIterable<Document> iterable = APOD_COLLECTION.find();
        MongoCursor<Document> cursor = iterable.cursor();

        List<NasaApod> results = new ArrayList<>();
        while (cursor.hasNext()) {
            NasaApod currentApod = convertDocumentToApod(cursor.next());
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

        MongoCursor<Document> cursor = APOD_COLLECTION.find(searchQuery).cursor();

        List<NasaApod> results = new ArrayList<>();
        while (cursor.hasNext()) {
            results.add(convertDocumentToApod(cursor.next()));
        }

        return results;
    }

    @Override
    public List<NasaApod> getApodFromTo(String startDate, String endDate) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("date", new BasicDBObject()
                .append("$gte", startDate)
                .append("$lte", endDate));

        MongoCursor<Document> cursor = APOD_COLLECTION.find(searchQuery).cursor();

        List<NasaApod> results = new ArrayList<>();
        while (cursor.hasNext()) {
            results.add(convertDocumentToApod(cursor.next()));
        }

        return results;
    }

    @Override
    public List<NasaApod> getRandomApods(Integer count) {
        BasicDBObject randomSample = new BasicDBObject();
        randomSample.append("$sample", new BasicDBObject()
                .append("size", count));

        MongoCursor<Document> cursor = APOD_COLLECTION.aggregate(List.of(randomSample)).cursor();

        List<NasaApod> results = new ArrayList<>();
        while (cursor.hasNext()) {
            results.add(convertDocumentToApod(cursor.next()));
        }

        return results;
    }

    private NasaApod searchByDate(LocalDate date) {
        BasicDBObject searchQuery = new BasicDBObject();

        searchQuery.append("date", date.toString());

        MongoCursor<Document> cursor = APOD_COLLECTION.find(searchQuery).cursor();

        if (cursor.hasNext()) {
            return convertDocumentToApod(cursor.next());
        } else {
            LocalDate previousDay = date.minusDays(1);
            return searchByDate(previousDay);
        }
    }

    private NasaApod convertDocumentToApod(Document document) {
        return NasaApod.builder()
                .copyright(document.getString("copyright"))
                .date(document.getString("date"))
                .explanation(document.getString("explanation"))
                .mediaType(document.getString("mediaType"))
                .serviceVersion(document.getString("serviceVersion"))
                .title(document.getString("title"))
                .url(document.getString("url"))
                .hdurl(document.getString("hdurl"))
                .build();
    }
}
