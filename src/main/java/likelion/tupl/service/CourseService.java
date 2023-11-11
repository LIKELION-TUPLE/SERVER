package likelion.tupl.service;

import likelion.tupl.dto.CourseDto;
import likelion.tupl.entity.Course;
import likelion.tupl.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    // create course: 과외 추가
    public CourseDto createCourse(CourseDto courseDto) {

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
                .paymentCycle(courseDto.getPaymentCycle())
                .paymentDelayed(0)
                .totalLessonTime(0)
                .build();

        // course을 DB에 저장
        courseRepository.save(course);

        // return할 courseDto를 업데이트
        courseDto.setId(course.getId());

        return courseDto;
        //초대코드 로직 추가 필요
    }

    // delete course: 과외 삭제
    public void deleteCourse(Long courseId) {
        // Course ID가 있는지 체크
        if (courseRepository.existsById(courseId)) {
            // 있으면 삭제
            courseRepository.deleteById(courseId);
        } else {
            // 없는 Course ID를 입력한 경우
            throw new IllegalArgumentException("Course not found with ID: " + courseId);
        }
    }

    // update course: 과외 수정
    public CourseDto updateCourse(Long courseId, CourseDto updatedCourseDto) {
        // Course ID가 있는지 체크
        if (courseRepository.existsById(courseId)) {
            // DB에서 해당 course 가져오고,
            Course existingCourse = courseRepository.getById(courseId);

            // 업데이트된 내용으로 기존 엔티티 변경
            existingCourse.setColor(updatedCourseDto.getColor());
            existingCourse.setStudentName(updatedCourseDto.getStudentName());
            existingCourse.setStudentAge(updatedCourseDto.getStudentAge());
            existingCourse.setSchool(updatedCourseDto.getSchool());
            existingCourse.setStudentGrade(updatedCourseDto.getStudentGrade());
            existingCourse.setStudentPhone(updatedCourseDto.getStudentPhone());
            existingCourse.setParentPhone(updatedCourseDto.getParentPhone());
            existingCourse.setSubject(updatedCourseDto.getSubject());
            existingCourse.setCourseTime(updatedCourseDto.getCourseTime());
            existingCourse.setPaymentCycle(updatedCourseDto.getPaymentCycle());

            // 변경 후 저장
            courseRepository.save(existingCourse);

            // 업데이트한 정보 return
            return updatedCourseDto;
        } else {
            // 없는 Course ID를 입력한 경우
            throw new IllegalArgumentException("Course not found with ID: " + courseId);
        }
    }

    // get course by ID: 과외 조회
    public CourseDto getCourseById(Long courseId) {
        // Course ID가 있는지 체크
        if (courseRepository.existsById(courseId)) {
            // DB에서 해당 course 가져오고,
            Course course = courseRepository.getById(courseId);

            // Course 엔티티들을 Dto로 만들어서 return
            return CourseDto.builder()
                    .id(course.getId())
                    .color(course.getColor())
                    .studentName(course.getStudentName())
                    .studentAge(course.getStudentAge())
                    .school(course.getSchool())
                    .studentGrade(course.getStudentGrade())
                    .studentPhone(course.getStudentPhone())
                    .parentPhone(course.getParentPhone())
                    .subject(course.getSubject())
                    .courseTime(course.getCourseTime())
                    .paymentCycle(course.getPaymentCycle())
                    .paymentDelayed(course.getPaymentDelayed())
                    .totalLessonTime(course.getTotalLessonTime())
                    .build();
        } else {
            // 없는 Course ID를 입력한 경우
            throw new IllegalArgumentException("Course not found with ID: " + courseId);
        }
    }

}
