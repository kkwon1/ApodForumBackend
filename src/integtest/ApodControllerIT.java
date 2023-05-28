import apodviewer.Main;
import apodviewer.apod.model.NasaApod;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static apodviewer.EnvironmentVariables.initializeEnvironmentVariables;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApodControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public static void setup() {
        initializeEnvironmentVariables();
    }

    @Test
    public void testGetApod() {
        ResponseEntity<NasaApod> responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/apod?date=2023-04-01", NasaApod.class);

        NasaApod expectedNasaApod = NasaApod.builder()
                .date("2023-04-01")
                .copyright("Nicolas Rolland")
                .url("https://apod.nasa.gov/apod/image/2304/NGC2442-NicolasROLLAND_signatur1024.jpg")
                .explanation("Distorted galaxy NGC 2442 can be found in the southern constellation of the flying fish, (Piscis) Volans. Located about 50 million light-years away, the galaxy's two spiral arms extending from a pronounced central bar give it a hook-shaped appearance in this deep colorful image, with spiky foreground stars scattered across the telescopic field of view. The image also reveals the distant galaxy's obscuring dust lanes, young blue star clusters and reddish star forming regions surrounding a core of yellowish light from an older population of stars. But the star forming regions seem more concentrated along the drawn-out (upper right) spiral arm. The distorted structure is likely the result of an ancient close encounter with the smaller galaxy seen near the top left of the frame. The two interacting galaxies are separated by about 150,000 light-years at the estimated distance of NGC 2442.")
                .hdurl("https://apod.nasa.gov/apod/image/2304/NGC2442-NicolasROLLAND_signatur.jpg")
                .title("NGC 2442: Galaxy in Volans")
                .build();

        NasaApod actualApod = responseEntity.getBody();
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertThat(actualApod)
                .usingRecursiveComparison()
                .isEqualTo(expectedNasaApod);
    }
}