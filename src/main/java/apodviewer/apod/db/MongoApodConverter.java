package apodviewer.apod.db;

import apodviewer.apod.model.NasaApod;
import apodviewer.comments.util.CommentHelper;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

public class MongoApodConverter {

    @Autowired
    private CommentHelper commentHelper;

    public Document convertApodToDocument(NasaApod apod) {
        return new Document()
                .append("copyright", apod.getCopyright())
                .append("date", apod.getDate())
                .append("explanation", apod.getDescription())
                .append("mediaType", apod.getMediaType())
                .append("serviceVersion", apod.getServiceVersion())
                .append("title", apod.getTitle())
                .append("url", apod.getUrl())
                .append("hdurl", apod.getHdurl());
    }

    public NasaApod convertDocumentToApod(Document document) {
        String postId = document.getString("date");
        int numComments = commentHelper.getCommentCount(postId);
        return NasaApod.builder()
                .copyright(document.getString("copyright"))
                .date(document.getString("date"))
                .description(document.getString("explanation"))
                .mediaType(document.getString("mediaType"))
                .serviceVersion(document.getString("serviceVersion"))
                .title(document.getString("title"))
                .url(document.getString("url"))
                .hdurl(document.getString("hdurl"))
                .commentCount(numComments)
                .build();
    }
}
