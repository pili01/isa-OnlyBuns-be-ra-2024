package rs.ac.uns.ftn.informatika.jpa.additionalSerices;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.service.RateLimiterService;

import java.time.Duration;

@Aspect
@Component
public class CommentRateLimiterAspect {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Around("@annotation(CommentRateLimited)")
    public Object rateLimit(ProceedingJoinPoint pjp) throws Throwable {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean allowed = rateLimiterService.tryConsumeComment(username); // 👈 3.23 rule

        if (allowed) {
            return pjp.proceed();
        } else {
            throw new Exception("Rate limit exceeded: max 5 comments per minute.");
        }
    }
}
