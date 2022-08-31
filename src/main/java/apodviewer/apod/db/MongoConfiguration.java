package apodviewer.apod.db;

import apodviewer.comments.db.MongoCommentNodeConverter;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private static final String COMMENTS_COLLECTION_NAME = "comments";

    @Bean
    public MongoClient getMongoClient() {
        return MongoClients.create(MONGO_ENDPOINT);
    }

    @Bean
    public MongoDatabase getMongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase(APOD_DB_NAME);
    }

    @Bean
    @Qualifier("apodCollection")
    public MongoCollection<Document> getApodCollection(MongoDatabase mongoDatabase) {
        return mongoDatabase.getCollection(APOD_COLLECTION_NAME);
    }

    @Bean
    @Qualifier("commentsCollection")
    public MongoCollection<Document> getCommentsCollection(MongoDatabase mongoDatabase) {
        return mongoDatabase.getCollection(COMMENTS_COLLECTION_NAME);
    }

    @Bean
    public MongoApodConverter getMongoConverter() {
        return new MongoApodConverter();
    }

    @Bean
    public MongoCommentNodeConverter getMongoCommentNodeConverter() {
        return new MongoCommentNodeConverter();
    }
}
