package dott.subscription.member.mapper;

import dott.subscription.member.dto.MemberDeleteDto;
import dott.subscription.member.dto.MemberPatchDto;
import dott.subscription.member.dto.MemberPostDto;
import dott.subscription.member.dto.MemberResponseDto;
import dott.subscription.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDtoToMember(MemberPostDto memberPostDto);
    Member memberPatchDtoToMember(MemberPatchDto memberPatchDto);
    Member memberDeleteDtoToMember(MemberDeleteDto memberDeleteDto);

    MemberResponseDto memberToMemberResponseDto(Member member);
}
