package apodviewer.apod.populator;

import apodviewer.apod.model.NasaApod;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ApodPopulatorController {
    @Autowired
    private ApodService apodService;

    // By default we will populate 1 year worth of data
    // Probably better to query 1 month at a time.
    @GetMapping("/populate")
    public void populateDatabase() throws IOException {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = client.getDatabase("apodDB");
        MongoCollection<Document> collection = db.getCollection("apod");

        List<NasaApod> apods = apodService.getApodFrom("2022-08-01");

        List<Document> docs = apods.stream().map(this::convertApodToDocument).collect(Collectors.toList());
        collection.insertMany(docs);
    }

    @GetMapping("/initialize")
    public void initializeDb() throws IOException {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = client.getDatabase("apodDB");
        MongoCollection<Document> collection = db.getCollection("apod");

        collection.createIndex(Indexes.ascending("date"));
    }

    private Document convertApodToDocument(NasaApod apod) {
        return new Document()
                .append("copyright", apod.getCopyright())
                .append("date", apod.getDate())
                .append("explanation", apod.getExplanation())
                .append("mediaType", apod.getMediaType())
                .append("serviceVersion", apod.getServiceVersion())
                .append("title", apod.getTitle())
                .append("url", apod.getUrl())
                .append("hdurl", apod.getHdurl());
    }
}
