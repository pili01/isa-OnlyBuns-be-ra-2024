package rs.ac.uns.ftn.informatika.jpa.additionalSerices;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepository;

import java.time.LocalDateTime;

@Component
public class ActiveUsersMetrics {

    private final UserRepository userRepository;

    public ActiveUsersMetrics(UserRepository userRepository, MeterRegistry meterRegistry) {
        this.userRepository = userRepository;

        // gauge: broj korisnika koji su se logovali u poslednjih 24h
        Gauge.builder("app_active_users_24h", this, ActiveUsersMetrics::getActiveUsersLast24h)
                .description("Number of users who logged in during last 24 hours")
                .register(meterRegistry);

        // Register ProcessorMetrics (provides system CPU metrics)
        new ProcessorMetrics().bindTo(meterRegistry);
    }

    // Micrometer gauge supplier - vraća trenutnu vrednost
    public double getActiveUsersLast24h() {
        // vraća broj korisnika sa lastLoggedTime > now() - 24h
        return (double) userRepository.countByLastLoggedTimeAfter(LocalDateTime.now().minusHours(24));
    }
}
