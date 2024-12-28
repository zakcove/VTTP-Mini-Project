package ssf.miniproject.weatherplan.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ssf.miniproject.weatherplan.models.User;

import java.util.Optional;

@Repository
public class RedisUserRepository {

    private static final String USER_KEY = "users";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveUser(User user) {
        redisTemplate.opsForHash().put(USER_KEY, user.getEmail(), user);
    }

    public Optional<User> findByEmail(String email) {
        User user = (User) redisTemplate.opsForHash().get(USER_KEY, email);
        return Optional.ofNullable(user);
    }

    public void deleteUser(String email) {
        redisTemplate.opsForHash().delete(USER_KEY, email);
    }
}
