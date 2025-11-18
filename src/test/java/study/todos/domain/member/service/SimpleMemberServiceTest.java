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
import study.todos.common.dto.Api;
import study.todos.common.dto.Pagination;
import study.todos.domain.Member.dto.SimpleMemberReq;
import study.todos.domain.Member.dto.SimpleMemberRes;
import study.todos.domain.Member.dto.UpdateMemberReq;
import study.todos.domain.Member.entity.Member;
import study.todos.domain.Member.exception.MemberErrorCode;
import study.todos.domain.Member.exception.MemberException;
import study.todos.domain.Member.repository.JpaMemberRepository;
import study.todos.domain.Member.service.SimpleMemberService;
import study.todos.domain.todomember.dto.SimpleMembersTodoRes;
import study.todos.domain.todomember.service.SimpleTodoMemberService;

import java.util.List;
import java.util.Map;
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

    @Mock
    private SimpleTodoMemberService simpleTodoMemberService;

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
        List<Member> members = IntStream.range(1, 13).mapToObj(i -> new Member("userName" + i, "email")).toList();
        BDDMockito.given(jpaMemberRepository.findAll()).willReturn(members);

        List<SimpleMemberRes> response = simpleMemberService.findMembers();

        List<SimpleMemberRes> expect = members.stream().map(m -> new SimpleMemberRes(m.getMemberName(), m.getEmail())).toList();

        Assertions.assertThat(response).usingRecursiveComparison().isEqualTo(expect);

    }

    @Test
    @DisplayName("일정_유저_전체_조회")
    void findMembersByTodo_성공() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Member> members = IntStream.range(1, 11).mapToObj(i -> new Member("userName" + i, "email")).toList();
        Pagination pagination = new Pagination(0, 10, 10, 1, 10L);
        SimpleMembersTodoRes simpleMembersTodoRes = new SimpleMembersTodoRes(1L, members, pagination);

        BDDMockito.given(simpleTodoMemberService.findByTodoId(anyLong(),any(Pageable.class))).willReturn(simpleMembersTodoRes);

        Api<List<SimpleMemberRes>> response = simpleMemberService.findMembersByTodoId(1L, pageable);

        Assertions.assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(members);
        Assertions.assertThat(response.getPagination()).usingRecursiveComparison().isEqualTo(pagination);

        BDDMockito.verify(simpleTodoMemberService).findByTodoId(1L, pageable);
    }

    @Test
    @DisplayName("유저_수정_성공")
    void updateMember_성공() {
        UpdateMemberReq req = new UpdateMemberReq("updateUserName", "updateEmail");

        BDDMockito.given(jpaMemberRepository.findById(anyLong())).willReturn(Optional.of(member));

        SimpleMemberRes response = simpleMemberService.updateMember(1L, req);

        Assertions.assertThat(response.memberName()).isEqualTo("updateUserName");
        Assertions.assertThat(response.email()).isEqualTo("updateEmail");
    }

    @Test
    @DisplayName("유저_수정_실패")
    void updateMember_실패() {
        UpdateMemberReq req = new UpdateMemberReq("updateUserName", "updateEmail");

        BDDMockito.given(jpaMemberRepository.findById(anyLong())).willReturn(Optional.empty());

        MemberException memberException = assertThrows(MemberException.class, () -> simpleMemberService.updateMember(1L, req));

        Assertions.assertThat(memberException.getStatus()).isEqualTo(MemberErrorCode.NOT_FOUND.getStatus());
        Assertions.assertThat(memberException.getMessage()).isEqualTo(MemberErrorCode.NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("유저_삭제_성공")
    void deleteMember_성공() {
        BDDMockito.given(jpaMemberRepository.findById(anyLong())).willReturn(Optional.of(member));

        Map<String, String> response = simpleMemberService.deleteMember(1L);

        Assertions.assertThat(response.get("message")).isEqualTo("유저가 삭제되었습니다.");
        BDDMockito.verify(jpaMemberRepository).findById(1L);
    }
}
