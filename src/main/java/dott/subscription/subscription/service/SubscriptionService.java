package dott.subscription.subscription.service;

import com.fasterxml.jackson.databind.JsonNode;
import dott.subscription.channel.entity.Channel;
import dott.subscription.channel.service.ChannelService;
import dott.subscription.constant.SubscriptionStatus;
import dott.subscription.exception.BusinessLogicException;
import dott.subscription.exception.Exceptions;
import dott.subscription.member.entity.Member;
import dott.subscription.member.service.MemberService;
import dott.subscription.subscription.dto.SubscribeDto;
import dott.subscription.subscription.entity.Subscription;
import dott.subscription.subscription.repository.SubscriptionRepository;
import dott.subscription.subscriptionHistory.entity.SubscriptionHistory;
import dott.subscription.subscriptionHistory.repository.SubscriptionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final MemberService memberService;
    private final SubscriptionRepository subscriptionRepository;
    private final ChannelService channelService;
    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private final RestTemplate restTemplate;

    private static final String RANDOM_API_URL = "https://csrng.net/csrng/csrng.php?min=0&max=1";

    // 구독 기능
    public Subscription subscribe(SubscribeDto subscribeDto) {
        // 회원 인증
        Member member = memberService.isMemberExistByPhoneNumber(subscribeDto.getPhoneNumber());

        // 채널 유무 확인
        Channel channel = channelService.isChannelExistByChannelId(subscribeDto.getChannelId());

        // 구독 여부 확인
        Subscription subscription = subscriptionRepository.findByMember(member)
                .orElse(new Subscription(member, SubscriptionStatus.NONE));

        SubscriptionStatus previousSubscriptionStatus = subscription.getSubscriptionStatus();
        SubscriptionStatus newSubscriptionStatus = subscribeDto.getSubscriptionStatus();

        // 구독 가능 여부 확인
        validateSubscriptionTransition(previousSubscriptionStatus, newSubscriptionStatus);

        // 외부 API 통신
        if (!callExternalApi()) {
            throw new BusinessLogicException(Exceptions.SUBSCRIPTION_API_CONNECTION_ERROR);
        }

        // 구독 정보 최신화
        subscription.setSubscriptionStatus(subscribeDto.getSubscriptionStatus());
        subscriptionRepository.save(subscription);

        // 구독 이력 생성
        subscriptionHistoryRepository.save(new SubscriptionHistory(null, member.getPhoneNumber(), channel.getName(), member, channel, previousSubscriptionStatus, subscribeDto.getSubscriptionStatus()));

        log.info("SUBSCRIBE SUCCESS : {}", subscription);
        return subscription;
    }

    // 구독해지 기능
    public Subscription unsubscribe(SubscribeDto subscribeDto) {
        // 회원 인증
        Member member = memberService.isMemberExistByPhoneNumber(subscribeDto.getPhoneNumber());

        // 채널 유무 확인
        Channel channel = channelService.isChannelExistByChannelId(subscribeDto.getChannelId());

        // 구독 해지 여부 확인
        Subscription subscription = isSubscribingMember(member);

        SubscriptionStatus previousSubscriptionStatus = subscription.getSubscriptionStatus();
        SubscriptionStatus newSubscriptionStatus = subscribeDto.getSubscriptionStatus();

        // 구독 해지 가능 여부 확인 //
        validateUnSubscriptionTransition(previousSubscriptionStatus, newSubscriptionStatus);

        // 외부 API 통신
        if (!callExternalApi()) {
            throw new BusinessLogicException(Exceptions.SUBSCRIPTION_API_CONNECTION_ERROR);
        }

        subscription.setSubscriptionStatus(newSubscriptionStatus);
        subscriptionRepository.save(subscription);

        // 구독 해지 이력 생성
        LocalDateTime unsubscribeTime = LocalDateTime.now();
        subscriptionHistoryRepository.save(new SubscriptionHistory(null, member.getPhoneNumber(), channel.getName(), member, channel, previousSubscriptionStatus, subscribeDto.getSubscriptionStatus(), unsubscribeTime));

        log.info("UNSUBSCRIBE SUCCESS : {}", subscription);
        return subscription;
    }

    // 외부 API 호출
    private boolean callExternalApi() {
        ResponseEntity<JsonNode> response = restTemplate.exchange(RANDOM_API_URL, HttpMethod.GET, null, JsonNode.class);
        return response.getBody().get(0).get("random").asInt() == 1;
    }

    // 구독 가능 여부 검증
    private void validateSubscriptionTransition(SubscriptionStatus current, SubscriptionStatus next) {
        if (current == SubscriptionStatus.NONE && next == SubscriptionStatus.BASIC ||
                current == SubscriptionStatus.NONE && next == SubscriptionStatus.PREMIUM ||
                current == SubscriptionStatus.BASIC && next == SubscriptionStatus.PREMIUM) {
            return;
        }
        throw new IllegalStateException("잘못된 구독 전환 요청");
    }

    // 구독 해지 가능 여부 검증
    private void validateUnSubscriptionTransition(SubscriptionStatus current, SubscriptionStatus next) {
        if (current == SubscriptionStatus.PREMIUM && next == SubscriptionStatus.BASIC ||
                current == SubscriptionStatus.PREMIUM && next == SubscriptionStatus.NONE ||
                current == SubscriptionStatus.BASIC && next == SubscriptionStatus.NONE) {
            return;
        }
        throw new IllegalStateException("잘못된 구독 전환 요청");
    }

    // 구독중인 회원 확인 메서드
    public Subscription isSubscribingMember(Member member) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findByMember(member);

        if (optionalSubscription.isPresent()) {
            // 구독중인 경우
            return optionalSubscription.get();
        } else {
            // 구독하지 않은 경우
            throw new BusinessLogicException(Exceptions.SUBSCRIPTION_NOT_FOUND);
        }
    }

}
