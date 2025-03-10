package dott.subscription.member.controller;

import dott.subscription.member.dto.*;
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
     * 회원 가입
     */
    @PostMapping("/create")
    public ResponseEntity<MemberResponseDto> createMember(@RequestBody @Validated MemberPostDto memberPostDto) {
        log.info("CREATE MEMBER START");
        log.debug("[MemberPostDto - createMember] : {}", memberPostDto.toString());

        // Dto to Entity 세팅
        Member member = memberMapper.memberPostDtoToMember(memberPostDto);
        log.debug("[MEMBER ENTITY - createMember] : {}", member.toString());

        // 회원 생성
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(memberService.createMember(member));
        log.debug("[MemberResponseDto - createMember] : {}", memberResponseDto.toString());

        log.info("CREATE MEMBER END");
        return new ResponseEntity(new SingleResponseDto<>(memberResponseDto), HttpStatus.CREATED);
    }

    /**
     * 회원 전화번호 변경
     */
    @PatchMapping("/update")
    public ResponseEntity<MemberResponseDto> updateMemberPhoneNumber(@RequestBody @Validated MemberPatchDto memberPatchDto) {
        log.info("UPDATE MEMBER's PHONE NUMBER START");

        // Dto to Entity 세팅
        Member member = memberMapper.memberPatchDtoToMember(memberPatchDto);
        log.debug("[MEMBER ENTITY - updateMemberPhoneNumber] : {}", member.toString());

        // 전화번호 변경
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(memberService.updatePhoneNumber(member));
        log.debug("[MemberResponseDto - updateMemberPhoneNumber] : {}", memberResponseDto.toString());

        log.info("UPDATE MEMBER's PHONE NUMBER END");
        return new ResponseEntity(new SingleResponseDto<>(memberResponseDto), HttpStatus.OK);
    }

    /**
     * 회원 상세 조회
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> getMemberDetails(@PathVariable long memberId) {
        log.info("UPDATE MEMBER's PHONE NUMBER START");

        // Dto to Entity 세팅
        Member member = memberService.findMemberByMemberId(memberId);
        log.debug("[MEMBER ENTITY - getMemberDetails] : {}", member.toString());

        // 전화번호 변경
        MemberDetailsResponseDto memberDetailsResponseDto = memberMapper.memberToMemberDetailsResponseDto(member);
        log.debug("[MemberDetailsResponseDto - getMemberDetails] : {}", memberDetailsResponseDto.toString());

        log.info("UPDATE MEMBER's PHONE NUMBER END");
        return new ResponseEntity(new SingleResponseDto<>(memberDetailsResponseDto), HttpStatus.OK);
    }

    /**
     * 회원 삭제
     */
    @DeleteMapping("/delete")
    public ResponseEntity<MemberResponseDto> deleteMember(@RequestBody @Validated MemberDeleteDto memberDeleteDto) {
        log.info("DELETE MEMBER START");

        // Dto to Entity 세팅
        Member member = memberMapper.memberDeleteDtoToMember(memberDeleteDto);

        // 회원 삭제
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(memberService.deleteMember(member));

        log.info("DELETE MEMBER END");
        return new ResponseEntity(new SingleResponseDto<>(memberResponseDto), HttpStatus.NO_CONTENT);
    }
}
