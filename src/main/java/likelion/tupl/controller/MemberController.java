package likelion.tupl.controller;

import likelion.tupl.dto.JwtDto;
import likelion.tupl.dto.LoginDto;
import likelion.tupl.dto.LoginIdDto;
import likelion.tupl.dto.MemberDto;
import likelion.tupl.jwt.JwtFilter;
import likelion.tupl.jwt.JwtProvider;
import likelion.tupl.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

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

    @PostMapping("/signup/check")
    public ResponseEntity<DuplicateDto> duplicateLoginId(
            @Valid @RequestBody LoginIdDto loginIdDto
    ){
        DuplicateDto dto = new DuplicateDto(memberService.duplicateLoginId(loginIdDto));
        return ResponseEntity.ok(dto);
    }

    @Data
    @AllArgsConstructor
    static class DuplicateDto{
        private String isDuplicated;
    }
    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(
            @Valid @RequestBody LoginDto loginDto
    ){
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        JwtDto jwtDto = JwtDto.builder()
                .loginId(authentication.getName())
                .name(memberService.findName(authentication.getName()))
                .token(jwt)
                .build();


        // body, header, status
        return new ResponseEntity<>(jwtDto, httpHeaders, HttpStatus.OK);
    }
}
