package dott.subscription.subscription.entity;

import dott.subscription.audit.Auditable;
import dott.subscription.channel.entity.Channel;
import dott.subscription.constant.SubscriptionStatus;
import dott.subscription.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Subscription extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus subscriptionStatus;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    public Subscription(Member member, SubscriptionStatus subscriptionStatus) {
        this.member = member;
        this.subscriptionStatus = subscriptionStatus;
    }
}

