package study.todos.domain.Member.service;

import org.springframework.data.domain.Pageable;
import study.todos.common.dto.Api;
import study.todos.domain.Member.dto.SimpleMemberReq;
import study.todos.domain.Member.dto.SimpleMemberRes;
import study.todos.domain.Member.dto.UpdateMemberReq;

import java.util.List;
import java.util.Map;

public interface MemberService {

    SimpleMemberRes saveMember(SimpleMemberReq req);
    SimpleMemberRes findMember(Long memberId);
    Api<List<SimpleMemberRes>> findMembers(Long TodoId, Pageable pageable);
    SimpleMemberRes updateMember(Long memberId, UpdateMemberReq req);
    Map<String, String> deleteMember(Long memberId);
}
