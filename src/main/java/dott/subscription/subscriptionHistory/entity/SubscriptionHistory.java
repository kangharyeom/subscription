package dott.subscription.subscriptionHistory.entity;

import dott.subscription.audit.Auditable;
import dott.subscription.channel.entity.Channel;
import dott.subscription.constant.SubscriptionStatus;
import dott.subscription.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscription_histories")
@Getter
@Setter
@NoArgsConstructor
public class SubscriptionHistory extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String channelName;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus previousSubscriptionStatus;  // 이전 구독 상태

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus newSubscriptionStatus;  // 변경된 구독 상태

    @Column
    private LocalDateTime cancelledAt; // 구독 해지한 경우 업데이트

    // 구독 생성 이력
    public SubscriptionHistory(Long id, String phoneNumber, String channelName, Member member, Channel channel, SubscriptionStatus previousSubscriptionStatus, SubscriptionStatus newSubscriptionStatus) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.channelName = channelName;
        this.member = member;
        this.channel = channel;
        this.previousSubscriptionStatus = previousSubscriptionStatus;
        this.newSubscriptionStatus = newSubscriptionStatus;
    }

    // 구독 해지 이력
    public SubscriptionHistory(Long id, String phoneNumber, String channelName, Member member, Channel channel, SubscriptionStatus previousSubscriptionStatus, SubscriptionStatus newSubscriptionStatus, LocalDateTime cancelledAt) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.channelName = channelName;
        this.member = member;
        this.channel = channel;
        this.previousSubscriptionStatus = previousSubscriptionStatus;
        this.newSubscriptionStatus = newSubscriptionStatus;
        this.cancelledAt = cancelledAt;
    }

}