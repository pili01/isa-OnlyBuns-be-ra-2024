package rs.ac.uns.ftn.informatika.jpa.service;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class RateLimiterService {

    private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Queue<Instant>> requestTimestamps = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String username) {
        return buckets.computeIfAbsent(username, this::newBucket);
    }

    private Bucket newBucket(String username) {
        Bandwidth limit = Bandwidth.simple(50, Duration.ofMinutes(10000));
        return Bucket4j.builder().addLimit(limit).build();
    }

    public synchronized boolean tryConsume(String username) {
        Bucket bucket = resolveBucket(username);
        Queue<Instant> timestamps = requestTimestamps.computeIfAbsent(username, k -> new LinkedList<>());

        Instant now = Instant.now();
        Instant windowStart = now.minus(Duration.ofMinutes(1));

        // Uklonite sve zahteve koji su izvan trenutnog 60-sekundnog prozora
        while (!timestamps.isEmpty() && timestamps.peek().isBefore(windowStart)) {
            timestamps.poll();
        }
        bucket.addTokens(50- timestamps.size());

        if (timestamps.size() < 50) {
            timestamps.add(now);
            System.out.println("Uzet 1 ostalo u kanti: "+(50- timestamps.size()));
            return true;
        } else {
            return false;
        }
    }
}
