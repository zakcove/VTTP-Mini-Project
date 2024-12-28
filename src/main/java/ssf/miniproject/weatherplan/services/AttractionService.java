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
public class AttractionService {

    @Value("${ticketmaster.api.key}")
    private String apiKey;

    @Value("${ticketmaster.base.url}")
    private String baseUrl;

    public List<Map<String, String>> getAttractions(String location) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s?apikey=%s&city=%s", baseUrl, apiKey, location);

        String response = restTemplate.getForObject(url, String.class);
        List<Map<String, String>> attractions = new ArrayList<>();

        if (response != null) {
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.has("_embedded")) {
                JSONArray events = jsonResponse.getJSONObject("_embedded").getJSONArray("events");
                for (int i = 0; i < events.length(); i++) {
                    JSONObject event = events.getJSONObject(i);
                    Map<String, String> details = new HashMap<>();
                    details.put("name", event.getString("name"));
                    details.put("date", event.getJSONObject("dates").getJSONObject("start").getString("localDate"));
                    details.put("venue",
                            event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name"));
                    details.put("url", event.optString("url", "#"));
                    attractions.add(details);
                }
            }
        }

        return attractions;
    }
}
