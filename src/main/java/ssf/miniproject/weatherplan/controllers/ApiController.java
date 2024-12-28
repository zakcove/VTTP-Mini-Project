package ssf.miniproject.weatherplan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ssf.miniproject.weatherplan.services.WeatherService;
import ssf.miniproject.weatherplan.services.AttractionService;
import ssf.miniproject.weatherplan.repositories.RedisBookmarkRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private AttractionService attractionService;

    @Autowired
    private RedisBookmarkRepository redisBookmarkRepository;

    @PostMapping("/search")
    public Map<String, Object> search(
            @RequestParam String email,
            @RequestBody Map<String, String> payload) {

        String location = payload.get("location");

        List<Map<String, String>> weather = weatherService.getWeather(location);

        List<Map<String, String>> attractions = attractionService.getAttractions(location);

        redisBookmarkRepository.saveSearch(email, location, weather.toString(), attractions);

        return Map.of(
                "location", location,
                "weather", weather,
                "attractions", attractions
        );
    }

    @PostMapping("/bookmark")
    public String bookmark(
            @RequestParam String email,
            @RequestBody Map<String, String> payload) {

        String name = payload.get("name");
        String date = payload.get("date");
        String venue = payload.get("venue");

        Map<String, String> event = Map.of(
                "name", name,
                "date", date,
                "venue", venue
        );

        redisBookmarkRepository.addBookmark(email, event);

        return "Activity bookmarked successfully!";
    }

    @PostMapping("/delete-bookmark")
    public String deleteBookmark(
            @RequestParam String email,
            @RequestBody Map<String, String> payload) {

        String name = payload.get("name");
        String date = payload.get("date");
        String venue = payload.get("venue");

        Map<String, String> event = Map.of(
                "name", name,
                "date", date,
                "venue", venue
        );

        redisBookmarkRepository.removeBookmark(email, event);

        return "Bookmark deleted successfully!";
    }

    @GetMapping("/cached-search")
    public Map<String, Object> getCachedSearch(
            @RequestParam String email,
            @RequestParam String location) {

        Map<Object, Object> cachedSearch = redisBookmarkRepository.getCachedSearch(email, location);

        if (cachedSearch.isEmpty()) {
            return Map.of("message", "No cached results found for the given location.");
        }

        return Map.of(
                "location", cachedSearch.get("location"),
                "weather", cachedSearch.get("weather"),
                "attractions", cachedSearch.get("attractions")
        );
    }
}

