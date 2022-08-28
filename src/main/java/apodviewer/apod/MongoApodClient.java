package apodviewer.apod;

import apodviewer.apod.model.NasaApod;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class MongoApodClient implements ApodClient {

    @Autowired
    private MongoCollection<Document> apodCollection;

    @Override
    public NasaApod getLatestApod() {
        return searchByDate(LocalDate.now());
    }

    @Override
    public NasaApod getApod(String date) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.append("date", date);
        MongoCursor<Document> cursor = apodCollection.find(searchQuery).cursor();

        if (cursor.hasNext()) {
            return convertDocumentToApod(cursor.next());
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

        MongoCursor<Document> cursor = apodCollection.find(searchQuery).cursor();

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

        MongoCursor<Document> cursor = apodCollection.find(searchQuery).cursor();

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

        MongoCursor<Document> cursor = apodCollection.aggregate(List.of(randomSample)).cursor();

        List<NasaApod> results = new ArrayList<>();
        while (cursor.hasNext()) {
            results.add(convertDocumentToApod(cursor.next()));
        }

        return results;
    }

    private NasaApod searchByDate(LocalDate date) {
        BasicDBObject searchQuery = new BasicDBObject();

        searchQuery.append("date", date.toString());

        MongoCursor<Document> cursor = apodCollection.find(searchQuery).cursor();

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
