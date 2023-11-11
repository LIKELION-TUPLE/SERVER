package likelion.tupl.controller;

import likelion.tupl.dto.HomeworkDto;
import likelion.tupl.dto.LessonDto;
import likelion.tupl.service.HomeworkService;
import likelion.tupl.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HomeworkController {

    private final HomeworkService homeworkService;

    // create homework: course_id에 대한 숙제 생성
    @PostMapping("/lessons/homeworks/{lesson_id}")
    public HomeworkDto createHomework(@PathVariable Long lesson_id, @RequestBody HomeworkDto homeworkDto) {
        return homeworkService.createHomework(lesson_id, homeworkDto);
    }

    // delete homework: homework_id에 대한 숙제 삭제
    @DeleteMapping("lessons/homeworks/{homework_id}")
    public ResponseEntity<Map<String, Boolean>> deleteHomework(@PathVariable Long homework_id) {
        return homeworkService.deleteHomework(homework_id);
    }

}
