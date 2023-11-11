package likelion.tupl.service;

import likelion.tupl.dto.HomeworkDto;
import likelion.tupl.entity.Homework;
import likelion.tupl.exception.ResourceNotFoundException;
import likelion.tupl.repository.HomeworkRepository;
import likelion.tupl.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final LessonRepository lessonRepository;

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
}
