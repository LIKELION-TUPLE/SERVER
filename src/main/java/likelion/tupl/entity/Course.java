package likelion.tupl.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String color;
    private String studentName;
    private Integer studentAge;
    private Integer studentGrade;
    private String parentPhone;
    private String subject;
    private Integer courseTime;
    private Integer paymentCycle; // 입금 회차
    private Integer paymentDelayed;
    private Boolean wantAlarmTalk;
    private Integer totalLessonTime; // 전체 회차
}
