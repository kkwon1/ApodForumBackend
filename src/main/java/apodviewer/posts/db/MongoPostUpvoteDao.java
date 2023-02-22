package apodviewer.posts.db;

import apodviewer.apod.model.NasaApod;
import apodviewer.posts.model.Upvote;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

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

    @Override
    public List<String> getUpvotedPostIds(String userSub) {
        Bson query = eq("userSub", userSub);

        MongoCursor<Document> cursor = postUpvoteCollection.find(query).cursor();
        List<Upvote> upvotes = new ArrayList<>();
        while (cursor.hasNext()) {
            upvotes.add(mongoPostUpvoteConverter.convertDocumentToUpvote(cursor.next()));
        }

        return upvotes.stream().map(Upvote::getPostId).collect(Collectors.toList());
    }
}
