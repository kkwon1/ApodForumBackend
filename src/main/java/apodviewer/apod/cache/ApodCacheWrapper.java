package apodviewer.apod.cache;

import apodviewer.apod.model.NasaApod;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ApodCacheWrapper {

    @Autowired
    @Qualifier("apodPostCache")
    private Cache<String, NasaApod> apodPostCache;

    public boolean containsApod(String date) {
        return apodPostCache.getIfPresent(date) != null;
    }

    public boolean containsApodList(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> allDates = new ArrayList<>();
        LocalDate tempDate = startDate;
        while(!tempDate.isAfter(endDate)) {
            allDates.add(startDate);
            tempDate = tempDate.plusDays(1);
        }

        return allDates.stream().map(LocalDate::toString)
                .allMatch(this::containsApod);
    }

    public NasaApod getApod(String date) {
        NasaApod apod = apodPostCache.getIfPresent(date);
        apodPostCache.put(apod.getDate(), apod);
        return apod;
    }

    public List<NasaApod> getApodList(LocalDate startDate, LocalDate endDate) {
        List<NasaApod> results = new ArrayList<>();

        while(!startDate.isAfter(endDate)) {
            NasaApod apod = apodPostCache.getIfPresent(startDate.toString());
            apodPostCache.put(apod.getDate(), apod);
            results.add(apod);
            startDate = startDate.plusDays(1);
        }

        return results;
    }

    public void addToCache(NasaApod apod) {
        apodPostCache.put(apod.getDate(), apod);
    }

    public void addToCache(List<NasaApod> apodList) {
        apodList.forEach(apod -> apodPostCache.put(apod.getDate(), apod));
    }
}
