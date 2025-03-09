package dott.subscription.member.entity;

import dott.subscription.audit.Auditable;
import dott.subscription.subscription.entity.Subscription;
import dott.subscription.subscriptionHistory.entity.SubscriptionHistory;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Setter
    private String phoneNumber;

    @ToString.Exclude
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Subscription subscription;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubscriptionHistory> subscriptionHistories;
}