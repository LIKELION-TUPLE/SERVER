package likelion.tupl.service;

import likelion.tupl.dto.LoginIdDto;
import likelion.tupl.dto.MemberDto;
import likelion.tupl.entity.Member;
import likelion.tupl.entity.Role;
import likelion.tupl.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberDto studentSignUp(MemberDto memberDto){
        if (memberRepository.existsByLoginId(memberDto.getLoginId())){
            throw new RuntimeException("이미 가입된 유저입니다.");
        }

        Member member = Member.builder()
                .loginId(memberDto.getLoginId())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .name(memberDto.getName())
                .phone(memberDto.getPhone())
                .birthDate(memberDto.getBirthDate())
                .role(Role.ROLE_STUDENT)
                .build();

        return MemberDto.toEntity(memberRepository.save(member));
    }

    public MemberDto teacherSignUp(MemberDto memberDto){
        if (memberRepository.findOneByLoginId(memberDto.getLoginId()).orElse(null) != null){
            throw new RuntimeException("이미 가입된 유저입니다.");
        }

        Member member = Member.builder()
                .loginId(memberDto.getLoginId())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .name(memberDto.getName())
                .phone(memberDto.getPhone())
                .birthDate(memberDto.getBirthDate())
                .role(Role.ROLE_TEACHER)
                .build();

        return MemberDto.toEntity(memberRepository.save(member));
    }

    // 로그인 아이디 중복 체크
    public String duplicateLoginId(LoginIdDto loginIdDto){
        if (memberRepository.findOneByLoginId(loginIdDto.getLoginId()).orElse(null) != null){
            return "true";
        }
        else return "false";
    }
}
