package apodviewer;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static apodviewer.EnvironmentVariables.MONGO_ENDPOINT;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
@ComponentScan
public class AppConfiguration {
    private static final String APOD_DB_NAME = "apodDB";
    private static final CodecRegistry POJO_CODEC_REGISTRY = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    @Bean
    public MongoClient getMongoClient() throws Exception {
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(POJO_CODEC_REGISTRY)
                .applyConnectionString(new ConnectionString(MONGO_ENDPOINT))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoDatabase getMongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase(APOD_DB_NAME);
    }
}
