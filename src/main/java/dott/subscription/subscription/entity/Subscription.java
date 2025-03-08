package dott.subscription.subscription.entity;

import dott.subscription.audit.Auditable;
import dott.subscription.channel.entity.Channel;
import dott.subscription.constant.SubscriptionStatus;
import dott.subscription.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subscription extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false, unique = true)
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

