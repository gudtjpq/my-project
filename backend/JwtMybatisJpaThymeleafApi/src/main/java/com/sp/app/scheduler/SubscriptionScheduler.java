package com.sp.app.scheduler;

import com.sp.app.domain.entity.Subscription;
import com.sp.app.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SubscriptionScheduler {

    @Autowired
    private SubscriptionService subscriptionService;

    @Scheduled(cron = "0 0 9 * * *")
    public void checkDailyPayments() {
        int today = LocalDate.now().getDayOfMonth();
        List<Subscription> dailySubs = subscriptionService.getTodaySubscriptions(today);
        
        for (Subscription sub : dailySubs) {
            System.out.println("결제 알림: " + sub.getServiceName() + " (" + sub.getPrice() + "원)");
        }
    }
}