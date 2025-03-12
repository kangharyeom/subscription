package dott.subscription.subscription.mapper;

import dott.subscription.subscription.dto.SubscribeResponseDto;
import dott.subscription.subscription.entity.Subscription;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    SubscribeResponseDto subscriptionToSubscribeResponseDto(Subscription subscription);

}
