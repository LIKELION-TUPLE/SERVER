package likelion.tupl.dto;

import likelion.tupl.entity.DOW;
import lombok.*;

import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentBlockDto {
    private Long courseId;
    private Integer noPaymentCount;
    private Date date1; // 수업 진행 날짜(년-월-일)
    private Date date2; // 수업 진행 날짜(년-월-일)
    private Date date3; // 수업 진행 날짜(년-월-일)
    private Date date4; // 수업 진행 날짜(년-월-일)
    private Date date5; // 수업 진행 날짜(년-월-일)
    private Date date6; // 수업 진행 날짜(년-월-일)

}