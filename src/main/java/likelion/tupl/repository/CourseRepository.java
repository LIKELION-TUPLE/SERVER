package likelion.tupl.repository;

import likelion.tupl.entity.Course;
import likelion.tupl.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT e.course FROM Enroll e WHERE e.member.id = :memberId")
    List<Course> findAllByMemberId(Long memberId);

}
