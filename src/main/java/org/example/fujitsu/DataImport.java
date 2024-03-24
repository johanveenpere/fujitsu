package org.example.fujitsu;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class DataImport {
    private static WeatherRepository repo;
    static Logger logger = LoggerFactory.getLogger(DataImport.class);

    public DataImport(WeatherRepository repo) {
        this.repo = repo;
    }

    @Scheduled(cron = " 0 15 * * * *")
    static void pullUpdates() throws IOException, ParserConfigurationException, SAXException {
        logger.info("pulling data");
        URL url = null;
        HttpURLConnection con = null;
        try {
            url = new URL("http://www.ilmateenistus.ee/ilma_andmed/xml/observations.php");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
        } catch (MalformedURLException e) {
            logger.error("URL for weather updates is broken");
            System.out.println(e);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        BufferedReader inStream = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        logger.info("line 57");
        while ((inputLine = inStream.readLine()) != null) {
            response.append(inputLine);
        }
        inStream.close();

        logger.info("line 63");
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(response.toString().substring(response.toString().indexOf('<')));
        doc.normalize();

        logger.info("line 68");
        Node first = doc.getElementsByTagName("observations").item(0);
        long timestamp = Long.parseLong(first.getAttributes().getNamedItem("timestamp").getNodeValue());
        NodeList children = first.getChildNodes();
        List<String> cities = Arrays.asList("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");
        logger.info(String.valueOf(children.getLength()));
        logger.info("line 74");
        for (int i = 0; i < children.getLength(); i++) {
            logger.info(children.item(0).getNodeValue());
            Node child = children.item(i);
            NodeList tags = child.getChildNodes();
            if (!cities.contains(tags.item(0).getNodeValue()))
                continue;
            Weather weather = new Weather();
            weather.timestamp = timestamp;
            weather.stationName = tags.item(0).getNodeValue();
            weather.WMOCode = tags.item(1).getNodeValue();
            weather.weatherPhenomenon = switch (tags.item(4).getNodeValue()) {
                case "Sleet" -> WeatherPhenomenon.SLEET;
                case "Snow" -> WeatherPhenomenon.SNOW;
                case "Light shower", "Moderate shower", "Heavy shower", "Light rain", "Moderate rain", "Heavy rain" ->
                        WeatherPhenomenon.RAIN;
                case "Glaze" -> WeatherPhenomenon.GLAZE;
                case "Hail" -> WeatherPhenomenon.HAIL;
                case "Thunder" -> WeatherPhenomenon.THUNDER;
                default -> WeatherPhenomenon.OTHER;
            };
            weather.airTemperature = Double.parseDouble(tags.item(9).getNodeValue());
            weather.windspeed = Double.parseDouble(tags.item(11).getNodeValue());
            logger.info("saving data to db");
            repo.save(weather);
        }



    }
    @PostConstruct
    void onStartup() {
        logger.info("pulling data on startup");
        try {
            pullUpdates();
        } catch (IOException e) {

        } catch (ParserConfigurationException e) {
        } catch (SAXException e) {
        }
    }
}
