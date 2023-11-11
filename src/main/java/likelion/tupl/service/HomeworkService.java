package likelion.tupl.service;

import likelion.tupl.dto.HomeworkDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
@RequiredArgsConstructor
public class HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    // create homework: course_id에 대한 숙제 생성
    public HomeworkDto createHomework(Long lesson_id, HomeworkDto homeworkDto) {
        // DB에 저장할 Homework 객체 생성
        Homework homework = new Homework();

        // lesson_id에서 받는 것: lesson_id
        homework.setLesson(lessonRepository.getById(lesson_id));

        // completed의 기본값은 FALSE
        homework.setCompleted(Boolean.FALSE);

        // homeworkDto에서 받는 것: 숙제 내용
        homework.setHomeworkContent(homeworkDto.getHomeworkContent());

        // homework를 저장
        homeworkRepository.save(homework);

        // return할 homeworkDto를 업데이트
        homeworkDto.setId(homework.getId());
        homeworkDto.setCompleted(homework.getCompleted());
        homeworkDto.setLessonId(homework.getLesson().getId());

        return homeworkDto;
    }

    // delete homework: homework_id에 대한 숙제 삭제
    public ResponseEntity<Map<String, Boolean>> deleteHomework(Long homework_id) {
        // 삭제할 Homework 객체 가져옴
        Homework homework = homeworkRepository.findById(homework_id)
                .orElseThrow(() -> new ResourceNotFoundException("Homework not exist with id :" + homework_id));
        homeworkRepository.delete(homework);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    // change homework completed: homework_id에 대해서 completed 여부 수정
    public HomeworkDto changeHomeworkCompleted(Long homework_id, HomeworkDto homeworkDto) {
        // 수정할 숙제 찾아옴
        Homework homework = homeworkRepository.findById(homework_id)
                .orElseThrow(() -> new ResourceNotFoundException("Homework not exist with id :" + homework_id));
        // 해당 숙제에서 completed 값 변경 후 저장
        homework.setCompleted(homeworkDto.getCompleted());
        homeworkRepository.save(homework);

        // homeworkDto에 homework 저장
        homeworkDto.setLessonId(homework.getLesson().getId());
        homeworkDto.setHomeworkContent(homework.getHomeworkContent());
        homeworkDto.setId(homework.getId());

        return homeworkDto;
    }

    // last homework list: 가장 최근 회차 수업에 대한 <다음 시간까지 숙제> = 새로운 <수업 일지> 생성 시, <오늘까지 숙제>에 나올 list
    public List<HomeworkDto> lastHomeworkList(Long course_id) {

        // 첫 회차 lesson일 때 null 값 반환
        if (courseRepository.findById(course_id).get().getTotalLessonTime().equals(0))
            return null;

        // 첫 회차 lesson이 아닐 때 전 lesson의 homeworkList 반환

        // 1. 현재 course에 대한 lesson 객체/ID를 받아옴
        Optional<Lesson> lessonOptional = lessonRepository.findTopByCourseIdOrderByIdDesc(course_id);
        Long lesson_id = lessonOptional.map(Lesson::getId).orElse(null);

        // 2. 해당 lesson에 대한 homeworkList 받아오기
        List<Homework> homeworkList = homeworkRepository.findByLessonId(lesson_id);

        // 3. homeworkDtoList에 옮겨서 반환
        Iterator<Homework> homeworkIterator = homeworkList.iterator();
        List<HomeworkDto> homeworkDtoList = new ArrayList<HomeworkDto>();
        while(homeworkIterator.hasNext()) {
            Homework homework = homeworkIterator.next();
            HomeworkDto homeworkDto = new HomeworkDto();
            homeworkDto.setId(homework.getId());
            homeworkDto.setLessonId(homework.getLesson().getId());
            homeworkDto.setCompleted(homework.getCompleted());
            homeworkDto.setHomeworkContent(homework.getHomeworkContent());
            homeworkDtoList.add(homeworkDto);
        }

        return homeworkDtoList;
    }
}
