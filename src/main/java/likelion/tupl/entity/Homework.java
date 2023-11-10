package likelion.tupl.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Homework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("0")
    private Boolean completed;

    private String homeworkContent;

    // 의존 관계 매핑: lesson_id(FK)
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
