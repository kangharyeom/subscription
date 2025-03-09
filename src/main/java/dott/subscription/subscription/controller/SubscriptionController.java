package dott.subscription.subscription.controller;

import dott.subscription.response.SingleResponseDto;
import dott.subscription.subscription.dto.SubscribeDto;
import dott.subscription.subscription.dto.SubscribeResponseDto;
import dott.subscription.subscription.entity.Subscription;
import dott.subscription.subscription.mapper.SubscriptionMapper;
import dott.subscription.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;

    /**
     * 구독
     */
    @PostMapping("/subscribe")
    public ResponseEntity subscribe(@RequestBody SubscribeDto subscribeDto) {
        log.info("SUBSCRIBE START");
        Subscription subscription = subscriptionService.subscribe(subscribeDto);
        log.debug("[SUBSCRIPTION ENTITY - subscribe] : {}", subscription.toString());

        SubscribeResponseDto subscribeResponseDto = subscriptionMapper.subscriptionToSubscribeResponseDto(subscription);
        log.debug("[SubscribeResponseDto - subscribe] : {}", subscribeResponseDto.toString());

        log.info("SUBSCRIBE END");
        return new ResponseEntity<>(new SingleResponseDto<>(subscribeResponseDto), HttpStatus.CREATED);
    }

    /**
     * 구독 해지
     */
    @PostMapping("/unsubscribe")
    public ResponseEntity unsubscribe(@RequestBody SubscribeDto subscribeDto) {
        log.info("UNSUBSCRIBE START");
        Subscription subscription = subscriptionService.unsubscribe(subscribeDto);
        log.debug("[SUBSCRIPTION ENTITY - unsubscribe] : {}", subscription.toString());

        SubscribeResponseDto subscribeResponseDto = subscriptionMapper.subscriptionToSubscribeResponseDto(subscription);
        log.debug("[SubscribeResponseDto - unsubscribe] : {}", subscribeResponseDto.toString());

        log.info("UNSUBSCRIBE END");
        return new ResponseEntity<>(new SingleResponseDto<>(subscribeResponseDto), HttpStatus.ACCEPTED);
    }
}