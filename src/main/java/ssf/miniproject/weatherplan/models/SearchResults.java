package ssf.miniproject.weatherplan.models;

import java.util.List;
import java.util.Map;

public class SearchResults {
    private String location;
    private List<Map<String, String>> weather;
    private List<Map<String, String>> attractions;

    public SearchResults(String location, List<Map<String, String>> weather, List<Map<String, String>> attractions) {
        this.location = location;
        this.weather = weather;
        this.attractions = attractions;
    }

    public String getLocation() {
        return location;
    }

    public List<Map<String, String>> getWeather() {
        return weather;
    }

    public List<Map<String, String>> getAttractions() {
        return attractions;
    }
}
