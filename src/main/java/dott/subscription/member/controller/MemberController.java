package dott.subscription.member.controller;

import dott.subscription.member.dto.MemberPatchDto;
import dott.subscription.member.dto.MemberPostDto;
import dott.subscription.member.dto.MemberResponseDto;
import dott.subscription.member.entity.Member;
import dott.subscription.member.mapper.MemberMapper;
import dott.subscription.member.service.MemberService;
import dott.subscription.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @PostMapping("/create")
    public ResponseEntity<MemberResponseDto> createMember(@RequestBody @Validated MemberPostDto memberPostDto) {
        Member member = new Member();
        member = memberMapper.memberPostDtoToMember(memberPostDto);
        log.debug("[MEMBER ENTITY] : {}", member);

        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto = memberMapper.memberToMemberResponseDto(memberService.createMember(member));

        return new ResponseEntity(new SingleResponseDto<>(memberResponseDto),HttpStatus.CREATED);
    }

    @PatchMapping("/patch")
    public ResponseEntity<MemberResponseDto> updateMemberPhoneNumber(@RequestBody @Validated MemberPatchDto memberPatchDto) {
        MemberResponseDto memberResponseDto = new MemberResponseDto();

        return new ResponseEntity(new SingleResponseDto<>(memberResponseDto),HttpStatus.OK);
    }

    @PatchMapping("/delete")
    public ResponseEntity<MemberResponseDto> deleteMember(@RequestBody @Validated MemberPatchDto memberPatchDto) {
        MemberResponseDto memberResponseDto = new MemberResponseDto();

        return new ResponseEntity(new SingleResponseDto<>(memberResponseDto),HttpStatus.CREATED);
    }
}
