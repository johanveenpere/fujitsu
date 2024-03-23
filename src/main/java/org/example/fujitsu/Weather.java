package org.example.fujitsu;

import jakarta.persistence.*;

@Entity
@IdClass(WeatherId.class)
public class Weather {
    @Id
    public Long timestamp;
    @Id
    public String stationName;
    public String WMOCode;
    public double airTemperature;
    @Enumerated(EnumType.STRING)
    public WeatherPhenomenon weatherPhenomenon;
    public double windspeed;
}
