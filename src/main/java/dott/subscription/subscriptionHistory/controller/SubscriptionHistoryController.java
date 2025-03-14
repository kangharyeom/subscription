package dott.subscription.subscriptionHistory.controller;

import dott.subscription.response.SingleResponseDto;
import dott.subscription.subscriptionHistory.dto.SubscriptionHistoryDto;
import dott.subscription.subscriptionHistory.entity.SubscriptionHistory;
import dott.subscription.subscriptionHistory.service.SubscriptionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription/histories")
public class SubscriptionHistoryController {
    private final SubscriptionHistoryService subscriptionHistoryService;

    /**
     * 구독 & 구독 해지 이력 조회
     */
    @GetMapping("/search")
    public ResponseEntity getSubscriptionHistories(@RequestBody @Validated SubscriptionHistoryDto subscriptionHistoryDto){
        log.info("GET SUBSCRIPTION HISTORIES START");
        List<SubscriptionHistory> subscriptionHistoryResponse = subscriptionHistoryService.getSubscriptionHistory(subscriptionHistoryDto.getPhoneNumber());

        log.info("GET SUBSCRIPTION HISTORIES END");
        return new ResponseEntity(new SingleResponseDto<>(subscriptionHistoryResponse), HttpStatus.OK);
    }
}
