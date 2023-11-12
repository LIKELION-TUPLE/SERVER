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
    @GetMapping("/payment/{memberId}")
    public List<PaymentBlockDto> createPaymentBlocks(@PathVariable("memberId") Long memberId){
        return paymentService.createPaymentBlocks(memberId);
    }

    //course_id에 입금 완료 버튼이 눌렸을 때 -> paymentDelayed -1씩
    @PutMapping("/payment/{courseId}/complete")
    public void newPaymentDelayed(@PathVariable("courseId") Long courseId) {
        paymentService.newPaymentDelayed(courseId);
    }

}
