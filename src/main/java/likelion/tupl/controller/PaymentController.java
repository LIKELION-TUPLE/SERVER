package likelion.tupl.controller;

import likelion.tupl.dto.PaymentBlockDto;
import likelion.tupl.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    //멤버 아이디 받았을 때 -> block list에 course id 포함해서 건네주기
    @GetMapping("/payment")
    public List<PaymentBlockDto> createPaymentBlocks(){
        return paymentService.createPaymentBlocks();
    }

    //course_id에 입금 완료 버튼이 눌렸을 때 -> paymentDelayed -1씩
    @PutMapping("/payment/{courseId}/complete")
    public Integer newPaymentDelayed(@PathVariable("courseId") Long courseId) {
        return paymentService.newPaymentDelayed(courseId);
    }

}
