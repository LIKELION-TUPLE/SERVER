package likelion.tupl.repository;

import likelion.tupl.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Optional<Lesson> findTopByCourseIdOrderByIdDesc(Long courseId);

    List<Lesson>  findByCourseId(Long courseId);

    List<Lesson> findByCourseIdAndDateBetween(Long courseId, Date startDate, Date endDate);

    List<Lesson> findByCourseIdIn(List<Long> courseIds);

    List<Lesson> findByCourseIdOrderByDate(Long courseId);
}
