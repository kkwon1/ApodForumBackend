package apodviewer.apod;

import apodviewer.apod.populator.ApodService;
import apodviewer.apod.model.NasaApod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ApodController {
    @Autowired
    private ApodClient apodClient;

    @GetMapping("/apod")
    public NasaApod getLatestApod() {
        return apodClient.getLatestApod();
    }

    @GetMapping(value = "/apod", params = {"date"})
    public NasaApod getApod(@RequestParam String date) {
        return apodClient.getApod(date);
    }

    @GetMapping(value = "/apod", params = {"search"})
    public List<NasaApod> searchApod(@RequestParam String search) {
        return apodClient.searchApod(search);
    }
}
