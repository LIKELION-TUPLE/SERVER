package likelion.tupl.service;

import likelion.tupl.dto.LessonDto;
import likelion.tupl.entity.Course;
import likelion.tupl.entity.Lesson;
import likelion.tupl.repository.CourseRepository;
import likelion.tupl.repository.HomeworkRepository;
import likelion.tupl.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final HomeworkRepository homeworkRepository;
    private final CourseRepository courseRepository;

    // create lesson: 수업 일지에서 입력 받아서 저장 (숙제 제외)
    public LessonDto createLesson(Long course_id, LessonDto lessonDto) {
        // DB에 저장할 Lesson 객체 생성
        Lesson lesson = new Lesson();

        // course_id에서 받는 것: course_id
        lesson.setCourse(courseRepository.getById(course_id));

        // lessdonDto에서 받는 것: 날짜, 요일, 시작 시간, 종료 시간, 장소, 오늘 나간 진도
        lesson.setDate(lessonDto.getDate());
        lesson.setDow(lessonDto.getDow());
        lesson.setStartTime(lessonDto.getStartTime());
        lesson.setEndTime(lessonDto.getEndTime());
        lesson.setPlace(lessonDto.getPlace());
        lesson.setStudyContent(lessonDto.getStudyContent());

        // lesson에 추가적으로 저장해야 할 것: 현재 회차
        int totalTime = lesson.getCourse().getTotalLessonTime();
        int courseTime = lesson.getCourse().getCourseTime();
        int curTime = totalTime % courseTime;
        lesson.setCurrentLessonTime(curTime);

        // course에 전체 회차 업데이트
        Course course = lesson.getCourse();
        course.setTotalLessonTime(course.getTotalLessonTime() + 1);

        // lesson을 저장
        lessonRepository.save(lesson);

        // return할 lessonDto를 업데이트
        lessonDto.setId(lesson.getId());
        lessonDto.setCourseId(lesson.getCourse().getId());

        return lessonDto;
    }
}
