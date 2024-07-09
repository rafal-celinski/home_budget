package xyz.celinski.home_budget.dto;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class HttpErrorDTO {
    private String timestamp;
    private int status;
    private String error;
    private String path;

    public HttpErrorDTO() {}

    public HttpErrorDTO(Date timestampDate, int status, String error, String path) {
        setTimestampFromDate(timestampDate);
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestampFromDate(Date date) {
        this.timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'")
                .format(date.toInstant().atZone(ZoneOffset.UTC));
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}