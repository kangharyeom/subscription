package dott.subscription.subscriptionHistory.service;

import dott.subscription.exception.BusinessLogicException;
import dott.subscription.exception.Exceptions;
import dott.subscription.member.entity.Member;
import dott.subscription.member.service.MemberService;
import dott.subscription.subscriptionHistory.entity.SubscriptionHistory;
import dott.subscription.subscriptionHistory.repository.SubscriptionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class SubscriptionHistoryService {
    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private final MemberService memberService;

    // 구독 & 구독 해지 이력 조회
    public List<SubscriptionHistory> getSubscriptionHistory(String phoneNumber){
        // 회원 인증
        Member member = memberService.findMemberByPhoneNumber(phoneNumber);

        // 구독 이력 확인 및 전체 조회
        return findAllSubscriptionHistoryByMember(member);
    }


    // 구독 이력 확인 및 전체 조회
    public List<SubscriptionHistory> findAllSubscriptionHistoryByMember(Member member){
        List<SubscriptionHistory> subscriptionHistory = subscriptionHistoryRepository.findAllByMember(member);
        if (subscriptionHistory.isEmpty()) {
            // 구독이력 존재하지 않을 경우
            throw new BusinessLogicException(Exceptions.SUBSCRIPTION_HISTORY_NOT_FOUND);
        } else {
            // 구독이력이 존재할 경우
            return subscriptionHistory;
        }
    }

}
