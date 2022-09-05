package apodviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static apodviewer.EnvironmentVariables.ALLOWED_ORIGIN;
import static apodviewer.EnvironmentVariables.initializeEnvironmentVariables;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        initializeEnvironmentVariables();
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/apod").allowedOrigins(ALLOWED_ORIGIN);
                registry.addMapping("/post").allowedOrigins(ALLOWED_ORIGIN);
                registry.addMapping("/comments").allowedOrigins(ALLOWED_ORIGIN);
            }
        };
    }
}
