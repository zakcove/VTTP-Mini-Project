package ssf.miniproject.weatherplan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ssf.miniproject.weatherplan.repositories.RedisBookmarkRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private RedisBookmarkRepository redisBookmarkRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ssf.miniproject.weatherplan.models.User user = redisBookmarkRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .roles("USER") 
            .build();
    }
}
