package likelion.tupl.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
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
    private String courseTime;
    private Integer paymentCycle;
    private Integer paymentDelayed;
    private Boolean wantAlarmTalk;
}
