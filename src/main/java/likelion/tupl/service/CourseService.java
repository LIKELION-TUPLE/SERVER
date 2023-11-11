package likelion.tupl.service;

import likelion.tupl.dto.CourseDto;
import likelion.tupl.entity.Course;
import likelion.tupl.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    // create course: 과외 추가
    public CourseDto createCourse(CourseDto courseDto) {
        // lombok의 builder 사용
        // courseDto에서 받는 것: 대표 색상, 학생 이름, 학생 나이, 학생 학교, 학생 학년, 학생 폰 번호, 학부모 폰 번호,
        //                      과외 과목, 수업 시간, 입금 회차        // 밀린 입금 횟수, 전체 회차 초기 값은 0
        Course course = Course.builder()
                .color(courseDto.getColor())
                .studentName(courseDto.getStudentName())
                .studentAge(courseDto.getStudentAge())
                .school(courseDto.getSchool())
                .studentGrade(courseDto.getStudentGrade())
                .studentPhone(courseDto.getStudentPhone())
                .parentPhone(courseDto.getParentPhone())
                .subject(courseDto.getSubject())
                .courseTime(courseDto.getCourseTime())
                .paymentDelayed(0)
                .totalLessonTime(0)
                .build();

        // course을 DB에 저장
        courseRepository.save(course);

        // return할 courseDto를 업데이트
        courseDto.setId(course.getId());

        return courseDto;
    }
}
