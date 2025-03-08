package dott.subscription.member.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MemberPatchDto {
    @NotNull
    private long id;

    @NotBlank(message = "전화번호는 공백이 아니어야 합니다.")
    @Length(min = 11, max = 11, message = "전화번호는 11자리 입니다.")
    @Pattern(regexp = "^01(?:0|1|[6-9])\\d{3,4}\\d{4}$", message = "ds")
    private String phoneNumber;

}