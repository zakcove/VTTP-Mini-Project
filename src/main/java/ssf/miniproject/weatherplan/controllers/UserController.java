package ssf.miniproject.weatherplan.controllers;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ssf.miniproject.weatherplan.models.SearchResults;
import ssf.miniproject.weatherplan.models.User;
import ssf.miniproject.weatherplan.repositories.RedisBookmarkRepository;
import ssf.miniproject.weatherplan.services.AttractionService;
import ssf.miniproject.weatherplan.services.WeatherService;

@Controller
public class UserController {

    @Autowired
    private RedisBookmarkRepository redisBookmarkRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AttractionService attractionService;

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/")
    public String landingPage() {
        return "login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginErrorPage() {
        return "login-error";
    }

    @GetMapping("/register")
    public String registrationPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String email, @RequestParam String password, Model model) {
        if (redisBookmarkRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email already exists!");
            return "register";
        }

        User user = new User(email, passwordEncoder.encode(password));
        redisBookmarkRepository.saveUser(user);

        return "account-success";
    }

    @GetMapping("/home")
    public String homePage(Model model, Principal principal) {
        String email = principal.getName();
        Set<Map<String, String>> bookmarks = redisBookmarkRepository.getBookmarks(email);
        model.addAttribute("email", email);
        model.addAttribute("bookmarks", bookmarks);
        return "home";
    }

    @PostMapping("/search")
    public String searchEvents(
            @RequestParam String email,
            @RequestParam String location,
            Model model) {

        List<Map<String, String>> weather = weatherService.getWeather(location);

        List<Map<String, String>> attractions = attractionService.getAttractions(location);

        attractions.sort(Comparator.comparing(event -> event.get("date")));

        SearchResults searchResults = new SearchResults(location, weather, attractions);

        model.addAttribute("email", email);
        model.addAttribute("bookmarks", redisBookmarkRepository.getBookmarks(email));
        model.addAttribute("searchResults", searchResults);

        return "home";
    }

    @PostMapping("/bookmark")
    public String bookmarkEvent(
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String date,  
            @RequestParam String venue,
            @RequestParam String url,
            @RequestParam(required = false) String location,
            Model model) {

        Map<String, String> bookmark = Map.of(
                "name", name,
                "date", date,
                "venue", venue,
                "url", url);

        redisBookmarkRepository.addBookmark(email, bookmark);

        Set<Map<String, String>> bookmarks = redisBookmarkRepository.getBookmarks(email);

        if (location != null) {
            List<Map<String, String>> weather = weatherService.getWeather(location);
            List<Map<String, String>> attractions = attractionService.getAttractions(location);
            model.addAttribute("searchResults", new SearchResults(location, weather, attractions));
        }

        model.addAttribute("email", email);
        model.addAttribute("bookmarks", bookmarks);

        return "home";
    }

    @PostMapping("/delete-bookmark")
    public String deleteBookmark(
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String date,
            @RequestParam String venue,
            @RequestParam String url,
            Model model) {

        Map<String, String> bookmark = Map.of(
                "name", name,
                "date", date,
                "venue", venue,
                "url", url);

        redisBookmarkRepository.removeBookmark(email, bookmark);

        return "redirect:/home?email=" + email;
    }
}
