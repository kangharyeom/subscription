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
        SubscriptionStatus subscriptionStatus = Optional.ofNullable(member.getSubscription())
                .map(Subscription::getSubscriptionStatus)
                .orElse(SubscriptionStatus.NONE);

        return MemberDetailsResponseDto.builder()
                .id(member.getId())
                .phoneNumber(member.getPhoneNumber())
                .subscriptionStatus(subscriptionStatus)
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .build();
    }
}
