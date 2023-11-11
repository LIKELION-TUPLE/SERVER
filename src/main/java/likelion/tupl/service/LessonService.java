package likelion.tupl.service;

import likelion.tupl.dto.HomeworkDto;
import likelion.tupl.dto.LessonDto;
import likelion.tupl.entity.Course;
import likelion.tupl.entity.Homework;
import likelion.tupl.entity.Lesson;
import likelion.tupl.exception.ResourceNotFoundException;
import likelion.tupl.repository.CourseRepository;
import likelion.tupl.repository.HomeworkRepository;
import likelion.tupl.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        int paymentCycle = lesson.getCourse().getPaymentCycle();
        int curTime = totalTime % paymentCycle;
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

    // delete lesson: lesson_id에 대한 lesson 삭제
    public ResponseEntity<Map<String, Boolean>> deleteLesson(Long lesson_id) {
        // 삭제할 Lesson 객체 가져옴
        Lesson lesson = lessonRepository.findById(lesson_id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not exist with id :" + lesson_id));
        // lesson에 딸려 있는 homework 삭제
        List<Homework> homeworkList = homeworkRepository.findByLessonId(lesson_id);
        Iterator<Homework> homeworkIterator = homeworkList.iterator();
        while(homeworkIterator.hasNext()) {
            Homework homework = homeworkIterator.next();
            homeworkRepository.delete(homework);
        }

        // lesson 삭제
        lessonRepository.delete(lesson);

        // course에서 totalLessonTime 줄이기
        Course course = courseRepository.getById(lesson.getCourse().getId());
        int totalTime = course.getTotalLessonTime();
        course.setTotalLessonTime(totalTime - 1);
        courseRepository.save(course);

        Map<String, Boolean> response = new HashMap<>();
        response.put("Lesson-deleted", Boolean.TRUE);

        return ResponseEntity.ok(response);
    }

    // update lesson: lesson_id에 대한 수정 (숙제 제외)
    public LessonDto updateLesson(Long lesson_id, LessonDto lessonDto) {
        Lesson lesson = lessonRepository.findById(lesson_id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not exist with id : " + lesson_id));
        lesson.setDate(lessonDto.getDate());
        lesson.setStartTime(lessonDto.getStartTime());
        lesson.setEndTime(lessonDto.getEndTime());
        lesson.setDow(lessonDto.getDow());
        lesson.setPlace(lessonDto.getPlace());
        lesson.setStudyContent(lessonDto.getStudyContent());

        lessonRepository.save(lesson);

        lessonDto.setId(lesson.getId());
        lessonDto.setCourseId(lesson.getCourse().getId());
        lessonDto.setCurrentLessonTime(lesson.getCurrentLessonTime());

        return lessonDto;
    }
}
