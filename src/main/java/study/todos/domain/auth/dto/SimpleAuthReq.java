package study.todos.domain.auth.dto;

public class SimpleAuthReq {
    private final String username;
    private final String password;
    private final String memberName;
    private final String email;

    public SimpleAuthReq(String username, String password, String memberName, String email) {
        this.username = username;
        this.password = password;
        this.memberName = memberName;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getEmail() {
        return email;
    }
}
