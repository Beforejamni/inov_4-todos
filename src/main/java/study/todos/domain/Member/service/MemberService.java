package study.todos.domain.Member.service;

import org.springframework.data.domain.Pageable;
import study.todos.common.dto.Api;
import study.todos.domain.Member.dto.SimpleMemberReq;
import study.todos.domain.Member.dto.SimpleMemberRes;
import study.todos.domain.Member.dto.UpdateMemberReq;
import study.todos.domain.todomember.dto.SimpleMemberTodoRes;
import study.todos.domain.todomember.dto.SimpleMembersTodoRes;

import java.util.List;
import java.util.Map;

public interface MemberService {

    SimpleMemberRes saveMember(SimpleMemberReq req);
    SimpleMemberRes findMember(Long memberId);
    List<SimpleMemberRes> findMembers();
    SimpleMemberRes updateMember(Long memberId, UpdateMemberReq req);
    Map<String, String> deleteMember(Long memberId);
}
