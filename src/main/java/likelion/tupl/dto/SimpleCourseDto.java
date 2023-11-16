package likelion.tupl.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
public class SimpleCourseDto {
    private Long course_id;
    private String color; // 색깔
    private String studentName;// 학생 이름
    private String school; // 학생 학교
    private Integer studentGrade; // 학생 학년
    private String teacherName; // 선생님 이름
    private String subject; // 과목
    private Integer currentLessonTime; // 회차
}
