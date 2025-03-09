package dott.subscription.subscription.dto;

import dott.subscription.constant.SubscriptionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class SubscribeResponseDto {
    private String phoneNumber;
    private long channelId;
    private String channelName;
    private SubscriptionStatus subscriptionStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
