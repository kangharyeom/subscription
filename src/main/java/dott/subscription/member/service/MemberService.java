package dott.subscription.member.service;

import dott.subscription.exception.BusinessLogicException;
import dott.subscription.exception.Exceptions;
import dott.subscription.member.entity.Member;
import dott.subscription.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 회원가입
    public Member createMember(Member member) {
        // 중복된 전화번호인지 확인
        isPhoneNumberDuplicated(member.getPhone());

        log.info("MEMBER CREATE SUCCESS : {}", member);
        return memberRepository.save(member);
    }

    // 회원 전화번호 변경
    public Member updatePhoneNumber(Member member) {
        // 가입된 회원인지 확인
        isMemberExistByMemberId(member.getId());

        // 중복된 전화번호인지 확인
        isPhoneNumberDuplicated(member.getPhone());

        Optional.ofNullable(member.getPhone())
                .ifPresent(member::setPhone);

        log.info("PHONE NUMBER UPDATE SUCCESS : {}", member);
        return memberRepository.save(member);
    }

    // 회원 삭제
    public void deleteMember(Member member) {
        // 가입된 회원인지 확인
        isMemberExistByMemberId(member.getId());

        memberRepository.delete(member);
        log.info("MEMBER DELETE SUCCESS : {}", member);
    }

    // 전화번호 중복 확인 메서드
    private void isPhoneNumberDuplicated(String phoneNumber) {
        Optional<Member> phone = memberRepository.findByPhone(phoneNumber);
        if(phone.isPresent()) {
            throw new BusinessLogicException(Exceptions.PHONE_NUMBER_EXIST);
        }
    }

    // 회원 Id로 회원 조회
    private void isMemberExistByMemberId(long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        optionalMember.orElseThrow(() ->
                new BusinessLogicException(Exceptions.MEMBER_NOT_FOUND));
    }
}
