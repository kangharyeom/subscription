package dott.subscription.member.mapper;

import dott.subscription.channel.entity.Channel;
import dott.subscription.constant.SubscriptionStatus;
import dott.subscription.member.dto.*;
import dott.subscription.member.entity.Member;
import dott.subscription.subscription.entity.Subscription;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDtoToMember(MemberPostDto memberPostDto);
    Member memberPatchDtoToMember(MemberPatchDto memberPatchDto);
    Member memberDeleteDtoToMember(MemberDeleteDto memberDeleteDto);

    MemberResponseDto memberToMemberResponseDto(Member member);
    default MemberDetailsResponseDto memberToMemberDetailsResponseDto(Member member){
        Long channelId = Optional.ofNullable(member.getSubscription())
                .map(Subscription::getChannel)
                .map(Channel::getId)
                .orElse(null);
        String channelName = Optional.ofNullable(member.getSubscription())
                .map(Subscription::getChannel)
                .map(Channel::getName)
                .orElse(null);
        SubscriptionStatus subscriptionStatus = Optional.ofNullable(member.getSubscription())
                .map(Subscription::getSubscriptionStatus)
                .orElse(null);

        return MemberDetailsResponseDto.builder()
                .id(member.getId())
                .phoneNumber(member.getPhoneNumber())
                .channelId(channelId)
                .channelName(channelName)
                .subscriptionStatus(subscriptionStatus)
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .build();
    }
}
