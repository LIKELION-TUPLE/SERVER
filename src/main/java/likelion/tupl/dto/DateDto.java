package likelion.tupl.dto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor

public class DateDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date; // 수업 진행 날짜(년-월-일)
}
