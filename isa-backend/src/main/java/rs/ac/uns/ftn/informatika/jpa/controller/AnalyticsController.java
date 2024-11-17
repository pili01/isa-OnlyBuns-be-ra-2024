package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.jpa.service.AnalyticsService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/posts-comments")
    public Map<String, Object> getPostsAndCommentsAnalytics(@RequestParam int year, @RequestParam(required = false) Integer month) {
        if (month == null) {
            return analyticsService.getPostsAndCommentsAnalytics(year);
        } else {
            return analyticsService.getPostsAndCommentsAnalytics(year, month);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user-activity")
    public Map<String, Object> getUserActivityAnalytics() {
        return analyticsService.getUserActivityAnalytics();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/posts-comments-by-year")
    public Map<String, Object> getPostsAndCommentsByYear() {
        return analyticsService.getPostsAndCommentsByYear();
    }
}