package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.repository.CommentRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getPostsAndCommentsAnalytics(int year) {
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("monthlyPosts", postRepository.countPostsByMonth(year));
        analytics.put("monthlyComments", commentRepository.countCommentsByMonth(year));
        return analytics;
    }

    public Map<String, Object> getPostsAndCommentsAnalytics(int year, int month) {
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("weeklyPosts", postRepository.countPostsByWeek(year, month));
        analytics.put("weeklyComments", commentRepository.countCommentsByWeek(year, month));
        return analytics;
    }

    public Map<String, Object> getUserActivityAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        long totalUsers = userRepository.count();
        long usersWithPosts = userRepository.countUsersWithPosts();
        long usersWithCommentsOnly = userRepository.countUsersWithCommentsOnly();
        long inactiveUsers = totalUsers - usersWithPosts - usersWithCommentsOnly;

        analytics.put("usersWithPosts", (double) usersWithPosts / totalUsers * 100);
        analytics.put("usersWithCommentsOnly", (double) usersWithCommentsOnly / totalUsers * 100);
        analytics.put("inactiveUsers", (double) inactiveUsers / totalUsers * 100);
        return analytics;
    }
}