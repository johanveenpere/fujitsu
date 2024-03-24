package org.example.fujitsu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

public class BusinessRules {

    private final WeatherRepository repo;

    public BusinessRules(WeatherRepository repo) {
        this.repo = repo;
    }


    @Bean
    public Double fee(City city, Vehicle vehicleType) throws BadWeatherException, NoDataException {
        List<Weather> results = repo.findAll();
        if(results.size() == 0) {
            throw new NoDataException();
        }
        Weather weather = results.get(0);
        double airTemperature = weather.airTemperature, windspeed = weather.windspeed;
        WeatherPhenomenon phenomenon = WeatherPhenomenon.RAIN;

        final float maxWindspeed = 20; // m/s
        boolean tooHighWindspeed = windspeed > maxWindspeed;
        boolean dangerousPhenomenon = phenomenon == WeatherPhenomenon.GLAZE || phenomenon == WeatherPhenomenon.HAIL || phenomenon == WeatherPhenomenon.THUNDER;
        if ( tooHighWindspeed || dangerousPhenomenon ) {
            throw new BadWeatherException("Usage of selected vehicle type is forbidden");
        }

        double BaseFee = 0;

        double airTempExtraFee = 0;
        if (airTemperature < -10) airTempExtraFee = 1;
        if (airTemperature >= -10 && airTemperature <= 0) airTempExtraFee = 0.5;

        double windspeedExtraFee = 0;
        if (windspeed >= 10 && windspeed <= 20) windspeedExtraFee = 0.5;

        double weatherPhenomenonExtraFee = 0;
        if (phenomenon == WeatherPhenomenon.SNOW|| phenomenon == WeatherPhenomenon.SLEET) weatherPhenomenonExtraFee = 1;
        if (phenomenon == WeatherPhenomenon.RAIN) weatherPhenomenonExtraFee = 0.5;

        return (BaseFee + airTempExtraFee + windspeedExtraFee + weatherPhenomenonExtraFee);
    }
}
