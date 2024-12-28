package ssf.miniproject.weatherplan.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${weatherbit.api.key}")
    private String apiKey;

    @Value("${weatherbit.base.url}")
    private String baseUrl;

    public List<Map<String, String>> getWeather(String location) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s?city=%s&key=%s", baseUrl, location, apiKey);

        String response = restTemplate.getForObject(url, String.class);
        List<Map<String, String>> forecast = new ArrayList<>();

        if (response != null) {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray forecastArray = jsonResponse.getJSONArray("data");

            for (int i = 0; i < forecastArray.length(); i++) {
                JSONObject day = forecastArray.getJSONObject(i);
                Map<String, String> weatherDetails = new HashMap<>();
                weatherDetails.put("date", day.getString("valid_date"));
                weatherDetails.put("temperature", String.format("%.1f°C", day.getDouble("temp")));
                weatherDetails.put("description", day.getJSONObject("weather").getString("description"));
                weatherDetails.put("precipitation", String.format("%.1f mm", day.optDouble("precip", 0.0)));

                forecast.add(weatherDetails);
            }
        }

        return forecast;
    }
}
