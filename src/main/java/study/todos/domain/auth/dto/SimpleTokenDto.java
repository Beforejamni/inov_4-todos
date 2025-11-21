package study.todos.domain.auth.dto;

public record SimpleTokenDto(String accessToken, String refreshToken) {
}
