package rs.ac.uns.ftn.informatika.jpa.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.UserDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.UserLikeDTO;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepository;
import rs.ac.uns.ftn.informatika.jpa.service.PostService;
import rs.ac.uns.ftn.informatika.jpa.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trends")
public class TrendsController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    // GET /api/trends/stats
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getNetworkStatistics() {
        Map<String, Object> stats = new HashMap<>();
        postService.getPostsLastMonth();
        stats.put("totalPosts", postService.getTotalPosts());
        stats.put("postsLastMonth", postService.getPostsLastMonth());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/most-liked")
    public List<PostDTO> getPopularPostsAllTime() {
        List<PostDTO> postDTOs = postService.getTop10MostLikedPosts();
        return postDTOs;
    }




    @GetMapping("/most-liked/weekly")
    public List<PostDTO> getTop5MostLikedPostsLastWeek() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return postService.getTop5MostLikedPostsLastWeek(oneWeekAgo);
    }

    @GetMapping("/top-likers")
    public List<UserLikeDTO> getTopLikers() {
        return postService.getTop10UsersWithMostLikesLastWeek();
    }




}




