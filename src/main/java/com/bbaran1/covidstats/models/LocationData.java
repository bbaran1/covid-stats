package com.bbaran1.covidstats.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationData {
    private String countryName;
    private String regionName;
    private Double latitude;
    private Double longitude;
    private Integer latestNumberOfCases;
}
