package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.repository.CommentRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getPostsAndCommentsAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("weeklyPosts", postRepository.countPostsByWeek());
        analytics.put("monthlyPosts", postRepository.countPostsByMonth());
        analytics.put("yearlyPosts", postRepository.countPostsByYear());
        analytics.put("weeklyComments", commentRepository.countCommentsByWeek());
        analytics.put("monthlyComments", commentRepository.countCommentsByMonth());
        analytics.put("yearlyComments", commentRepository.countCommentsByYear());
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