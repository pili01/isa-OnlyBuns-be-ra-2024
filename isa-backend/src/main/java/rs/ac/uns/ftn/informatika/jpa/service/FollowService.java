package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepository;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FollowService {

    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional(readOnly = true)
    public List<User> getFollowers(int userId) {
        return userRepository.findFollowersByUserId(userId);
    }

    @Transactional
    public void followUser(String followerUsername, int followedId) {
        logger.info("> following user:{} by :{}",followedId,followerUsername);
        User follower = userRepository.findByUsernameForUpdate(followerUsername).orElseThrow(() -> new IllegalArgumentException("User not found"));
        User followed = userRepository.findByIdForUpdate(followedId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (follower.getFollowByMe().contains(followed)) {
            throw new IllegalArgumentException("Already following this user.");
        }

        if (follower.getId() == followed.getId()) {
            throw new IllegalArgumentException("You can't follow yourself.");
        }

        follower.getFollowByMe().add(followed);
        followed.getFollowMe().add(follower);

        // Povećaj brojače
        followed.incrementFollowersCount();
        follower.incrementFollowingCount();

        userRepository.save(follower);
        userRepository.save(followed);
        logger.info(">finished following user:{} by:{}",followedId,followerUsername);
    }

    @Transactional
    public void unfollowUser(String followerUsername, int followedId) {
        logger.info("> unfollowing user:{} by :{}",followedId,followerUsername);
        User follower = userRepository.findByUsernameForUpdate(followerUsername).orElseThrow(() -> new IllegalArgumentException("User not found."));
        User followed = userRepository.findByIdForUpdate(followedId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!follower.getFollowByMe().contains(followed)) {
            throw new IllegalArgumentException("Not following this user.");
        }

        if (follower.getId() == followed.getId()) {
            throw new IllegalArgumentException("You can't unfollow yourself.");
        }

        follower.getFollowByMe().remove(followed);
        followed.getFollowMe().remove(follower);

        // Smanji brojače
        followed.decrementFollowersCount();
        follower.decrementFollowingCount();

        userRepository.save(follower);
        userRepository.save(followed);
        logger.info(">finished unfollowing user:{} by:{}",followedId,followerUsername);
    }
}
