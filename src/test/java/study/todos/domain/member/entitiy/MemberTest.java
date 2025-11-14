package study.todos.domain.member.entitiy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.todos.domain.Member.entity.Member;

public class MemberTest {
    @Test
    @DisplayName("유저_생성")
    void createMember() {
        Member member = new Member("userName", "test@test.com");

        Assertions.assertThat(member.getMemberName()).isEqualTo("userName");
        Assertions.assertThat(member.getEmail()).isEqualTo("test@test.com");
    }
}
