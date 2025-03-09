package dott.subscription.member.mapper;

import dott.subscription.member.dto.*;
import dott.subscription.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDtoToMember(MemberPostDto memberPostDto);
    Member memberPatchDtoToMember(MemberPatchDto memberPatchDto);
    Member memberDeleteDtoToMember(MemberDeleteDto memberDeleteDto);

    MemberResponseDto memberToMemberResponseDto(Member member);
    default MemberDetailsResponseDto memberToMemberDetailsResponseDto(Member member){

        return MemberDetailsResponseDto.builder()
                .id(member.getId())
                .phoneNumber(member.getPhoneNumber())
                .channelId(member.getSubscription().getChannel().getId())
                .channelName(member.getSubscription().getChannel().getName())
                .subscriptionStatus(member.getSubscription().getSubscriptionStatus())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .build();
    }
}
