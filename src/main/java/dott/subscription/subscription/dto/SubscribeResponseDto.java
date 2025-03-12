package dott.subscription.subscription.dto;

import dott.subscription.constant.SubscriptionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeResponseDto {
    private long id;
    private SubscriptionStatus subscriptionStatus;
}
