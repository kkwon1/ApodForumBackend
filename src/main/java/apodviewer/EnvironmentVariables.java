package apodviewer;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvironmentVariables {
    public static String MONGO_ENDPOINT;
    // Comma separated string of origins
    public static String ALLOWED_ORIGINS;
    public static String NASA_API_KEY;

    public static void initializeEnvironmentVariables() {
        MONGO_ENDPOINT = System.getenv("MONGO_ENDPOINT");
        if (MONGO_ENDPOINT == null || MONGO_ENDPOINT.isBlank()) {
            Dotenv dotenv = Dotenv.load();
            MONGO_ENDPOINT = dotenv.get("MONGO_ENDPOINT");
        }

        ALLOWED_ORIGINS = System.getenv("ALLOWED_ORIGINS");
        if (ALLOWED_ORIGINS == null || ALLOWED_ORIGINS.isBlank()) {
            Dotenv dotenv = Dotenv.load();
            ALLOWED_ORIGINS = dotenv.get("ALLOWED_ORIGINS");
        }

        NASA_API_KEY = System.getenv("NASA_API_KEY");
        if (NASA_API_KEY == null || NASA_API_KEY.isBlank()) {
            Dotenv dotenv = Dotenv.load();
            NASA_API_KEY = dotenv.get("NASA_API_KEY");
        }
    }
}
