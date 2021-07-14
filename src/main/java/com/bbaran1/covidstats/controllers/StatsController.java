package com.bbaran1.covidstats.controllers;

import com.bbaran1.covidstats.models.LocationData;
import com.bbaran1.covidstats.services.CovidStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatsController {

    final CovidStatsService statsService;

    @Autowired
    public StatsController(CovidStatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    public List<LocationData> getStatistics() {
        return statsService.getLocationsData();
    }
}
