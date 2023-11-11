package likelion.tupl.controller;

import likelion.tupl.dto.CourseDto;
import likelion.tupl.entity.Course;
import likelion.tupl.repository.CourseRepository;
import likelion.tupl.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CourseController {
    final private CourseService courseService;

    // create course: 과외 추가에서 입력 받아서 저장
    @PostMapping("/course/create")
    public CourseDto createCourse(@RequestBody CourseDto courseDto) {
        return courseService.createCourse(courseDto);
    }

}
