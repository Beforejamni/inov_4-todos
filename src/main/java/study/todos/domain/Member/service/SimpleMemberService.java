package study.todos.domain.Member.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
import study.todos.domain.todomember.dto.SimpleMembersTodoRes;
import study.todos.domain.todomember.service.SimpleTodoMemberService;

import java.util.List;
import java.util.Map;

@Transactional
@Service
public class SimpleMemberService implements MemberService{

    private final JpaMemberRepository memberRepository;
    private final SimpleTodoMemberService simpleTodoMemberService;

    public SimpleMemberService(JpaMemberRepository memberRepository, SimpleTodoMemberService simpleTodoMemberService){
        this.memberRepository = memberRepository;
        this.simpleTodoMemberService = simpleTodoMemberService;
    }


    @Override
    public SimpleMemberRes saveMember(SimpleMemberReq req) {

        Member member = new Member(req.memberName(), req.email());

        Member saveMember = memberRepository.save(member);

        return new SimpleMemberRes(saveMember.getMemberName(), saveMember.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public SimpleMemberRes findMember(Long memberId) {
        Member member = extractMember(memberRepository, memberId);
        return new SimpleMemberRes(member.getMemberName(), member.getEmail());
    }


    @Override
    @Transactional(readOnly = true)
    public List<SimpleMemberRes> findMembers() {
        List<Member> response = memberRepository.findAll();
        return response.stream().map(m -> new SimpleMemberRes(m.getMemberName(), m.getEmail())).toList();
    }


    @Transactional(readOnly = true)
    public Api<List<SimpleMemberRes>> findMembersByTodoId(Long todoId, Pageable pageable) {
        SimpleMembersTodoRes memberByTodoId = simpleTodoMemberService.findByTodoId(todoId, pageable);

        List<Member> members = memberByTodoId.getMembers();
        List<SimpleMemberRes> simpleMemberResponses
                = members.stream().map(m -> new SimpleMemberRes(m.getMemberName(), m.getEmail())).toList();
        return new Api<>(simpleMemberResponses, memberByTodoId.getPagination());
    }

    @Override
    public SimpleMemberRes updateMember(Long memberId, UpdateMemberReq req) {
        Member foundMember = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberException(MemberErrorCode.NOT_FOUND)
        );

      foundMember.updateMember(req);

        return new SimpleMemberRes(foundMember.getMemberName(), foundMember.getEmail());
    }

    @Override
    public Map<String, String> deleteMember(Long memberId) {

        Member member = extractMember(memberRepository, memberId);

        memberRepository.delete(member);

        return Map.of("message" , "유저가 삭제되었습니다.");
    }

    public static Member extractMember(JpaMemberRepository memberRepository, Long memberId) {
       return memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
    }
}
