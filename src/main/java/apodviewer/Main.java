package apodviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static apodviewer.EnvironmentVariables.*;

@SpringBootApplication
public class Main {

    private static final String COMMA_DELIMITER = ",";

    public static void main(String[] args) {
        initializeEnvironmentVariables();
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/apod").allowedOrigins(ALLOWED_ORIGINS.split(COMMA_DELIMITER));
                registry.addMapping("/post").allowedOrigins(ALLOWED_ORIGINS.split(COMMA_DELIMITER));
                registry.addMapping("/comment/add").allowedOrigins(ALLOWED_ORIGINS.split(COMMA_DELIMITER));
                registry.addMapping("/comment/delete").allowedOrigins(ALLOWED_ORIGINS.split(COMMA_DELIMITER));
            }
        };
    }
}
