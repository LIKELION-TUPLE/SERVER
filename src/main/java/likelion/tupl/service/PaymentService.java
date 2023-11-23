package likelion.tupl.service;

import likelion.tupl.dto.PaymentBlockDto;
import likelion.tupl.entity.Course;
import likelion.tupl.entity.Lesson;
import likelion.tupl.entity.Member;
import likelion.tupl.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final MemberRepository memberRepository;

    // input받은 courseId를 db에서 못찾았을 경우 고려한 함수
    private Course getCourseById(Long courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        return optionalCourse.orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));
    }

    /**
     * block 생성
     */

    public PaymentBlockDto createPaymentBlock(Long courseId) {

        // 입력 받은 courseId로 course의 paymentCycle 값 가져오기
        int payCycle = getCourseById(courseId).getPaymentCycle();

        // 입력 받은 courseId로 course의 paymentDelayed 값 가져오기
        int payDel = getCourseById(courseId).getPaymentDelayed();

        // 입력 받은 courseId로 course의 색깔, 학생이름, 학생학교, 학생학년, 과목 값 가져오기
        String color = getCourseById(courseId).getColor(); // 색깔
        String studentName = getCourseById(courseId).getStudentName();// 학생 이름
        String studentSchool = getCourseById(courseId).getStudentSchool(); // 학생 학교
        int studentGrade = getCourseById(courseId).getStudentGrade(); // 학생 학년
        String subject = getCourseById(courseId).getSubject(); // 과목
        int coursePayment = getCourseById(courseId).getCoursePayment(); // 금액

        // 입력 받은 courseId로 전체 lesson 가져오기
        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByDate(courseId);

        // lesson이 비어있으면 null 반환
        if (lessons.isEmpty()) {
            return null;
        } else {
            // PaymentBlockDto 생성
            PaymentBlockDto paymentBlockDto = new PaymentBlockDto();
            paymentBlockDto.setCourseId((courseId));
            paymentBlockDto.setPaymentCycle(payCycle);
            paymentBlockDto.setColor(color);
            paymentBlockDto.setStudentName(studentName);
            paymentBlockDto.setStudentSchool(studentSchool);
            paymentBlockDto.setStudentGrade(studentGrade);
            paymentBlockDto.setSubject(subject);
            paymentBlockDto.setCoursePayment(coursePayment);
            paymentBlockDto.setNoPaymentCount(payDel);

            if (payDel == 0) {
                // delayed null 설정
                paymentBlockDto.setDates(Collections.emptyList());

            } else {
                // cycle의 첫번째 날짜를 delayed 개수만큼 리스트화
                List<Date> startDates = lessons.stream().filter(h -> h.getCurrentLessonTime() == 1).limit(payDel).map(Lesson::getDate).collect(Collectors.toList());
                Collections.reverse(startDates); // 리스트를 역순으로 변경
                paymentBlockDto.setDates(startDates);

            }

            return paymentBlockDto;

        }


    }

    /**
     * blocklist 생성
     */

    public List<PaymentBlockDto> createPaymentBlocks() {

        // 로그인한 유저 정보 가져옴
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;

        String username = userDetails.getUsername();
        Optional<Member> optionalMember = memberRepository.findOneByLoginId(username);
        Member member = optionalMember.get();

        // 입력 받은 MemberId로 해당 Member가 가지고 있는 Course 모두 가져오기
        System.out.println(member.getId());
        List<Course> courses = courseRepository.findAllByMemberId(member.getId());

        // memberId 불일치시 오류 반환
        if (courses.isEmpty()) {
            throw new RuntimeException("Member not found with ID: " + member.getId());
        }

        // Course 엔터티에서 id만 추출하여 리스트화
        List<Long> courseIds = courses.stream()
                .map(Course::getId)
                .collect(Collectors.toList());

        List<PaymentBlockDto> paymentBlockDtos = courseIds.stream()
                .map(this::createPaymentBlock) // 각 courseId에 대해 createPaymentBlock 호출
                .collect(Collectors.toList());

        return paymentBlockDtos;
    }

    /**
     * 입금 확인 눌렀을 때 paymentDelayed 변수 수정
     */

    public Integer newPaymentDelayed(Long courseId) {
        // 입력 받은 CourseId로 course의 paymentDelayed 값 가져오기 (course 없으면 오류 반환)
        Course course = getCourseById(courseId);
        int payDel = course.getPaymentDelayed();

        // 새로운 paymentDelayed 값 계산
        int newPayDel = payDel - 1;

        // course 엔터티에 새로운 paymentDelayed 값 설정
        course.setPaymentDelayed(newPayDel);

        // course 엔터티를 저장하여 데이터베이스 업데이트
        courseRepository.save(course);

        return newPayDel;

    }

}
