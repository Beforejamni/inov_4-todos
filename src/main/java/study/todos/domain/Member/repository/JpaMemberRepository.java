package study.todos.domain.Member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.todos.domain.Member.entity.Member;

@Repository
public interface JpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
}
