package likelion.tupl.controller;

import likelion.tupl.dto.CourseDto;
import likelion.tupl.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CourseController {
    final private CourseService courseService;

    // create course: 과외 추가에서 입력 받아서 저장
    @PostMapping("/course/create")
    public CourseDto createCourse(@Validated @RequestBody CourseDto courseDto) {
        return courseService.createCourse(courseDto);
    }

    // delete course: 과외 삭제
    @DeleteMapping("/course/delete/{courseId}")
    public void deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
    }

    // update course: 과외 수정
    @PutMapping("/course/update/{courseId}")
    public CourseDto updateCourse(@PathVariable Long courseId, @Validated @RequestBody CourseDto updatedCourseDto) {
        return courseService.updateCourse(courseId, updatedCourseDto);
    }

    // get course by ID: 과외 조회
    @GetMapping("/course/{courseId}")
    public CourseDto getCourseById(@PathVariable Long courseId) {
        return courseService.getCourseById(courseId);
    }
}
