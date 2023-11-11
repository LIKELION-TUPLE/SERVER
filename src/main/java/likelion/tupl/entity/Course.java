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
    private Integer courseTime; // 수업 시간
    private Integer paymentCycle; // 입금 회차 (몇 회마다 입금 받을건지)
    private Integer paymentDelayed; // 입금 필요한지 여부
    private Integer totalLessonTime; // 전체 회차 (지금까지 총 몇 회 수업했는지)
}
