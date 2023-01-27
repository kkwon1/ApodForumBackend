package apodviewer.apod;

import apodviewer.apod.client.ApodClient;
import apodviewer.apod.model.NasaApod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApodController {
    private static final String APOD_PATH = "/apod";

    @Autowired
    private ApodClient apodClient;

    @GetMapping(APOD_PATH)
    public NasaApod getRandomApod() {
        return apodClient.getRandomApod();
    }

    @GetMapping(value = APOD_PATH, params = {"offset", "limit"})
    public List<NasaApod> getApodPage(@RequestParam String offset, @RequestParam String limit) {return apodClient.getApodPage(offset, limit);}

    @GetMapping(value = APOD_PATH, params = {"date"})
    public NasaApod getApod(@RequestParam String date) {
        return apodClient.getApod(date);
    }

    @GetMapping(value = APOD_PATH, params = {"search"})
    public List<NasaApod> searchApod(@RequestParam String search) {
        return apodClient.searchApod(search);
    }

    @GetMapping(value = APOD_PATH, params = {"count"})
    public List<NasaApod> getRandomApods(@RequestParam Integer count) {
        return apodClient.getRandomApods(count);
    }
}
