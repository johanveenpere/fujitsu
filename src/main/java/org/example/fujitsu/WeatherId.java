package org.example.fujitsu;

import java.io.Serializable;

public class WeatherId implements Serializable {
    private Long timestamp;
    private String stationName;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
}
