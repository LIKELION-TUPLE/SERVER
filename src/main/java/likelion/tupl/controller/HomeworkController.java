package likelion.tupl.controller;

import likelion.tupl.dto.HomeworkDto;
import likelion.tupl.dto.LessonDto;
import likelion.tupl.service.HomeworkService;
import likelion.tupl.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HomeworkController {

    private final HomeworkService homeworkService;

    // create homework: 숙제 생성
    @PostMapping("/lessons/homeworks/{lesson_id}")
    public HomeworkDto createHomework(@PathVariable Long lesson_id, @RequestBody HomeworkDto homeworkDto) {
        return homeworkService.createHomework(lesson_id, homeworkDto);
    }
}
