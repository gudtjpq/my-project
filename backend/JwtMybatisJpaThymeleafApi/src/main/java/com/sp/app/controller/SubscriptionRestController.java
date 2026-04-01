package com.sp.app.controller;

import com.sp.app.domain.entity.Subscription;
import com.sp.app.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/subscription")
@CrossOrigin(origins = "http://localhost:5173")
public class SubscriptionRestController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Subscription subscription) {
        subscriptionService.saveSubscription(subscription);
        Map<String, Object> result = new HashMap<>();
        result.put("state", "true");
        return result;
    }

    @GetMapping("/list/{memberId}")
    public Map<String, Object> getList(@PathVariable("memberId") Long memberId) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", subscriptionService.getSubscriptions(memberId));
        result.put("stats", subscriptionService.getMonthlyStats(memberId));
        return result;
    }
}