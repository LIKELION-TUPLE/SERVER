package likelion.tupl.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
public class DateLessonDto extends SimpleCourseDto {
    private LocalTime startTime; // 시작 시간
    private LocalTime endTime; // 종료 시간
    private Long lesson_id; // lesson id
}
