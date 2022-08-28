package apodviewer.apod.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class MongoConfiguration {
    private static final Dotenv DOTENV = Dotenv.load();
    private static final String MONGO_ENDPOINT = DOTENV.get("MONGO_ENDPOINT");

    private static final String APOD_DB_NAME = "apodDB";
    private static final String APOD_COLLECTION_NAME = "apod";

    @Bean
    public MongoClient getMongoClient() {
        return MongoClients.create(MONGO_ENDPOINT);
    }

    @Bean
    public MongoDatabase getMongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase(APOD_DB_NAME);
    }

    @Bean
    public MongoCollection<Document> getMongoCollection(MongoDatabase mongoDatabase) {
        return mongoDatabase.getCollection(APOD_COLLECTION_NAME);
    }

    @Bean
    public MongoConverter getMongoConverter() {
        return new MongoConverter();
    }
}
