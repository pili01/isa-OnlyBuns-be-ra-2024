package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.jpa.service.AnalyticsService;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/posts-comments")
    public Map<String, Object> getPostsAndCommentsAnalytics() {
        return analyticsService.getPostsAndCommentsAnalytics();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user-activity")
    public Map<String, Object> getUserActivityAnalytics() {
        return analyticsService.getUserActivityAnalytics();
    }
}