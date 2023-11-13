package likelion.tupl.service;

import likelion.tupl.dto.CourseDto;
import likelion.tupl.dto.InviteCodeDto;
import likelion.tupl.dto.SimpleCourseDto;
import likelion.tupl.entity.Course;
import likelion.tupl.entity.Enroll;
import likelion.tupl.entity.Member;
import likelion.tupl.entity.Role;
import likelion.tupl.repository.CourseRepository;
import likelion.tupl.repository.EnrollRepository;
import likelion.tupl.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final EnrollRepository enrollRepository;
    private final MemberRepository memberRepository;

    // create course: 과외 추가
    public CourseDto createCourse(CourseDto courseDto) {

        //랜덤 초대코드(generatedCode) 생성 로직
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedCode = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

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
                .inviteCode(generatedCode)
                .build();

        // course을 DB에 저장
        courseRepository.save(course);

        // return할 courseDto를 업데이트
        courseDto.setId(course.getId());
        courseDto.setPaymentDelayed(course.getPaymentDelayed());
        courseDto.setTotalLessonTime(course.getTotalLessonTime());
        courseDto.setInviteCode(course.getInviteCode());

        // 로그인한 멤버 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Optional<Member> optionalMember = memberRepository.findOneByLoginId(username);
        Member member = optionalMember.get();

        // Enroll에 과외 정보 저장

        Enroll enroll = Enroll.builder()
                .course(course)
                .member(member)
                .build();
        enrollRepository.save(enroll);

        return courseDto;
    }

    // delete course: 과외 삭제
    public ResponseEntity<Map<String, Boolean>> deleteCourse(Long courseId) {
        // response 객체
        Map<String, Boolean> response = new HashMap<>();

        // Course ID가 있는지 체크
        if (courseRepository.existsById(courseId)) {
            // 있으면 삭제
            // Enroll에서 삭제
            List<Enroll> enrollList = enrollRepository.findByCourseId(courseId);
            for(int i = 0; i <enrollList.size(); i++){
                enrollRepository.delete(enrollList.get(i));
            }
            // course에서 삭제
            courseRepository.deleteById(courseId);

            // response
            response.put("Course-deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);

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

            // return할 courseDto를 업데이트
            updatedCourseDto.setId(existingCourse.getId());
            updatedCourseDto.setPaymentDelayed(existingCourse.getPaymentDelayed());
            updatedCourseDto.setTotalLessonTime(existingCourse.getTotalLessonTime());
            updatedCourseDto.setInviteCode(existingCourse.getInviteCode());

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
                    .inviteCode(course.getInviteCode())
                    .build();
        } else {
            // 없는 Course ID를 입력한 경우
            throw new IllegalArgumentException("Course not found with ID: " + courseId);
        }
    }

    // list all courses : 진행 중인 과외 리스트
    public List<CourseDto> CurrentCourses() {
        List<Course> courses = courseRepository.findAll();

        // Course 엔티티를 CourseDto로 변경
        return courses.stream()
                .map(course -> CourseDto.builder()
                        .color(course.getColor())
                        .studentName(course.getStudentName())
                        .school(course.getSchool())
                        .studentGrade(course.getStudentGrade())
                        .subject(course.getSubject())
                        .totalLessonTime(course.getTotalLessonTime())
                        .build())
                .collect(Collectors.toList());
    }

    // student create course: 로그인한 학생에게 초대 코드 받아서 과외 등록
    public ResponseEntity<Map<String, Boolean>> studentCreateCourse(InviteCodeDto inviteCodeDto) {

        // 등록할 과외 객체 불러오기
        Course course = courseRepository.findByInviteCode(inviteCodeDto.getInviteCode());

        // 과외 정보가 없으면 등록 X
        if (course == null) {
            Map<String, Boolean> response = new HashMap<>();
            response.put("Enroll-success", Boolean.FALSE);

            return ResponseEntity.ok(response);
        }
        else {
            // 로그인한 멤버 정보 가져오기
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            Optional<Member> optionalMember = memberRepository.findOneByLoginId(username);
            Member member = optionalMember.get();

            // Enroll에 과외 정보 저장

            Enroll enroll = Enroll.builder()
                    .course(course)
                    .member(member)
                    .build();
            enrollRepository.save(enroll);

            // 등록 완료 response
            Map<String, Boolean> response = new HashMap<>();
            response.put("Enroll-success", Boolean.TRUE);

            return ResponseEntity.ok(response);
        }
    }

    // course list for create lesson: 로그인한 선생님이 수업 일지 추가 시 선택할 수 있는 과외 리스트
    public List<SimpleCourseDto> courseListForCreateLesson() {
        // 로그인한 선생님 정보 가져옴
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;

        String username = userDetails.getUsername();
        Optional<Member> optionalMember = memberRepository.findOneByLoginId(username);
        Member teacher = optionalMember.get();

        // 선생님이 등록하고 있는 Enroll 가져옴
        List<Enroll> enrollList = enrollRepository.findByMemberId(teacher.getId());

        // 그 Enroll에 대한 CourseList 가져옴
        List<Course> courseList = new ArrayList<Course>();
        for (int i = 0; i < enrollList.size(); i++){
            Optional<Course> courseOptional = courseRepository.findById(enrollList.get(i).getCourse().getId());
            courseList.add(courseOptional.get());
        }

        // 그 Course에 대한 정보를 SimpleCourseDto에 담아서 내보냄
        List<SimpleCourseDto> simpleCourseDtoList = new ArrayList<SimpleCourseDto>();
        for (int i = 0; i < courseList.size(); i++) {
            Course course = courseList.get(i);
            simpleCourseDtoList.add(
                    SimpleCourseDto.builder()
                            .course_id(course.getId())
                            .color(course.getColor())
                            .studentName(course.getStudentName())
                            .school(course.getSchool())
                            .studentGrade(course.getStudentGrade())
                            .teacherName(teacher.getName())
                            .subject(course.getSubject())
                            .currentLessonTime(course.getTotalLessonTime() % course.getPaymentCycle() + 1)
                            .build()
            );
        }

        return simpleCourseDtoList;
    }

    // course list: 로그인한 유저(선생님/학생)가 등록한 과외 리스트
    public List<SimpleCourseDto> courseList() {
        // 로그인한 유저 정보 가져옴
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;

        String username = userDetails.getUsername();
        Optional<Member> optionalMember = memberRepository.findOneByLoginId(username);
        Member member = optionalMember.get();

        // 로그인한 유저가 등록하고 있는 Enroll 가져옴
        List<Enroll> enrollList = enrollRepository.findByMemberId(member.getId());

        // 그 Enroll에 대한 CourseList 가져옴
        List<Course> courseList = new ArrayList<Course>();
        for (int i = 0; i < enrollList.size(); i++){
            Optional<Course> courseOptional = courseRepository.findById(enrollList.get(i).getCourse().getId());
            courseList.add(courseOptional.get());
        }

        // 그 Course에 대한 정보를 SimpleCourseDto에 담아서 내보냄
        List<SimpleCourseDto> simpleCourseDtoList = new ArrayList<SimpleCourseDto>();
        for (int i = 0; i < courseList.size(); i++) {
            Course course = courseList.get(i);

            // teacherName 가져오기
            String teacherName = new String();
            if (member.getRole() == Role.ROLE_TEACHER) { // 로그인한 유저가 선생님이면, 선생님 이름 저장
                teacherName = member.getName();
            }
            else { // 로그인한 유저가 선생님이 아니면, Enroll에서 선생님 찾아옴
                List<Enroll> courseEnrollList = enrollRepository.findByCourseId(course.getId()); // 그 course에 대한 등록 정보 가져옴
                for (int j = 0; j < courseEnrollList.size(); j++) {
                    Enroll courseEnroll = courseEnrollList.get(j);
                    if (courseEnroll.getMember().getRole() == Role.ROLE_TEACHER) // 등록한 사람이 선생님이면 이름을 저장함
                        teacherName = courseEnroll.getMember().getName();
                }
            }

            // 현재 회차 계산
            int currentLessonTime;
            if (course.getTotalLessonTime() == 0) { // 전체 회차가 0이면 0
                currentLessonTime = 0;
            }
            else if (course.getTotalLessonTime() % course.getPaymentCycle() == 0) { // cycle의 배수면 cycle과 같음
                currentLessonTime = course.getPaymentCycle();
            }
            else { // 둘다 아니면 나머지
                currentLessonTime = course.getTotalLessonTime() % course.getPaymentCycle();
            }

            simpleCourseDtoList.add(
                    SimpleCourseDto.builder()
                            .course_id(course.getId())
                            .color(course.getColor())
                            .studentName(course.getStudentName())
                            .school(course.getSchool())
                            .studentGrade(course.getStudentGrade())
                            .teacherName(teacherName)
                            .subject(course.getSubject())
                            .currentLessonTime(currentLessonTime)
                            .build()
            );
        }

        return simpleCourseDtoList;
    }

}
