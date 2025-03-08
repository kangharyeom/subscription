package dott.subscription.subscription.controller;

import dott.subscription.subscription.dto.SubscribeDto;
import dott.subscription.subscription.entity.Subscription;
import dott.subscription.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    /**
     * 구독
     */
    @PostMapping("/subscribe")
    public Subscription subscribe(@RequestBody SubscribeDto subscribeDto) {
        log.info("SUBSCRIBE START");
        return subscriptionService.subscribe(subscribeDto);
    }

    /**
     * 구독 해지
     */
    @PostMapping("/unsubscribe/{subscriptionId}")
    public Subscription unsubscribe(@RequestBody SubscribeDto subscribeDto) {
        log.info("UNSUBSCRIBE START");
        return subscriptionService.unsubscribe(subscribeDto);
    }
}