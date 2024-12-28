package ssf.miniproject.weatherplan.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class User implements Serializable {
    private String email;
    private String password;
    private Set<Map<String, String>> bookmarks = new HashSet<>();

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<Map<String, String>> getBookmarks() {
        return bookmarks;
    }

    public void addBookmark(Map<String, String> event) {
        this.bookmarks.add(event);
    }
}
