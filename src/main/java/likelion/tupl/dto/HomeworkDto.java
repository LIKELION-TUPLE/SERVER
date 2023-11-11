package likelion.tupl.dto;

import likelion.tupl.entity.Lesson;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class HomeworkDto {
    private Long id;
    private Boolean completed;
    private String homeworkContent;
    private Long lessonId; // 연결된 Lesson의 ID(FK)
}
