package likelion.tupl.dto;

import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class InviteCodeDto {
    private String inviteCode; //초대 코드
}
