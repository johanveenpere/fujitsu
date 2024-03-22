package org.example.fujitsu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
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
import java.util.List;

public class DataImport {
    private final WeatherRepository repo;
    Logger logger = LoggerFactory.getLogger(DataImport.class);

    public DataImport(WeatherRepository repo) {
        this.repo = repo;
    }

    @Scheduled(cron = " 0 15 * ? ? ?")
    void pullUpdates() throws IOException, ParserConfigurationException, SAXException {
        URL url = null;
        HttpURLConnection con = null;
        try {
            url = new URL("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
        } catch (MalformedURLException e) {
            System.out.println("URL for weather updates is broken");
            System.out.println(e);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        BufferedReader inStream = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = inStream.readLine()) != null) {
            response.append(inputLine);
        }
        inStream.close();

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(response.toString());
        doc.normalize();

        Weather weather = new Weather();
        Node first = doc.getElementsByTagName("observations").item(0);
        weather.timestamp = Long.parseLong(first.getAttributes().getNamedItem("timestamp").getNodeValue());
        NodeList children = first.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            child.get
        }

    }
}
