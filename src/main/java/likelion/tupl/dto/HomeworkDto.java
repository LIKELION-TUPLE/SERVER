package likelion.tupl.dto;

import likelion.tupl.entity.Lesson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class HomeworkDto {
    private Long id;
    private Boolean completed;
    private String homeworkContent;
}
