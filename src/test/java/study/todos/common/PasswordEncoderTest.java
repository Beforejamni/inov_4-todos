package study.todos.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.todos.common.config.PasswordEncoder;

public class PasswordEncoderTest {

    private String rawPassword = "1234test";

    @Test
    @DisplayName("비밀번호_인코딩_성공")
    void passwordTest_성공() {
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        Assertions.assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }

    @Test
    @DisplayName("비밀번호_인코딩_실패")
    void passwordTest_실패() {
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        Assertions.assertThat(passwordEncoder.matches("test1234", encodedPassword)).isFalse();
    }
}
