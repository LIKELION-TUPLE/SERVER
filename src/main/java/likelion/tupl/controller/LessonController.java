package likelion.tupl.controller;

import likelion.tupl.dto.LessonDto;
import likelion.tupl.entity.Lesson;
import likelion.tupl.repository.HomeworkRepository;
import likelion.tupl.repository.LessonRepository;
import likelion.tupl.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LessonController {
    final LessonService lessonService;

    // create lesson
    @PostMapping("/lessons/{course_id}")
    public LessonDto createLesson(@PathVariable Long course_id, @RequestBody LessonDto lessonDto) {
        return lessonService.createLesson(course_id, lessonDto);
    }

}
