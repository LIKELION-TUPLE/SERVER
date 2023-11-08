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
    private String stu_name;
    private Integer stu_age;
    private Integer stu_grade;
    private String parent_phone;
    private String subject;
    private String course_time;
    private Integer payment_cycle;
    private Boolean want_alarmtalk;
    //왜안되지3
}
