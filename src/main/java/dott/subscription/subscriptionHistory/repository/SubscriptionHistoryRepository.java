package dott.subscription.subscriptionHistory.repository;

import dott.subscription.member.entity.Member;
import dott.subscription.subscriptionHistory.entity.SubscriptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionHistory, Long> {
    List<SubscriptionHistory> findAllByMember(Member member);
}
