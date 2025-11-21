package study.todos.domain.auth.dto;

public record SimpleAuthReq(String username, String password, String memberName, String email) {
}
