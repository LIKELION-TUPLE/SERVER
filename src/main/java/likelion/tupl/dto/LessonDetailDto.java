package likelion.tupl.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class LessonDetailDto extends LessonDto {
    private List<HomeworkDto> homeworkForTodayList; // 오늘까지 숙제
    private List<HomeworkDto> homeworkForNextList; // 다음시간까지 숙제
}
