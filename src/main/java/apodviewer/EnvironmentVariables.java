package apodviewer;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvironmentVariables {
    public static String MONGO_ENDPOINT;
    // Comma separated string of origins
    public static String ALLOWED_ORIGINS;
    public static String NASA_API_KEY;
    public static String AUTH0_AUDIENCE;
    public static String JWT_ISSUER;

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

        AUTH0_AUDIENCE = System.getenv("AUTH0_AUDIENCE");
        if (AUTH0_AUDIENCE == null || AUTH0_AUDIENCE.isBlank()) {
            Dotenv dotenv = Dotenv.load();
            AUTH0_AUDIENCE = dotenv.get("AUTH0_AUDIENCE");
        }

        JWT_ISSUER = System.getenv("JWT_ISSUER");
        if (JWT_ISSUER == null || JWT_ISSUER.isBlank()) {
            Dotenv dotenv = Dotenv.load();
            JWT_ISSUER = dotenv.get("JWT_ISSUER");
        }
    }
}
