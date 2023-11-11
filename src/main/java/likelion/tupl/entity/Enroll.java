package likelion.tupl.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Enroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 의존 관계 매핑: member_id(FK)
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // 의존 관계 매핑: course_id(FK)
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
