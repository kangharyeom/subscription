package dott.subscription.member.controller;

import dott.subscription.member.dto.MemberDeleteDto;
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

    /**
     * 회원 가입 기능
     */
    @PostMapping("/create")
    public ResponseEntity<MemberResponseDto> createMember(@RequestBody @Validated MemberPostDto memberPostDto) {
        log.info("CREATE MEMBER START");

        // Dto to Entity 세팅
        Member member = memberMapper.memberPostDtoToMember(memberPostDto);
        log.debug("[MEMBER ENTITY - createMember] : {}", member);

        // 회원 생성
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(memberService.createMember(member));
        log.debug("[MemberResponseDto - createMember] : {}", memberResponseDto.toString());

        log.info("CREATE MEMBER END");
        return new ResponseEntity(new SingleResponseDto<>(memberResponseDto), HttpStatus.CREATED);
    }

    /**
     * 회원 전화번호를 변경하는 기능
     */
    @PatchMapping("/patch")
    public ResponseEntity<MemberResponseDto> updateMemberPhoneNumber(@RequestBody @Validated MemberPatchDto memberPatchDto) {
        log.info("UPDATE MEMBER's PHONE NUMBER START");

        // Dto to Entity 세팅
        Member member = memberMapper.memberPatchDtoToMember(memberPatchDto);
        log.debug("[MEMBER ENTITY - updateMemberPhoneNumber] : {}", member);

        // 전화번호 변경
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(memberService.updatePhoneNumber(member));
        log.debug("[MemberResponseDto - updateMemberPhoneNumber] : {}", memberResponseDto.toString());

        log.info("UPDATE MEMBER's PHONE NUMBER END");
        return new ResponseEntity(new SingleResponseDto<>(memberResponseDto), HttpStatus.OK);
    }

    /**
     * 회원 삭제 기능
     */
    @PatchMapping("/delete")
    public ResponseEntity<MemberResponseDto> deleteMember(@RequestBody @Validated MemberDeleteDto memberDeleteDto) {
        log.info("DELETE MEMBER START");

        // Dto to Entity 세팅
        Member member = memberMapper.memberDeleteDtoToMember(memberDeleteDto);

        // 회원 삭제
        memberService.deleteMember(member);

        log.info("DELETE MEMBER END");
        return new ResponseEntity(new SingleResponseDto<>(member), HttpStatus.CREATED);
    }
}
