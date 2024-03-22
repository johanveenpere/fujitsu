package org.example.fujitsu;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
public class Weather {
    @Id
    public Long timestamp;
    @Id
    public String stationName;
    public String WMOCode;
    public double airTemperature;
    public double windspeed;
    public WeatherPhenomenon weatherPhenomenon;
}
