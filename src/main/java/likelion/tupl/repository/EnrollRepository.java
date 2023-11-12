package likelion.tupl.repository;

import likelion.tupl.entity.Enroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollRepository extends JpaRepository<Enroll, Long> {
    List<Enroll> findByMemberId(Long memberId);
}
