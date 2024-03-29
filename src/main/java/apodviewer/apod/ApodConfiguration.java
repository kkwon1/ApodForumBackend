package apodviewer.apod;

import apodviewer.apod.db.MongoApodConverter;
import apodviewer.apod.model.NasaApod;
import apodviewer.comments.db.MongoCommentNodeConverter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan
public class ApodConfiguration {
    private static final String APOD_COLLECTION_NAME = "apod";

    @Bean
    @Qualifier("apodCollection")
    public MongoCollection<Document> getApodCollection(MongoDatabase mongoDatabase) {
        return mongoDatabase.getCollection(APOD_COLLECTION_NAME);
    }

    @Bean
    public MongoApodConverter getMongoConverter() {
        return new MongoApodConverter();
    }

    @Bean
    public MongoCommentNodeConverter getMongoCommentNodeConverter() {
        return new MongoCommentNodeConverter();
    }

    @Bean
    @Qualifier("apodPostCache")
    public Cache<String, NasaApod> getApodPostCache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(3, TimeUnit.HOURS)
                .build();
    }
}
