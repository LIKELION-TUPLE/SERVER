package likelion.tupl.controller;

import likelion.tupl.dto.LessonDto;
import likelion.tupl.entity.Lesson;
import likelion.tupl.repository.HomeworkRepository;
import likelion.tupl.repository.LessonRepository;
import likelion.tupl.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LessonController {
    final private LessonService lessonService;

    // create lesson: 수업 일지에서 입력 받아서 저장 (숙제 제외)
    @PostMapping("/lessons/{course_id}")
    public LessonDto createLesson(@PathVariable Long course_id, @RequestBody LessonDto lessonDto) {
        return lessonService.createLesson(course_id, lessonDto);
    }

    // delete lesson: lesson_id에 대한 lesson 삭제
    @DeleteMapping("lessons/delete-lesson/{lesson_id}")
    public ResponseEntity<Map<String, Boolean>> deleteLesson(@PathVariable Long lesson_id) {
        return lessonService.deleteLesson(lesson_id);
    }

    // update lesson: lesson_id에 대한 수정 (숙제 제외)
    @PutMapping("lessons/update-lesson/{lesson_id}")
    LessonDto updateLesson(@PathVariable Long lesson_id, @RequestBody LessonDto lessonDto) {
        return lessonService.updateLesson(lesson_id, lessonDto);
    }

}
