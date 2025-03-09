package dott.subscription.subscription.mapper;

import dott.subscription.subscription.dto.SubscribeResponseDto;
import dott.subscription.subscription.entity.Subscription;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    default SubscribeResponseDto subscriptionToSubscribeResponseDto(Subscription subscription){

        return SubscribeResponseDto.builder()
                .phoneNumber(subscription.getMember().getPhoneNumber())
                .channelId(subscription.getChannel().getId())
                .channelName(subscription.getChannel().getName())
                .createdAt(subscription.getCreatedAt())
                .modifiedAt(subscription.getModifiedAt())
                .build();
    }
}
