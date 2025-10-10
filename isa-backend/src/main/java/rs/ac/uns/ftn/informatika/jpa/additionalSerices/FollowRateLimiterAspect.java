package rs.ac.uns.ftn.informatika.jpa.additionalSerices;
import io.github.bucket4j.Bucket;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.service.*;
@Aspect
@Component
public class FollowRateLimiterAspect {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Around("@annotation(FollowRateLimited)")
    public Object rateLimit(ProceedingJoinPoint pjp) throws Throwable {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (rateLimiterService.tryConsumeFollower(username)) {
            return pjp.proceed();
        } else {
            throw new Exception("Rate limit exceeded");
        }
    }
}
