package study.todos.domain.member.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import study.todos.domain.Member.entity.Member;
import study.todos.domain.Member.repository.JpaMemberRepository;

@DataJpaTest
public class jpaMemberRepositoryTest {

    @Autowired
    JpaMemberRepository jpaMemberRepository;

    @Test
    @DisplayName("유저_저장")
    void saveMember() {
        Member member = new Member("userName", "test@test.com");

        Member saveMember = jpaMemberRepository.save(member);

        Assertions.assertThat(saveMember.getMemberId()).isNotNull();
        Assertions.assertThat(saveMember.getMemberName()).isEqualTo("userName");
        Assertions.assertThat(saveMember.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("일정_유저_페이지_조회")
    void findMembers_성공() {

    }
}
