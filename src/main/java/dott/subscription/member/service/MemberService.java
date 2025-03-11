package dott.subscription.member.service;

import dott.subscription.exception.BusinessLogicException;
import dott.subscription.exception.Exceptions;
import dott.subscription.member.entity.Member;
import dott.subscription.member.repository.MemberRepository;
import dott.subscription.subscription.repository.SubscriptionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final EntityManager entityManager;

    // 회원가입
    public Member createMember(Member member) {
        // 중복된 전화번호인지 확인
        isPhoneNumberDuplicated(member.getPhoneNumber());

        log.info("MEMBER CREATE SUCCESS : {}", member.toString());
        return memberRepository.save(member);
    }

    // 회원 전화번호 변경
    @Transactional
    public Member updatePhoneNumber(Member member) {
        // 가입된 회원인지 확인 및 비관적락 적용
        Member findMember = entityManager.find(Member.class, member.getId(), LockModeType.PESSIMISTIC_WRITE);

        if (findMember == null) {
            throw new BusinessLogicException(Exceptions.MEMBER_NOT_FOUND);
        }
        // 중복된 전화번호인지 확인
        isPhoneNumberDuplicated(member.getPhoneNumber());

        Optional.ofNullable(member.getPhoneNumber())
                .ifPresent(findMember::setPhoneNumber);

        memberRepository.save(findMember);
        log.info("PHONE NUMBER UPDATE SUCCESS : {}", findMember.toString());
        return findMember;
    }

    // 회원 삭제
    @Transactional
    public Member deleteMember(Member member) {
        // 가입된 회원인지 확인 및 비관적락 적용
        Member findMember = entityManager.find(Member.class, member.getPhoneNumber(), LockModeType.PESSIMISTIC_WRITE);
        if (findMember == null) {
            throw new BusinessLogicException(Exceptions.MEMBER_NOT_FOUND);
        }
        //멤버와 연관된 구독정보 삭제
        subscriptionRepository.deleteByMember(findMember);

        memberRepository.delete(findMember);
        log.info("MEMBER DELETE SUCCESS");
        return findMember;
    }

    // 전화번호 중복 확인 메서드
    private void isPhoneNumberDuplicated(String phoneNumber) {
        Optional<Member> phone = memberRepository.findByPhoneNumber(phoneNumber);
        if(phone.isPresent()) {
            throw new BusinessLogicException(Exceptions.PHONE_NUMBER_EXIST);
        }
    }

    // 회원 Id로 회원 조회
    public Member findMemberByMemberId(long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if (optionalMember.isPresent()) {
            // 회원이 존재할 경우
            return optionalMember.get();
        } else {
            // 회원이 존재하지 않을 경우
            throw new BusinessLogicException(Exceptions.MEMBER_NOT_FOUND);
        }
    }

    // 전화번호로 회원 조회
    public Member findMemberByPhoneNumber(String phoneNumber) {
        Optional<Member> optionalMember = memberRepository.findByPhoneNumber(phoneNumber);

        if (optionalMember.isPresent()) {
            // 회원이 존재할 경우
            return optionalMember.get();
        } else {
            // 회원이 존재하지 않을 경우
            throw new BusinessLogicException(Exceptions.MEMBER_NOT_FOUND);
        }
    }
}
