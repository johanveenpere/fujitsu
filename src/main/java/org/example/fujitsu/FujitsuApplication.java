package org.example.fujitsu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@SpringBootApplication
public class FujitsuApplication {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        SpringApplication.run(FujitsuApplication.class, args);
    }

}
