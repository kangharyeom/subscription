package dott.subscription.subscription.service;

import com.fasterxml.jackson.databind.JsonNode;
import dott.subscription.channel.entity.Channel;
import dott.subscription.channel.service.ChannelService;
import dott.subscription.constant.ChannelType;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final EntityManager entityManager;


    private static final String RANDOM_API_URL = "https://csrng.net/csrng/csrng.php?min=0&max=1";

    // 구독 기능
    @Transactional
    public Subscription subscribe(SubscribeDto subscribeDto) {
        // 회원 인증
        Member member = memberService.findMemberByPhoneNumber(subscribeDto.getPhoneNumber());

        // 채널 유무 확인
        Channel channel = channelService.findChannelByChannelId(subscribeDto.getChannelId());

        // 구독 여부 확인 (구독중인 회원인지 확인) (비관적락 적용)
        Subscription existingSubscription = findSubscriptionByPhoneNumber(member);

        Subscription subscription;

        if (existingSubscription != null) {
            subscription = entityManager.find(Subscription.class, existingSubscription.getId(), LockModeType.PESSIMISTIC_WRITE);
            if (subscription == null) {
                throw new BusinessLogicException(Exceptions.SUBSCRIPTION_NOT_FOUND);
            }
        } else {
            subscription = new Subscription(member, SubscriptionStatus.NONE);
        }

        SubscriptionStatus previousSubscriptionStatus = subscription.getSubscriptionStatus();
        SubscriptionStatus newSubscriptionStatus = subscribeDto.getSubscriptionStatus();

        // 구독 상태 변경 가능한지 확인
        checkSubscriptionChangeRule(previousSubscriptionStatus, newSubscriptionStatus);

        // 구독 가능한 채널인지 확인
        checkSubscriptionPossibleChannel(channel.getChannelType());

        // 외부 API 통신
        if (!callExternalApi()) {
            throw new BusinessLogicException(Exceptions.SUBSCRIPTION_API_CONNECTION_ERROR);
        }

        // 구독 정보 최신화
        subscription.setSubscriptionStatus(subscribeDto.getSubscriptionStatus());
        subscription.setMember(member);
        subscription.setChannel(channel);

        // 예외 미발생시 구독 테이블 반영
        entityManager.persist(subscription);

        // 구독 이력 생성
        subscriptionHistoryRepository.save(new SubscriptionHistory(null, member, channel, previousSubscriptionStatus, subscribeDto.getSubscriptionStatus()));

        log.info("SUBSCRIBE SUCCESS : {}", subscription.toString());
        return subscription;
    }

    // 구독해지 기능
    @Transactional
    public Subscription unsubscribe(SubscribeDto subscribeDto) {
        // 회원 인증
        Member member = memberService.findMemberByPhoneNumber(subscribeDto.getPhoneNumber());

        // 채널 유무 확인
        Channel channel = channelService.findChannelByChannelId(subscribeDto.getChannelId());

        // 구독 여부 확인 (구독중인 회원인지 확인) (비관적락 적용)
        Subscription existingSubscription = findSubscriptionByPhoneNumber(member);

        Subscription subscription;

        if (existingSubscription != null) {
            subscription = entityManager.find(Subscription.class, existingSubscription.getId(), LockModeType.PESSIMISTIC_WRITE);
            if (subscription == null) {
                throw new BusinessLogicException(Exceptions.SUBSCRIPTION_NOT_FOUND);
            }
        } else {
            throw new BusinessLogicException(Exceptions.SUBSCRIPTION_NOT_FOUND);
        }

        SubscriptionStatus previousSubscriptionStatus = subscription.getSubscriptionStatus();
        SubscriptionStatus newSubscriptionStatus = subscribeDto.getSubscriptionStatus();

        // 구독 해지 상태 변경 가능한지 확인
        checkUnSubscriptionChangeRule(previousSubscriptionStatus, newSubscriptionStatus);

        // 구독 해지 가능한 채널인지 확인
        checkUnSubscriptionPossibleChannel(channel.getChannelType());

        // 외부 API 통신
        if (!callExternalApi()) {
            throw new BusinessLogicException(Exceptions.SUBSCRIPTION_API_CONNECTION_ERROR);
        }

        subscription.setSubscriptionStatus(newSubscriptionStatus);
        // 예외 미발생시 구독 테이블 반영
        entityManager.persist(subscription);

        // 구독 해지 이력 생성
        if (newSubscriptionStatus.equals(SubscriptionStatus.NONE)) {
            // 일반구독 || 프리미엄 구독 -> 구독해지
            LocalDateTime unsubscribeTime = LocalDateTime.now();
            subscriptionHistoryRepository.save(new SubscriptionHistory(null, member, channel, previousSubscriptionStatus, subscribeDto.getSubscriptionStatus(), unsubscribeTime));
        } else {
            // 프리미엄 구독 -> 일반구독
            subscriptionHistoryRepository.save(new SubscriptionHistory(null, member, channel, previousSubscriptionStatus, subscribeDto.getSubscriptionStatus()));
        }
        


        log.info("UNSUBSCRIBE SUCCESS : {}", subscription.toString());
        return subscription;
    }

    // 외부 API 호출
    private boolean callExternalApi() {
        ResponseEntity<JsonNode> response = restTemplate.exchange(RANDOM_API_URL, HttpMethod.GET, null, JsonNode.class);
        log.debug("EXTERNAL API RESPONSE : {}", response.toString());
        return response.getBody().get(0).get("random").asInt() == 1;
    }

    // 구독 상태 변경 가능 규칙
    private void checkSubscriptionChangeRule(SubscriptionStatus current, SubscriptionStatus next) {
        if (current == SubscriptionStatus.NONE && next == SubscriptionStatus.BASIC ||
                current == SubscriptionStatus.NONE && next == SubscriptionStatus.PREMIUM ||
                current == SubscriptionStatus.BASIC && next == SubscriptionStatus.PREMIUM) {
            return;
        }
        throw new BusinessLogicException(Exceptions.CAN_NOT_SUBSCRIBE);
    }

    // 구독 해지 상태 변경 가능 규칙
    private void checkUnSubscriptionChangeRule(SubscriptionStatus current, SubscriptionStatus next) {
        if (current == SubscriptionStatus.PREMIUM && next == SubscriptionStatus.BASIC ||
                current == SubscriptionStatus.PREMIUM && next == SubscriptionStatus.NONE ||
                current == SubscriptionStatus.BASIC && next == SubscriptionStatus.NONE) {
            return;
        }
        throw new BusinessLogicException(Exceptions.CAN_NOT_UNSUBSCRIBE);
    }

    // 구독 가능 채널 확인
    private void checkSubscriptionPossibleChannel(ChannelType channelType) {
        if (channelType.equals(ChannelType.BOTH) || channelType.equals(ChannelType.SUBSCRIBE_ONLY)) {
            return;
        }
        throw new BusinessLogicException(Exceptions.CAN_NOT_SUBSCRIBE_CHANNEL);
    }

    // 구독 해지 가능 채널 확인
    private void checkUnSubscriptionPossibleChannel(ChannelType channelType) {
        if (channelType.equals(ChannelType.BOTH) || channelType.equals(ChannelType.SUBSCRIBE_ONLY)) {
            return;
        }
        throw new BusinessLogicException(Exceptions.CAN_NOT_UNSUBSCRIBE_CHANNEL);
    }

    // 구독중인 회원 조회
    public Subscription findSubscriptionByPhoneNumber(Member member) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findByMember(member);

        if (optionalSubscription.isPresent()) {
            // 구독중인 경우
            return optionalSubscription.get();
        } else {
            // 구독하지 않은 경우
            return null;
        }
    }

}
