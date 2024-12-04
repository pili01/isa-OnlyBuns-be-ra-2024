package rs.ac.uns.ftn.informatika.jpa.model;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiterInterceptor implements HandlerInterceptor {

    private static final int MAX_ATTEMPTS = 5; // Maksimalan broj pokušaja
    private static final long TIME_FRAME_IN_SECONDS = 60; // Vremenski okvir u sekundama

    private final Map<String, AttemptInfo> attemptsCache = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddress = request.getRemoteAddr();
        AttemptInfo attemptInfo = attemptsCache.getOrDefault(ipAddress, new AttemptInfo(0, LocalDateTime.now()));

        if (isTimeFrameExpired(attemptInfo.getTimestamp())) {
            // Resetuj pokušaje ako je prošlo više od dozvoljenog vremena
            attemptInfo.setAttempts(0);
            attemptInfo.setTimestamp(LocalDateTime.now());
        }

        if (attemptInfo.getAttempts() >= MAX_ATTEMPTS) {
            response.setStatus(429); // HTTP Status "Too Many Requests"
            response.getWriter().write("Too many login attempts. Please try again later.");
            return false; // Blokiraj zahtev
        }

        // Povećaj broj pokušaja i ažuriraj mapu
        attemptInfo.incrementAttempts();
        attemptsCache.put(ipAddress, attemptInfo);

        return true; // Dozvoli zahtev
    }

    private boolean isTimeFrameExpired(LocalDateTime timestamp) {
        return timestamp.plusSeconds(TIME_FRAME_IN_SECONDS).isBefore(LocalDateTime.now());
    }

    private static class AttemptInfo {
        private int attempts;
        private LocalDateTime timestamp;

        public AttemptInfo(int attempts, LocalDateTime timestamp) {
            this.attempts = attempts;
            this.timestamp = timestamp;
        }

        public int getAttempts() {
            return attempts;
        }

        public void incrementAttempts() {
            this.attempts++;
        }

        public void setAttempts(int attempts) {
            this.attempts = attempts;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }
}
