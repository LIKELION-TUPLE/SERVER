package likelion.tupl.service;

import likelion.tupl.dto.HomeworkDto;
import likelion.tupl.dto.LessonDetailDto;
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
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

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

        // course에 전체 회차 업데이트
        Course course = lesson.getCourse();
        course.setTotalLessonTime(course.getTotalLessonTime() + 1);

        // lesson에 추가적으로 저장해야 할 것: 현재 회차
        int totalTime = lesson.getCourse().getTotalLessonTime();
        int paymentCycle = lesson.getCourse().getPaymentCycle();
        int curTime = totalTime % paymentCycle;
        lesson.setCurrentLessonTime(curTime);

        // lesson을 저장
        lessonRepository.save(lesson);

        // return할 lessonDto를 업데이트
        lessonDto.setId(lesson.getId());
        lessonDto.setCourseId(lesson.getCourse().getId());
        lessonDto.setCurrentLessonTime(lesson.getCurrentLessonTime());

        return lessonDto;
    }

    // delete lesson: lesson_id에 대한 lesson 삭제
    public ResponseEntity<Map<String, Boolean>> deleteLesson(Long lesson_id) {
        // 삭제할 Lesson 객체 가져옴
        Lesson lesson = lessonRepository.findById(lesson_id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not exist with id :" + lesson_id));

        // 이 lesson이 포함되는 course의 전체 lesson list를 가져옴
        List<Lesson> lessonList = lessonRepository.findByCourseId(lesson.getCourse().getId());

        // lesson에 딸려 있는 homework 삭제
        List<Homework> homeworkList = homeworkRepository.findByLessonId(lesson_id);
        Iterator<Homework> homeworkIterator = homeworkList.iterator();
        while(homeworkIterator.hasNext()) {
            Homework homework = homeworkIterator.next();
            homeworkRepository.delete(homework);
        }

        // course에서 totalLessonTime 줄이기
        Course course = courseRepository.getById(lesson.getCourse().getId());
        int totalTime = course.getTotalLessonTime();
        course.setTotalLessonTime(totalTime - 1);
        courseRepository.save(course);

        // 해당 course에서 이 lesson보다 뒤에 생성된 lesson들의 currentLessonTime을 하나씩 줄여줌
        int targetIndex = -1;
        for (int i = 0; i < lessonList.size(); i++){
            if (lessonList.get(i).getId().equals(lesson_id)) {
                targetIndex = i;
                break;
            }
        }
        if (targetIndex < lessonList.size() - 1) { // 지우는 게 마지막이 아니면, 뒤에 애들의 currentLessonTime을 하나씩 줄여줘야 함
            for (int i = targetIndex + 1; i < lessonList.size(); i++) {
                int curTime = lessonList.get(i).getCurrentLessonTime();
                if (curTime == 1)
                    lessonList.get(i).setCurrentLessonTime(course.getPaymentCycle());
                else {
                    lessonList.get(i).setCurrentLessonTime(curTime - 1);
                }
                lessonRepository.save(lessonList.get(i));
            }
        }

        // lesson 삭제
        lessonRepository.delete(lesson);

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

    // lesson detail: lesson_id에 대한 수업 상세 = 수업 일지 전체 내용 (숙제 포함, 현재 회차 포함)
    public LessonDetailDto lessonDetail(Long lesson_id) {

        Lesson lesson = lessonRepository.findById(lesson_id).get();
        Course course = lesson.getCourse();

        // 그 코스의 lessonList를 가져옴
        List<Lesson> lessonList = lessonRepository.findByCourseId(course.getId());

        // 해당 course에서 이 lesson의 index를 가져옴
        int curLessonIndex = -1;
        for (int i = 0; i < lessonList.size(); i++){
            if (lessonList.get(i).getId().equals(lesson_id)) {
                curLessonIndex = i;
                break;
            }
        }

        // curLessonIndex가 0이 아닐 때 homeworkForTodayList에 값을 채워넣기
        List<Homework> homeworkForTodayList = new ArrayList<Homework>();
        if (curLessonIndex == 0) {
            ;
        }
        else {
            homeworkForTodayList = homeworkRepository.findByLessonId(lessonList.get(curLessonIndex - 1).getId());
        }

        // homeworkForNextList에 값 채워넣기
        List<Homework> homeworkForNextList = homeworkRepository.findByLessonId(lesson_id);

        // lessonDetailDto에 담아서 내보내기

        Iterator<Homework> homeworkForTodayIterator = homeworkForTodayList.iterator();
        Iterator<Homework> homeworkForNextIterator = homeworkForNextList.iterator();

        List<HomeworkDto> homeworkForTodayDtoList = new ArrayList<HomeworkDto>();
        List<HomeworkDto> homeworkForNextDtoList = new ArrayList<HomeworkDto>();

        while (homeworkForTodayIterator.hasNext()) {
            Homework homework = homeworkForTodayIterator.next();
            HomeworkDto homeworkDto = HomeworkDto.builder()
                    .id(homework.getId())
                    .lessonId(homework.getLesson().getId())
                    .completed(homework.getCompleted())
                    .homeworkContent(homework.getHomeworkContent())
                    .build();
            homeworkForTodayDtoList.add(homeworkDto);
        }

        while (homeworkForNextIterator.hasNext()) {
            Homework homework = homeworkForNextIterator.next();
            HomeworkDto homeworkDto = HomeworkDto.builder()
                    .id(homework.getId())
                    .lessonId(homework.getLesson().getId())
                    .completed(homework.getCompleted())
                    .homeworkContent(homework.getHomeworkContent())
                    .build();
            homeworkForNextDtoList.add(homeworkDto);
        }

        LessonDetailDto lessonDetailDto = new LessonDetailDto();
        lessonDetailDto.setId(lesson.getId());
        lessonDetailDto.setDate(lesson.getDate());
        lessonDetailDto.setStartTime(lesson.getStartTime());
        lessonDetailDto.setEndTime(lesson.getEndTime());
        lessonDetailDto.setDow(lesson.getDow());
        lessonDetailDto.setDow(lesson.getDow());
        lessonDetailDto.setPlace(lesson.getPlace());
        lessonDetailDto.setStudyContent(lesson.getStudyContent());
        lessonDetailDto.setCurrentLessonTime(lesson.getCurrentLessonTime());
        lessonDetailDto.setCourseId(lesson.getCourse().getId());
        lessonDetailDto.setHomeworkForTodayList(homeworkForTodayDtoList);
        lessonDetailDto.setHomeworkForNextList(homeworkForNextDtoList);

        lessonDetailDto.setColor(course.getColor());
        lessonDetailDto.setSchool(course.getSchool());
        lessonDetailDto.setStudentGrade(course.getStudentGrade());
        lessonDetailDto.setSubject(course.getSubject());
        lessonDetailDto.setStudentName(course.getStudentName());

        return lessonDetailDto;
    }


    // list lessons for a specific year and month : 월별 과외 기록
    public List<LessonDto> listLessonsByYearAndMonth(Long courseId, int year, int month) {
        Calendar calendar = new GregorianCalendar(year, month - 1, 1); // Month is zero-based in Calendar
        Date startDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        Date endDate = calendar.getTime();

        List<Lesson> lessons = lessonRepository.findByCourseIdAndDateBetween(courseId, startDate, endDate);

        // Convert Lesson entities to LessonDto
        return lessons.stream()
                .map(lesson -> LessonDto.builder()
                        .id(lesson.getId())
                        .date(lesson.getDate())
                        .dow(lesson.getDow())
                        .startTime(lesson.getStartTime())
                        .endTime(lesson.getEndTime())
                        .place(lesson.getPlace())
                        .studyContent(lesson.getStudyContent())
                        .currentLessonTime(lesson.getCurrentLessonTime())
                        .build())
                .collect(Collectors.toList());
    }
}
