package likelion.tupl.controller;

import likelion.tupl.dto.CourseDto;
import likelion.tupl.dto.InviteCodeDto;
import likelion.tupl.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor

public class CourseController {
    final private CourseService courseService;

    // create course: 과외 추가에서 입력 받아서 저장
    @PostMapping("/course/create")
    public CourseDto createCourse(@Validated @RequestBody CourseDto courseDto) {
        return courseService.createCourse(courseDto);
    }

    // delete course: 과외 삭제
    @DeleteMapping("/course/delete/{courseId}")
    public ResponseEntity<Map<String, Boolean>> deleteCourse(@PathVariable Long courseId) {
        return courseService.deleteCourse(courseId);
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

    // list all courses
    @GetMapping("/course/list")
    public List<CourseDto> listAllCourses() {
        return courseService.CurrentCourses();
    }

    // student create course: 로그인한 학생에게 초대 코드 받아서 과외 등록
    @PostMapping("/course/student-create")
    public ResponseEntity<Map<String, Boolean>> studentCreateCourse(@Validated @RequestBody InviteCodeDto inviteCodeDto) {
        return courseService.studentCreateCourse(inviteCodeDto);
    }
}
