package apodviewer.apod;

import apodviewer.apod.db.ApodClient;
import apodviewer.apod.model.NasaApod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApodController {
    private static final String APOD_PATH = "/apodviewer/apod";

    @Autowired
    private ApodClient apodClient;

    @GetMapping(APOD_PATH)
    public NasaApod getLatestApod() {
        return apodClient.getLatestApod();
    }

    @GetMapping(value = APOD_PATH, params = {"date"})
    public NasaApod getApod(@RequestParam String date) {
        return apodClient.getApod(date);
    }

    @GetMapping(value = APOD_PATH, params = {"search"})
    public List<NasaApod> searchApod(@RequestParam String search) {
        return apodClient.searchApod(search);
    }

    @GetMapping(value = APOD_PATH, params = {"start_date"})
    public List<NasaApod> getApodFrom(@RequestParam String start_date) {
        return apodClient.getApodFrom(start_date);
    }

    @GetMapping(value = APOD_PATH, params = {"start_date", "end_date"})
    public List<NasaApod> getApodFromTo(@RequestParam String start_date, @RequestParam String end_date) {
        return apodClient.getApodFromTo(start_date, end_date);
    }

    @GetMapping(value = APOD_PATH, params = {"count"})
    public List<NasaApod> getRandomApods(@RequestParam Integer count) {
        return apodClient.getRandomApods(count);
    }
}
