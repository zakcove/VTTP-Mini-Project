package ssf.miniproject.weatherplan.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ssf.miniproject.weatherplan.models.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class RedisBookmarkRepository {

    private static final String USER_KEY = "users";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveUser(User user) {
        redisTemplate.opsForHash().put(USER_KEY, user.getEmail(), user);
    }

    public void saveSearch(String email, String location, String weather, List<Map<String, String>> attractions) {
        String searchKey = String.format("search:%s:%s", email, location);
        Map<String, Object> searchData = Map.of(
            "location", location,
            "weather", weather,
            "attractions", attractions
        );
        redisTemplate.opsForHash().putAll(searchKey, searchData);
    }

    public Optional<User> findByEmail(String email) {
        User user = (User) redisTemplate.opsForHash().get(USER_KEY, email);
        return Optional.ofNullable(user);
    }

    public void addBookmark(String email, Map<String, String> event) {
        User user = findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.addBookmark(event);
        saveUser(user);
    }

    public void removeBookmark(String email, Map<String, String> event) {
        User user = findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.getBookmarks().remove(event);
        saveUser(user); 
    }

    public Set<Map<String, String>> getBookmarks(String email) {
        return findByEmail(email).map(User::getBookmarks).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public Map<Object, Object> getCachedSearch(String email, String location) {
        String searchKey = String.format("search:%s:%s", email, location);
        return redisTemplate.opsForHash().entries(searchKey);
    }
}
