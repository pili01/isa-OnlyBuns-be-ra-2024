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

    // 👥 Limit za FOLLOW zahteve: 50 u 1 minuti
    public synchronized boolean tryConsumeFollower(String username) {
        String key = username + "_follow";

        Bucket bucket = buckets.computeIfAbsent(key, k -> {
            Bandwidth limit = Bandwidth.simple(50, Duration.ofMinutes(1));
            return Bucket4j.builder().addLimit(limit).build();
        });

        Queue<Instant> timestamps = requestTimestamps.computeIfAbsent(key, k -> new LinkedList<>());

        Instant now = Instant.now();
        Instant windowStart = now.minus(Duration.ofMinutes(1));

        // Očistimo stare unose
        while (!timestamps.isEmpty() && timestamps.peek().isBefore(windowStart)) {
            timestamps.poll();
        }

        // Dodamo tokene ako treba (manje od 50)
        bucket.addTokens(50 - timestamps.size());

        if (timestamps.size() < 50) {
            timestamps.add(now);
            System.out.println("✅ Follow dozvoljen. Trenutno: " + timestamps.size() + "/50");
            return true;
        } else {
            System.out.println("❌ Prekoračen follow limit za korisnika: " + username);
            return false;
        }
    }


    //add comment
    // 💬 Limit za KOMENTARE: 5 u 1 minuti
    public synchronized boolean tryConsumeComment(String username) {
        String key = username + "_comment";

        Bucket bucket = buckets.computeIfAbsent(key, k -> {
            Bandwidth limit = Bandwidth.simple(5, Duration.ofMinutes(1));
            return Bucket4j.builder().addLimit(limit).build();
        });

        Queue<Instant> timestamps = requestTimestamps.computeIfAbsent(key, k -> new LinkedList<>());

        Instant now = Instant.now();
        Instant windowStart = now.minus(Duration.ofMinutes(1));

        while (!timestamps.isEmpty() && timestamps.peek().isBefore(windowStart)) {
            timestamps.poll();
        }

        System.out.println("ALOOOO EVO TIMESTAMPS.SIZE() = " + timestamps.size());
        bucket.addTokens(5 - timestamps.size());

        if (timestamps.size() < 5) {
            timestamps.add(now);
            System.out.println("✅ Komentar dozvoljen. Trenutno: " + timestamps.size() + "/5");
            return true;
        } else {
            System.out.println("❌ Prekoračen komentar limit za korisnika: " + username);
            return false;
        }
    }
}
