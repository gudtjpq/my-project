package com.sp.app.repository;

import com.sp.app.domain.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByMemberIdOrderByPaymentDayAsc(Long memberId);
    List<Subscription> findByPaymentDay(Integer day);
}