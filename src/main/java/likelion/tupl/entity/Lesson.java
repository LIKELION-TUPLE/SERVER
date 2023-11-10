package likelion.tupl.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date; // 수업 진행 날짜(년-월-일)

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime; // 시작 시간

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime; // 종료 시간

    private DOW dow; // 요일
    private String place; // 장소
    private String studyContent; // 오늘 나간 진도
    private Integer currentLessonTime;// 현재 회차
    private Integer totalLessonTime; // 전체 회차

    // 의존 관계 매핑: course_id(FK)
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
