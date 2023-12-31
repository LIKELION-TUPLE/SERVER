package likelion.tupl.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtDto {

    @NotNull
    private String loginId;

    @NotNull
    private String name;

    @NotNull
    private String role;

    @NotNull
    private String token;
}
