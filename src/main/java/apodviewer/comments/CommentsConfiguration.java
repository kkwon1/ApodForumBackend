package apodviewer.comments;

import apodviewer.comments.model.CommentTree;
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
public class CommentsConfiguration {
    private static final String COMMENTS_COLLECTION_NAME = "comments";

    @Bean
    @Qualifier("commentsCollection")
    public MongoCollection<Document> getCommentsCollection(MongoDatabase mongoDatabase) {
        return mongoDatabase.getCollection(COMMENTS_COLLECTION_NAME);
    }

    @Bean
    @Qualifier("commentsCache")
    public Cache<String, CommentTree> getCommentsCache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(3, TimeUnit.HOURS)
                .build();
    }
}
