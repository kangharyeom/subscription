package dott.subscription.channel.entity;

import dott.subscription.audit.Auditable;
import dott.subscription.constant.ChannelType;
import dott.subscription.subscription.entity.Subscription;
import dott.subscription.subscriptionHistory.entity.SubscriptionHistory;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "channels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Channel extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private ChannelType channelType;

    @OneToMany(mappedBy = "channel")
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "channel")
    private List<SubscriptionHistory> subscriptionHistories;

}