package com.omaragayev.civicflow.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {

    // Thread-safe map holding per-client in-memory token buckets
    private final Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String clientIp) {
        return bucketCache.computeIfAbsent(clientIp, this::createNewBucket);
    }

    private Bucket createNewBucket(String key) {
        // Capacity: 5 tokens | Refill Rate: 5 tokens per 1 minute
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}