package likelion.tupl.dto;

import likelion.tupl.entity.Course;
import likelion.tupl.entity.DOW;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class LessonDto {
    private Long id;
    private Date date; // 수업 진행 날짜(년-월-일)
    private LocalTime startTime; // 시작 시간
    private LocalTime endTime; // 종료 시간
    private DOW dow; // 요일
    private String place; // 장소
    private String studyContent; // 오늘 나간 진도
    private Integer currentLessonTime;// 현재 회차
    private Long courseId; // 연결된 Course의 ID(FK)
}
