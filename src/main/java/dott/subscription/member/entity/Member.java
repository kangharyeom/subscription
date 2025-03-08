package dott.subscription.member.entity;

import dott.subscription.audit.Auditable;
import dott.subscription.subscription.entity.Subscription;
import dott.subscription.subscriptionHistory.entity.SubscriptionHistory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Setter
    private String phoneNumber;

    @OneToMany(mappedBy = "member")
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "member")
    private List<SubscriptionHistory> subscriptionHistories;
}