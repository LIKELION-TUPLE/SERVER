package likelion.tupl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import likelion.tupl.entity.Member;
import likelion.tupl.entity.Role;
import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String loginId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String phone;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String birthDate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Role role;

    public static MemberDto toEntity(Member member){
        return MemberDto.builder()
                .loginId(member.getLoginId())
                .name(member.getName())
                .phone(member.getPhone())
                .birthDate(member.getBirthDate())
                .role(member.getRole())
                .build();
    }
}
