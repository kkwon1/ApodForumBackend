package apodviewer.posts.db;

import apodviewer.posts.model.Upvote;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MongoPostUpvoteDao implements PostUpvoteDao {

    @Autowired
    @Qualifier("postUpvoteCollection")
    private MongoCollection<Document> postUpvoteCollection;

    @Autowired
    private MongoPostUpvoteConverter mongoPostUpvoteConverter;

    @Override
    public void upvotePost(Upvote upvote) {
        postUpvoteCollection.insertOne(mongoPostUpvoteConverter.convertUpvoteToDocument(upvote));
    }
}
