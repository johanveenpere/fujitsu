package org.example.fujitsu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootTest
class FujitsuApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    WeatherRepository repo;
    @Test
    void dbTest() {
        Weather weather = new Weather();
        weather.weatherPhenomenon = WeatherPhenomenon.RAIN;
        weather.stationName = "teststation";
        weather.airTemperature = 20;
        weather.windspeed = 10;
        weather.timestamp = 100000L;

        repo.save(weather);
        System.out.println(repo.findAll().getFirst().stationName);
    }

}
