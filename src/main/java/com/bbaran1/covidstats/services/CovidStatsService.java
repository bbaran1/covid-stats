package com.bbaran1.covidstats.services;

import com.bbaran1.covidstats.models.LocationData;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
public class CovidStatsService {
    private final String dataURL;
    private final URI dataURI;
    private List<LocationData> locationsData;

    /**
     * Constructor.
     *
     * Creates URI from URL passed as argument.
     *
     * @param dataURL URL to CSV file
     */
    public CovidStatsService(String dataURL) {
        this.dataURL = dataURL;
        this.dataURI = URI.create(dataURL);
    }

    /**
     * No args constructor.
     *
     * Creates URI from URL passed as argument.
     */
    public CovidStatsService() {
        this.dataURL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
        this.dataURI = URI.create(dataURL);
    }

    /**
     * Gets data about Covid19 situation of each available country.
     *
     * Data come from CSSEGISandData csv file available on their GitHub repository.
     * https://github.com/CSSEGISandData/COVID-19/tree/master/csse_covid_19_data
     */
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *", zone = "Europe/Warsaw")
    public void getCovidData() {

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .uri(dataURI)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            locationsData = parseCSVData(response.body());
            System.out.println(locationsData);
        } catch (Exception e) {
            System.out.println("Request exception occurred: " + e.getMessage());
        }
    }

    /**
     * Parses data which comes from response body.
     *
     * Data comes from csv file.
     *
     * @param responseBody argument
     * @return parsed data
     * @throws IOException
     */
    public static List<LocationData> parseCSVData(String responseBody) throws IOException {
        List<LocationData> locationsData = new ArrayList<>();
        Reader in = new StringReader(responseBody);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            try {
                LocationData locationData = new LocationData(
                        replaceEmptyWithNA(replaceEmptyWithNA(record.get("Country/Region"))),
                        replaceEmptyWithNA(replaceEmptyWithNA(record.get("Province/State"))),
                        Double.parseDouble(replaceEmptyWithNA(record.get("Lat"))),
                        Double.parseDouble(replaceEmptyWithNA(record.get("Long"))),
                        Integer.parseInt(replaceEmptyWithNA(record.get(record.size() - 1)))
                );
                locationsData.add(locationData);
            } catch (Exception e) {
                System.out.println("Error adding record!");
            }
        }
        return locationsData;
    }

    /**
     * Replaces empty string with "N/A"
     *
     * @param text text to be converted
     * @return "N/A" or original text
     */
    public static String replaceEmptyWithNA(String text) {
        return text.isEmpty() ? "N/A" : text;
    }
}
