package com.sp.app.service;

import com.sp.app.domain.entity.Subscription;
import com.sp.app.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public void saveSubscription(Subscription subscription) {
        subscriptionRepository.save(subscription);
    }

    public List<Subscription> getSubscriptions(Long memberId) {
        return subscriptionRepository.findByMemberIdOrderByPaymentDayAsc(memberId);
    }

    public List<Map<String, Object>> getMonthlyStats(Long memberId) {
        List<Subscription> list = subscriptionRepository.findByMemberIdOrderByPaymentDayAsc(memberId);

        Map<String, Integer> groupedStats = list.stream()
                .collect(Collectors.groupingBy(
                        Subscription::getCategory,
                        Collectors.summingInt(Subscription::getPrice)
                ));

        List<Map<String, Object>> statsList = new ArrayList<>();
        groupedStats.forEach((category, totalAmount) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("CATEGORY", category);
            map.put("TOTALAMOUNT", totalAmount);
            statsList.add(map);
        });

        statsList.sort((m1, m2) -> ((Integer) m2.get("TOTALAMOUNT")).compareTo((Integer) m1.get("TOTALAMOUNT")));

        return statsList;
    }

    public List<Subscription> getTodaySubscriptions(Integer day) {
        return subscriptionRepository.findByPaymentDay(day);
    }
}