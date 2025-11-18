package study.todos.domain.Member.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import study.todos.domain.Member.dto.UpdateMemberReq;
import study.todos.domain.todomember.entity.TodoMember;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "email")
    private String email;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    protected Member() {}

    public Member(String userName, String email) {
        this.memberName  = userName;
        this.email = email;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void updateMember(UpdateMemberReq req){
        this.memberName = (req.memberName() != null ) ? req.memberName() : this.memberName;
        this.email = (req.email() != null) ? req.email() : this.getEmail();

    }
}
