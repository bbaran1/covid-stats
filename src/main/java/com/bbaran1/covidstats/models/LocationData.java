package com.bbaran1.covidstats.models;

import lombok.Data;

@Data
public class LocationData {
    private String countryName;
    private String regionName;
    private float latitude;
    private float longitude;
    private int latestNumberOfCases;
}
