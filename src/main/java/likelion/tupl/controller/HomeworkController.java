package likelion.tupl.controller;

import likelion.tupl.dto.HomeworkDto;
import likelion.tupl.dto.LessonDto;
import likelion.tupl.service.HomeworkService;
import likelion.tupl.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    // change homework completed: homework_id에 대해서 completed 여부 수정
    @PutMapping("lessons/homeworks/change-completed/{homework_id}")
    public HomeworkDto changeHomeworkCompleted(@PathVariable Long homework_id, @RequestBody HomeworkDto homeworkDto) {
        return homeworkService.changeHomeworkCompleted(homework_id, homeworkDto);
    }

    // last homework list: 특정 course_id에 대해서, 가장 최근 회차 수업에 대한 <다음 시간까지 숙제> = 새로운 <수업 일지> 생성 시, <오늘까지 숙제>에 나올 list
    @GetMapping("lessons/homeworks/last-homeworks-list/{course_id}")
    public List<HomeworkDto> lastHomeworkList(@PathVariable Long course_id) {
        return homeworkService.lastHomeworkList(course_id);
    }

}
