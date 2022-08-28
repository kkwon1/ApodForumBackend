package apodviewer.apod;

import apodviewer.apod.model.NasaApod;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MongoApodClient implements ApodClient {
    private static final MongoClient MONGO_CLIENT = MongoClients.create("mongodb://localhost:27017");
    private static final MongoDatabase APOD_DATABASE = MONGO_CLIENT.getDatabase("apodDB");
    private static final MongoCollection<Document> APOD_COLLECTION = APOD_DATABASE.getCollection("apod");

    @Override
    public NasaApod getLatestApod() {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("date", "2022-08-14");
        MongoCursor<Document> cursor = APOD_COLLECTION.find(searchQuery).cursor();

        if (cursor.hasNext()) {
            return convertDocumentToApod(cursor.next());
        } else {
            return NasaApod.builder().build();
        }
    }

    @Override
    public NasaApod getApod(String date) {
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("date", date);
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
        return null;
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
