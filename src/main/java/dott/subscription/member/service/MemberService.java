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
    public Member createMember(Member member) {
        isMemberExistByPhoneNumber(member.getPhone());

        log.info("MEMBER CREATE SUCCESS : {}", member);
        return memberRepository.save(member);
    }

    private void isMemberExistByPhoneNumber(String phoneNumber) {
        Optional<Member> phone = memberRepository.findByPhone(phoneNumber);
        if(phone.isPresent()) {
            throw new BusinessLogicException(Exceptions.PHONE_NUMBER_EXIST);
        }
    }
}
