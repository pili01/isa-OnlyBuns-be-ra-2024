package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class FollowService {

    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<User> getFollowers(int userId) {
        return userRepository.findFollowersByUserId(userId);
    }

    @Transactional
    public boolean followUser(String followerUsername, int followedId) {
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

        // Ažuriraj followed_at u tabeli followers i following
        entityManager.createNativeQuery(
                        "INSERT INTO followers (followed_id, follower_id, followed_at) " +
                                "VALUES (:followedId, :followerId, :followedAt) " +
                                "ON CONFLICT (followed_id, follower_id) DO UPDATE SET followed_at = :followedAt" +
                           "INSERT INTO following (follower_id, followed_id, followed_at) " +
                                "VALUES (:followerId, :followedId, :followedAt) " +
                                "ON CONFLICT (follower_id, followed_id) DO UPDATE SET followed_at = :followedAt")
                .setParameter("followedId", followed.getId())
                .setParameter("followerId", follower.getId())
                .setParameter("followedAt", LocalDateTime.now())
                .executeUpdate();

        logger.info(">finished following user:{} by:{}",followedId,followerUsername);
        return true;
    }

    @Transactional
    public boolean unfollowUser(String followerUsername, int followedId) {
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
        return true;
    }
}
