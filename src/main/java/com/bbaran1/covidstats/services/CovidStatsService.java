package com.bbaran1.covidstats.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class CovidStatsService {
    private final String dataURL;
    private final URI dataURI;

    /**
     * Constructor.
     *
     * Creates URI from URL passed as argument.
     *
     * @param dataURL
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
     * Gets data about covid19 situation of each available country.
     *
     * Data come from CSSEGISandData csv file available on their GitHub repository.
     * https://github.com/CSSEGISandData/COVID-19/tree/master/csse_covid_19_data
     * @return
     */
    @PostConstruct
    public void getCovidData() {

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .uri(dataURI)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(parseCSVData(response.body()));
        } catch (Exception e) {
            System.out.println("Request exception occurred: " + e.getMessage());
        }
    }

    /**
     * Parses data which comes from response body.
     *
     * Data comes from csv file.
     *
     * @param responseBody
     * @return parsed data
     * @throws IOException
     */
    public static String parseCSVData(String responseBody) throws IOException {
        Reader in = new StringReader(responseBody);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        StringBuilder output = new StringBuilder();
        for (CSVRecord record : records) {
            String country = record.get("Country/Region");
            String state = record.get("Province/State");
            state = state == "" ? "N/A" : state;
            output.append(country + ", " + state + "\n");
        }
        return output.toString();
    }
}
