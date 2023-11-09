package likelion.tupl.controller;

import likelion.tupl.dto.MemberDto;
import likelion.tupl.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup/teacher")
    public ResponseEntity<MemberDto> teacherSignUp(
            @Valid @RequestBody MemberDto memberDto
    ){
        return ResponseEntity.ok(memberService.teacherSignUp(memberDto));
    }

    @PostMapping("/signup/student")
    public ResponseEntity<MemberDto> studentSignUp(
            @Valid @RequestBody MemberDto memberDto
    ){
        return ResponseEntity.ok(memberService.studentSignUp(memberDto));
    }


}
