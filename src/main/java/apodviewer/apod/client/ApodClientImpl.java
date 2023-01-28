package apodviewer.apod.client;

import apodviewer.apod.db.ApodDao;
import apodviewer.apod.model.NasaApod;
import com.google.common.cache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Component
public class ApodClientImpl implements ApodClient {

    @Autowired
    private ApodDao apodDao;

    @Autowired
    @Qualifier("apodPostCache")
    private Cache<String, NasaApod> apodPostCache;


    @Override
    public NasaApod getRandomApod() {
        return apodDao.getRandomApod();
    }

    @Override
    public List<NasaApod> getApodPage(String offset, String limit) {
        int offsetVal = Integer.parseInt(offset);
        int limitVal = Integer.parseInt(limit);

        LocalDate today = LocalDate.now(ZoneId.of("America/Los_Angeles"));

        LocalDate endDate = today.minusDays(offsetVal);
        LocalDate startDate = endDate.minusDays(limitVal - 1);

        if (allApodInCache(startDate, endDate)) {
            return getAllApodFromCache(startDate, endDate);
        } else {
            return apodDao.getApodFromTo(startDate.toString(), endDate.toString());
        }
    }

    @Override
    public List<NasaApod> searchApod(String searchString) {
        return apodDao.getAllApod().stream()
                .peek(apod -> apodPostCache.put(apod.getDate(), apod))
                .filter(apod -> StringUtils.containsIgnoreCase(apod.getExplanation(), searchString))
                .collect(Collectors.toList());
    }

    @Override
    public List<NasaApod> getRandomApods(Integer count) {
        return apodDao.getRandomApods(count).stream()
                .peek(apod -> apodPostCache.put(apod.getDate(), apod))
                .collect(Collectors.toList());
    }

    @Override
    public void addCommentToPost(String postId) {
        apodDao.incrementCommentCount(postId);
    }

    @Override
    public void upvotePost(String postId) {
        apodDao.incrementUpvoteCount(postId);
    }

    @Override
    public NasaApod getApod(String postId) {
        NasaApod apod = apodDao.getApod(postId);
        apodPostCache.put(apod.getDate(), apod);
        return apod;
    }

    // TODO: Create and interact with a cache interface
    private NasaApod getApodFromCache(String date) {
        try {
            NasaApod apod = apodPostCache.get(date, () -> getApod(date));
            apodPostCache.put(date, apod);
            return apod;
        } catch (ExecutionException e) {
            System.out.println("Failed to retrieve APOD for date " + date + " " + e);
        }

        return getApod(date);
    }

    private boolean allApodInCache(LocalDate startDate, LocalDate endDate) {
        for(LocalDate i = startDate; !i.isAfter(endDate); i = i.plusDays(1)) {
            if (apodPostCache.getIfPresent(i.toString()) == null) return false;
        }

        return true;
    }

    private List<NasaApod> getAllApodFromCache(LocalDate startDate, LocalDate endDate) {
        List<NasaApod> results = new ArrayList<>();
        for(LocalDate i = startDate; !i.isAfter(endDate); i = i.plusDays(1)) {
            NasaApod apod = getApodFromCache(i.toString());
            results.add(apod);
        }

        return results;
    }
}
