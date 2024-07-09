package xyz.celinski.home_budget.dto;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpErrorDTOTest {
    HttpErrorDTO httpErrorDTO = new HttpErrorDTO();

    @Test
    public void setTimestampFromDate_shouldSetTimestamp_whenDateIsValid() {
        Date date = new Date();
        httpErrorDTO.setTimestampFromDate(date);
        assertThat(httpErrorDTO.getTimestamp()).isNotNull();
    }

    @Test
    public void setTimestampFromDate_shouldSetTimestamp_whenDateIsBeforeEpoch() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(1969, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();

        httpErrorDTO.setTimestampFromDate(date);
        assertThat(httpErrorDTO.getTimestamp()).isEqualTo("1969-01-01T00:00:00.000+00:00");
    }

    @Test
    public void setTimestampFromDate_shouldSetTimestamp_whenDateIsEpoch() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();

        httpErrorDTO.setTimestampFromDate(date);
        assertThat(httpErrorDTO.getTimestamp()).isEqualTo("1970-01-01T00:00:00.000+00:00");
    }

    @Test
    public void setTimestampFromDate_shouldSetTimestamp_whenDateIsToday() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();

        httpErrorDTO.setTimestampFromDate(date);
        assertThat(httpErrorDTO.getTimestamp()).isEqualTo("2024-01-01T00:00:00.000+00:00");
    }

    @Test
    public void setTimestampFromDate_shouldSetTimestamp_whenDateIsInFuture() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(5000, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();

        httpErrorDTO.setTimestampFromDate(date);
        assertThat(httpErrorDTO.getTimestamp()).isEqualTo("5000-01-01T00:00:00.000+00:00");
    }

    @Test
    public void setTimestampFromDate_shouldSetTimestamp_whenDateIsAccurateToMilliseconds() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(2024, Calendar.JULY, 5, 1, 47, 20);
        calendar.set(Calendar.MILLISECOND, 163);
        Date date = calendar.getTime();

        httpErrorDTO.setTimestampFromDate(date);
        assertThat(httpErrorDTO.getTimestamp()).isEqualTo("2024-07-05T01:47:20.163+00:00");
    }

    @Test
    public void setTimestampFromDate_shouldSetTimestampInUTC_whenDateIsInOtherTimeZone() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+01:00"));
        calendar.set(2024, Calendar.JULY, 5, 1, 47, 20);
        calendar.set(Calendar.MILLISECOND, 163);
        Date date = calendar.getTime();

        httpErrorDTO.setTimestampFromDate(date);
        assertThat(httpErrorDTO.getTimestamp()).isEqualTo("2024-07-05T00:47:20.163+00:00");
    }
}
