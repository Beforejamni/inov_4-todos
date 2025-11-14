package study.todos.domain.member.service;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import study.todos.domain.Member.dto.SimpleMemberReq;
import study.todos.domain.Member.dto.SimpleMemberRes;
import study.todos.domain.Member.entity.Member;
import study.todos.domain.Member.exception.MemberErrorCode;
import study.todos.domain.Member.exception.MemberException;
import study.todos.domain.Member.repository.JpaMemberRepository;
import study.todos.domain.Member.service.SimpleMemberService;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
@Transactional
public class SimpleMemberServiceTest {

    @Mock
    private  JpaMemberRepository jpaMemberRepository;


    @InjectMocks
    private SimpleMemberService simpleMemberService;

    private final Member member = new Member("userName", "test@test.com");


    @Test
    @DisplayName("유저_저장_성공")
    void saveMember() {
        SimpleMemberReq req = new SimpleMemberReq("userName", "test@test.com");
        BDDMockito.given(jpaMemberRepository.save(any(Member.class))).willReturn(member);

        SimpleMemberRes response = simpleMemberService.saveMember(req);

        Assertions.assertThat(response.memberName()).isEqualTo("userName");
        Assertions.assertThat(response.email()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("유저_조회_성공")
    void findMember_성공() {
        BDDMockito.given(jpaMemberRepository.findById(anyLong())).willReturn(Optional.of(member));

        SimpleMemberRes response = simpleMemberService.findMember(1L);

        Assertions.assertThat(response.memberName()).isEqualTo("userName");
        Assertions.assertThat(response.email()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("유저_조회_실패")
    void findMember_실패() {
        BDDMockito.given(jpaMemberRepository.findById(anyLong())).willReturn(Optional.empty());

        MemberException memberException = assertThrows(MemberException.class,
                () -> simpleMemberService.findMember(1L));

        Assertions.assertThat(memberException.getStatus()).isEqualTo(MemberErrorCode.NOT_FOUND.getStatus());
        Assertions.assertThat(memberException.getMessage()).isEqualTo(MemberErrorCode.NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("유저_전체_조회")
    void findMembers_성공() {
        Pageable pageable = PageRequest.of(1, 10);
        List<Member> members = IntStream.range(1, 20).mapToObj(i -> new Member("userName" + i, "email")).toList();

        Page<Member> memberPage = new PageImpl<>(members, pageable, members.size());

//        BDDMockito.given(jpaMemberRepository.findAllByTodo_TodoId(any(Pageable.class))).willReturn(memberPage);

        simpleMemberService.findMembers(1L, pageable);
    }
}
