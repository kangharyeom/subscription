package dott.subscription.subscription.dto;

import dott.subscription.constant.SubscriptionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class SubscribeDto {
    @NotBlank(message = "전화번호는 공백이 아니어야 합니다.")
    @Length(min = 11, max = 11, message = "전화번호는 11자리 입니다.")
    @Pattern(regexp = "^01(?:0|1|[6-9])\\d{3,4}\\d{4}$", message = "ds")
    private String phoneNumber;
    private long channelId;
    private SubscriptionStatus subscriptionStatus;

}
