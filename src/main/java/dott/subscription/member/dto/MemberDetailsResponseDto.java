package dott.subscription.member.dto;

import dott.subscription.constant.SubscriptionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDetailsResponseDto {
    private long id;
    private String phoneNumber;
    private long channelId;
    private String channelName;
    private SubscriptionStatus subscriptionStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
