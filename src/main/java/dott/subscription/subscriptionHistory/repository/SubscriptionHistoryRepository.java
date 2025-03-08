package dott.subscription.subscriptionHistory.repository;

import dott.subscription.subscriptionHistory.entity.SubscriptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionHistory, Long> {
    List<SubscriptionHistory> findAllByPhoneNumber(String phoneNumber);
}
