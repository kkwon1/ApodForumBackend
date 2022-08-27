package apodviewer;

import apodviewer.model.NasaApod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ApodController {
    @Autowired
    private ApodService apodService;

    @GetMapping("/apod")
    public NasaApod getApod() throws IOException {
        return apodService.getApod();
    }

    @GetMapping("/apod/random")
    public List<NasaApod> getRandomApods() throws IOException {
        return apodService.getRandomApods();
    }
}
