package apodviewer.apod.cache;

import apodviewer.apod.model.NasaApod;
import com.google.common.cache.Cache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApodCacheWrapperTest {

    @Mock
    private Cache<String, NasaApod> mockCache;

    private ApodCacheWrapper apodCacheWrapper;

    @Before
    public void setup(){
        apodCacheWrapper = new ApodCacheWrapper(mockCache);
    }


    @Test
    public void testContainsApodList() {
        LocalDate testStartDate = LocalDate.of(2022, 9, 1);
        LocalDate testEndDate = LocalDate.of(2022, 9, 5);

        when(mockCache.getIfPresent("2022-09-01")).thenReturn(buildTestApod("2022-09-01"));
        when(mockCache.getIfPresent("2022-09-02")).thenReturn(buildTestApod("2022-09-02"));
        when(mockCache.getIfPresent("2022-09-03")).thenReturn(buildTestApod("2022-09-03"));
        when(mockCache.getIfPresent("2022-09-04")).thenReturn(buildTestApod("2022-09-04"));
        when(mockCache.getIfPresent("2022-09-05")).thenReturn(buildTestApod("2022-09-05"));

        boolean actualResult = apodCacheWrapper.containsApodList(testStartDate, testEndDate);
        boolean expectedResult = true;

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testContainsApodListFail() {
        LocalDate testStartDate = LocalDate.of(2022, 9, 1);
        LocalDate testEndDate = LocalDate.of(2022, 9, 5);

        when(mockCache.getIfPresent("2022-09-01")).thenReturn(buildTestApod("2022-09-01"));
        when(mockCache.getIfPresent("2022-09-02")).thenReturn(buildTestApod("2022-09-02"));
        when(mockCache.getIfPresent("2022-09-03")).thenReturn(buildTestApod("2022-09-03"));

        boolean actualResult = apodCacheWrapper.containsApodList(testStartDate, testEndDate);
        boolean expectedResult = false;

        assertEquals(expectedResult, actualResult);
    }

    private NasaApod buildTestApod(String date) {
        return NasaApod.builder()
                .date(date)
                .build();
    }
}
