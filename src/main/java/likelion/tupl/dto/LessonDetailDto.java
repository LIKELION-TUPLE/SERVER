package likelion.tupl.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class LessonDetailDto extends LessonDto {
    private String color; // 대표 색상
    private String school; // 학생 학교
    private Integer studentGrade; // 학생 학년
    private String subject; // 과목
    private String studentName; // 학생 이름
    private String teacherName; // 선생님 이름

    private List<HomeworkDto> homeworkForTodayList; // 오늘까지 숙제
    private List<HomeworkDto> homeworkForNextList; // 다음시간까지 숙제
}
