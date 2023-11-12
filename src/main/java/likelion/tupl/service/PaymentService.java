package likelion.tupl.service;

import likelion.tupl.dto.PaymentBlockDto;
import likelion.tupl.entity.Course;
import likelion.tupl.entity.Lesson;
import likelion.tupl.repository.CourseRepository;
import likelion.tupl.repository.HomeworkRepository;
import likelion.tupl.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    @Autowired
    private final LessonRepository lessonRepository;

    @Autowired
    private final CourseRepository courseRepository;

    // input받은 lessonId를 db에서 못찾았을 경우 고려한 함수
    private Lesson getLessonById(Long lessonId) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        return optionalLesson.orElseThrow(() -> new RuntimeException("Lesson not found with ID: " + lessonId));
    }

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

        // 입력 받은 courseId로 전체 lesson 가져오기
        List<Lesson> lessons = lessonRepository.findByCourseId(courseId);

        // courseId 불일치시 오류 반환
        if (lessons.isEmpty()) {
            throw new RuntimeException("Course not found with ID: \" + courseId");
        }

        // PaymentBlockDto 생성
        PaymentBlockDto paymentBlockDto = new PaymentBlockDto();
        paymentBlockDto.setCourseId((courseId));
        paymentBlockDto.setNoPaymentCount(payDel);

        if (payDel == 0) {
            // date1, date2, date3, date4, date5, date6에 null 설정
            paymentBlockDto.setDate1(null);
            paymentBlockDto.setDate2(null);
            paymentBlockDto.setDate3(null);
            paymentBlockDto.setDate4(null);
            paymentBlockDto.setDate5(null);
            paymentBlockDto.setDate6(null);

        } else if (payDel == 1) {
            // cycle의 첫번째, 마지막 날짜를 Dto에 저장
            paymentBlockDto.setDate1(lessons.get(payCycle*(lessons.size()/payCycle-1)).getDate());
            paymentBlockDto.setDate2(lessons.get(payCycle*(lessons.size()/payCycle)-1).getDate());

            // date3, date4, date5, date6에 null 설정
            paymentBlockDto.setDate3(null);
            paymentBlockDto.setDate4(null);
            paymentBlockDto.setDate5(null);
            paymentBlockDto.setDate6(null);

        } else if (payDel == 2) {
            // cycle 두 개의 첫번째, 마지막 날짜를 Dto에 저장
            paymentBlockDto.setDate1(lessons.get(payCycle*(lessons.size()/payCycle-2)).getDate());
            paymentBlockDto.setDate2(lessons.get(payCycle*(lessons.size()/payCycle-1)-1).getDate());
            paymentBlockDto.setDate3(lessons.get(payCycle*(lessons.size()/payCycle-1)).getDate());
            paymentBlockDto.setDate4(lessons.get(payCycle*(lessons.size()/payCycle)-1).getDate());

            // date5, date6에 null 설정
            paymentBlockDto.setDate5(null);
            paymentBlockDto.setDate6(null);

        } else if (payDel == 3) {
            // cycle 세 개의 첫번째, 마지막 날짜를 Dto에 저장
            paymentBlockDto.setDate1(lessons.get(payCycle*(lessons.size()/payCycle-3)).getDate());
            paymentBlockDto.setDate2(lessons.get(payCycle*(lessons.size()/payCycle-2)-1).getDate());          paymentBlockDto.setDate1(lessons.get(lessons.size()-payCycle*payDel--).getDate());
            paymentBlockDto.setDate3(lessons.get(payCycle*(lessons.size()/payCycle-2)).getDate());
            paymentBlockDto.setDate4(lessons.get(payCycle*(lessons.size()/payCycle-1)-1).getDate());
            paymentBlockDto.setDate5(lessons.get(payCycle*(lessons.size()/payCycle-1)).getDate());
            paymentBlockDto.setDate6(lessons.get(payCycle*(lessons.size()/payCycle)-1).getDate());
        }

        return paymentBlockDto;
    }

    /**
     * blocklist 생성
     */

    public List<PaymentBlockDto> createPaymentBlocks(Long memberId) {
        // 입력 받은 MemberId로 해당 Member가 가지고 있는 Course 모두 가져오기
        List<Course> courses = courseRepository.findAllByMemberId(memberId);

        // memberId 불일치시 오류 반환
        if (courses.isEmpty()) {
            throw new RuntimeException("Member not found with ID: " + memberId);
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