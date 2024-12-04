package rs.ac.uns.ftn.informatika.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.service.FollowService;
import rs.ac.uns.ftn.informatika.jpa.service.PostService;
import rs.ac.uns.ftn.informatika.jpa.service.UserService;
import rs.ac.uns.ftn.informatika.jpa.model.User;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaExampleApplicationTests {

	@Autowired
	private UserService userService;
	@Autowired
	private PostService postService;

	@Autowired
	private FollowService followService;

	@Test
	public void contextLoads() {
		// Testiranje Spring konteksta
	}

	@Test
	public void testConcurrentFollow() throws InterruptedException {
		// Priprema podataka
		Optional<User> followed = userService.findOne(2);
		Optional<User> follower1 = userService.findOne(3);
		Optional<User> follower2 = userService.findOne(4);
		Post post = new Post();

		// Paralelno izvršavanje
		Runnable task1 = () -> followService.followUser(follower1.get().getUsername(), followed.get().getId());
		Runnable task2 = () -> followService.followUser(follower2.get().getUsername(), followed.get().getId());

		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.submit(task1);
		executor.submit(task2);
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.SECONDS);

		// Provera rezultata
		Optional<User> updatedFollowed = userService.findOne(followed.get().getId());
		assertEquals(2, updatedFollowed.get().getFollowersCount());
	}

	@Test
	public void testConcurrentUnFollow() throws InterruptedException {
		// Priprema podataka
		Optional<User> followed = userService.findOne(7);
		Optional<User> follower1 = userService.findOne(8);
		Optional<User> follower2 = userService.findOne(9);
		Post post = new Post();

		// Paralelno izvršavanje
		Runnable task1 = () -> followService.unfollowUser(follower1.get().getUsername(), followed.get().getId());
		Runnable task2 = () -> followService.unfollowUser(follower2.get().getUsername(), followed.get().getId());

		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.submit(task1);
		executor.submit(task2);
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.SECONDS);

		// Provera rezultata
		Optional<User> updatedFollowed = userService.findOne(followed.get().getId());
		assertEquals(0, updatedFollowed.get().getFollowersCount());
	}
}
