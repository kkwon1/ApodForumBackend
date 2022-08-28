package apodviewer.apod.db;

import apodviewer.apod.model.NasaApod;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

public class MongoConverterTest {
    private final MongoConverter mongoConverter = new MongoConverter();

    @Test
    public void testConvertApodToDocument() {
        NasaApod testNasaApod = NasaApod.builder()
                .copyright("testCopyright")
                .explanation("testExplanation")
                .serviceVersion("testServiceVersion")
                .date("testDate")
                .title("testTitle")
                .mediaType("testMediaType")
                .url("testUrl")
                .hdurl("testHdurl")
                .build();

        Document actualDocument = mongoConverter.convertApodToDocument(testNasaApod);

        Assert.assertEquals("testCopyright", actualDocument.get("copyright"));
        Assert.assertEquals("testExplanation", actualDocument.get("explanation"));
        Assert.assertEquals("testServiceVersion", actualDocument.get("serviceVersion"));
        Assert.assertEquals("testDate", actualDocument.get("date"));
        Assert.assertEquals("testTitle", actualDocument.get("title"));
        Assert.assertEquals("testMediaType", actualDocument.get("mediaType"));
        Assert.assertEquals("testUrl", actualDocument.get("url"));
        Assert.assertEquals("testHdurl", actualDocument.get("hdurl"));
    }
}
