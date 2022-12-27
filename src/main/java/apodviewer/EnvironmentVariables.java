package apodviewer;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvironmentVariables {
    public static String MONGO_ENDPOINT;
    public static String ALLOWED_ORIGIN;
    public static String NASA_API_KEY;

    public static void initializeEnvironmentVariables() {
        MONGO_ENDPOINT = System.getenv("MONGO_ENDPOINT");
        if (MONGO_ENDPOINT == null || MONGO_ENDPOINT.isBlank()) {
            Dotenv dotenv = Dotenv.load();
            MONGO_ENDPOINT = dotenv.get("MONGO_ENDPOINT");
        }

        ALLOWED_ORIGIN = System.getenv("ALLOWED_ORIGIN");
        if (ALLOWED_ORIGIN == null || ALLOWED_ORIGIN.isBlank()) {
            Dotenv dotenv = Dotenv.load();
            ALLOWED_ORIGIN = dotenv.get("ALLOWED_ORIGIN");
        }

        NASA_API_KEY = System.getenv("NASA_API_KEY");
        if (NASA_API_KEY == null || NASA_API_KEY.isBlank()) {
            Dotenv dotenv = Dotenv.load();
            NASA_API_KEY = dotenv.get("NASA_API_KEY");
        }
    }
}
