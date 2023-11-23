package likelion.tupl.dto;
import lombok.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentBlockDto {
    private Long courseId;
    private Integer paymentCycle;
    private String color; // 색깔
    private String studentName;// 학생 이름
    private String studentSchool; // 학생 학교
    private Integer studentGrade; // 학생 학년
    private String subject; // 과목
    private Integer coursePayment;
    private Integer noPaymentCount;
    private List<Date> dates;

}
