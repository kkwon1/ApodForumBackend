package apodviewer.posts;

import apodviewer.posts.db.MongoPostUpvoteConverter;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class PostConfiguration {
    private static final String POST_UPVOTE_COLLECTION_NAME = "postUpvote";

    @Bean
    @Qualifier("postUpvoteCollection")
    public MongoCollection<Document> getPostUpvoteCollection(MongoDatabase mongoDatabase) {
        return mongoDatabase.getCollection(POST_UPVOTE_COLLECTION_NAME);
    }

    @Bean
    public MongoPostUpvoteConverter getMongoPostUpvoteConverter() {
        return new MongoPostUpvoteConverter();
    }
}
