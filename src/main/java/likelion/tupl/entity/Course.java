package likelion.tupl.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Course ID

    private String color; // 대표 색상
    private String studentName; // 학생 이름
    private Integer studentAge; // 학생 나이
    private String school; // 학생 학교
    private Integer studentGrade; // 학생 학년
    private String studentPhone; // 학생 폰 번호
    private String parentPhone; // 학부모 폰 번호
    private String subject; // 과외 과목
    private Integer courseTime; // 수업 시간
    private Integer paymentCycle; // 입금 회차

    private Integer paymentDelayed; // 밀린 입금 횟수
    //  private Boolean wantAlarmTalk; // 알림톡 동의 여부 - 추후 추가
    private Integer totalLessonTime; // 전체 회차

    private String inviteCode; //초대 코드
}
