package likelion.tupl.dto;

import likelion.tupl.entity.Course;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id; // Course ID

    @NotNull(message = "대표 색상은 Null 일 수 없습니다!")
    private String color; // 대표 색상

    @NotNull(message = "이름은 Null 일 수 없습니다!")
    @Size(min = 1, max = 6, message = "이름은 1 ~ 6자 이여야 합니다!")
    private String studentName; // 학생 이름

    @NotNull(message = "나이는 Null 일 수 없습니다!")
    private Integer studentAge; // 학생 나이

    @NotNull(message = "학교는 Null 일 수 없습니다!")
    private String studentSchool; // 학생 학교

    @NotNull(message = "학년은 Null 일 수 없습니다!")
    private Integer studentGrade; // 학생 학년

    private String studentPhone; // 학생 폰 번호
    private String parentPhone; // 학부모 폰 번호

    @NotNull(message = "과목은 Null 일 수 없습니다!")
    @Size(min = 1, max = 6, message = "과목는 1 ~ 6자 이여야 합니다!")
    private String subject; // 과외 과목

    @NotNull(message = "수업 시간은 Null 일 수 없습니다!")
    private Integer courseTime; // 수업 시간 (분)

    @NotNull(message = "과외비는 Null 일 수 없습니다!")
    private Integer coursePayment; // 과외비

    @NotNull(message = "입금 회차는 Null 일 수 없습니다!")
    private Integer paymentCycle; // 입금 회차

    private Integer paymentDelayed; // 밀린 입금 횟수
    //    private Boolean wantAlarmTalk; // 알림톡 동의 여부 - 추후 추가
    private Integer totalLessonTime; // 전체 회차

    private String inviteCode; //초대 코드
}
