package study.todos.domain.Member.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.todos.common.dto.Api;
import study.todos.domain.Member.dto.SimpleMemberReq;
import study.todos.domain.Member.dto.SimpleMemberRes;
import study.todos.domain.Member.dto.UpdateMemberReq;
import study.todos.domain.Member.entity.Member;
import study.todos.domain.Member.exception.MemberErrorCode;
import study.todos.domain.Member.exception.MemberException;
import study.todos.domain.Member.repository.JpaMemberRepository;
import study.todos.domain.Member.repository.MemberRepository;

import java.util.List;
import java.util.Map;

@Transactional
@Service
public class SimpleMemberService implements MemberService{

    private final JpaMemberRepository memberRepository;

    public SimpleMemberService(JpaMemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }


    @Override
    public SimpleMemberRes saveMember(SimpleMemberReq req) {

        Member member = new Member(req.memberName(), req.email());

        Member saveMember = memberRepository.save(member);

        return new SimpleMemberRes(saveMember.getMemberName(), saveMember.getEmail());
    }

    @Override
    public SimpleMemberRes findMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        return new SimpleMemberRes(member.getMemberName(), member.getEmail());
    }

    @Override
    public Api<List<SimpleMemberRes>> findMembers(Long TodoId, Pageable pageable) {
        return null;
    }

    @Override
    public SimpleMemberRes updateMember(Long memberId, UpdateMemberReq req) {
        return null;
    }

    @Override
    public Map<String, String> deleteMember(Long memberId) {
        return Map.of();
    }
}
