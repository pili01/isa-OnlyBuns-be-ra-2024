package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.jpa.additionalSerices.FollowRateLimited;
import rs.ac.uns.ftn.informatika.jpa.service.FollowService;

@RestController
@RequestMapping("/api/follower")
public class FollowController {

    @Autowired
    private FollowService followService;

    @FollowRateLimited
    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    @PostMapping("/follow/{followedId}")
    public ResponseEntity<Boolean> follow(@PathVariable int followedId) {
        String followerUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (followService.followUser(followerUsername, followedId))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    @PostMapping("/unfollow/{followedId}")
    public ResponseEntity<Boolean> unfollow(@PathVariable int followedId) {
        String followerUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (followService.unfollowUser(followerUsername, followedId))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }
}
