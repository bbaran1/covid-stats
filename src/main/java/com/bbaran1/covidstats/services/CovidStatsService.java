package com.bbaran1.covidstats.services;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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
     * No args constructor
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
            System.out.println(response.body());
        } catch (Exception e) {
            System.out.println("Request exception occured: " + e.getMessage());
        }
    }
}
