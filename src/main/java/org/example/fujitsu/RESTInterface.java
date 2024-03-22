package org.example.fujitsu;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTInterface {

    private final WeatherRepository repo;

    public RESTInterface(WeatherRepository repo) {
        this.repo = repo;
    }


    @GetMapping("fee")
    public Result fee(@RequestParam(value="city") String city, @RequestParam(value="vehicleType") String vehicleType) {

        City city1;
        if(city.equals("Tartu")){
            city1 = City.TARTU;
        } else if(city.equals("Tallinn")){
            city1 = City.TALLINN;
        } else if(city.equals("Parnu")){
            city1 = City.PARNU;
        } else {
          return new Failure("missing value for city");
        }

        Vehicle vehicle;
        if(vehicleType.equals("Car")) {
            vehicle = Vehicle.CAR;
        } else if(vehicleType.equals("Scooter")) {
            vehicle = Vehicle.SCOOTER;
        }else if(vehicleType.equals("Bike")) {
            vehicle = Vehicle.BIKE;
        } else {
            return new Failure("missing value for vehicle type");
        }

        Result result;
        BusinessRules businessRules = new BusinessRules(repo);
        try {
            result = businessRules.fee(city1, vehicle);
        } catch (BadWeatherException e) {
            return new Failure(e.getMessage());
        }
        return result;
    }
}