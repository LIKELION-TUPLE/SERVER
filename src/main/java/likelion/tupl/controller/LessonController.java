package likelion.tupl.controller;

import likelion.tupl.dto.DateDto;
import likelion.tupl.dto.LessonDetailDto;
import likelion.tupl.dto.LessonDto;
import likelion.tupl.dto.DateLessonDto;
import likelion.tupl.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class LessonController {
    final private LessonService lessonService;

    // create lesson: 수업 일지에서 입력 받아서 저장 (숙제 제외)
    @PostMapping("/lessons/{course_id}")
    public LessonDto createLesson(@PathVariable Long course_id, @RequestBody LessonDto lessonDto) {
        return lessonService.createLesson(course_id, lessonDto);
    }

    // delete lesson: lesson_id에 대한 lesson 삭제
    @DeleteMapping("/lessons/delete-lesson/{lesson_id}")
    public ResponseEntity<Map<String, Boolean>> deleteLesson(@PathVariable Long lesson_id) {
        return lessonService.deleteLesson(lesson_id);
    }

    // update lesson: lesson_id에 대한 수정 (숙제 제외)
    @PutMapping("/lessons/update-lesson/{lesson_id}")
    LessonDto updateLesson(@PathVariable Long lesson_id, @RequestBody LessonDto lessonDto) {
        return lessonService.updateLesson(lesson_id, lessonDto);
    }

    // lesson detail: lesson_id에 대한 수업 상세 = 수업 일지 전체 내용 (숙제 포함)
    @GetMapping("/lessons/lesson-detail/{lesson_id}")
    LessonDetailDto lessonDetail(@PathVariable Long lesson_id){
        return lessonService.lessonDetail(lesson_id);
    }

    // list lessons for a specific year and month : 월별 과외 기록
    @GetMapping("/lessons/{courseId}/{year}/{month}")
    public List<LessonDto> listLessonsByYearAndMonth(
            @PathVariable Long courseId,
            @PathVariable int year,
            @PathVariable int month) {
        return lessonService.listLessonsByYearAndMonth(courseId, year, month);
    }

    // date lesson list: 특정 날짜의 수업 리스트
    @GetMapping("/lessons/today")
    public List<DateLessonDto> dateLessonList(@RequestBody DateDto dateDto) {
        return lessonService.dateLessonList(dateDto);
    }
}
