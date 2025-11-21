package study.todos.domain.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Auth {

    @Id
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    public Auth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Auth() {

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
