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
import java.util.concurrent.CountDownLatch;
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
		executor.awaitTermination(1, TimeUnit.SECONDS);

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
		executor.awaitTermination(1, TimeUnit.SECONDS);

		// Provera rezultata
		Optional<User> updatedFollowed = userService.findOne(followed.get().getId());
		assertEquals(0, updatedFollowed.get().getFollowersCount());
	}

	@Test
	public void testConcurrentLike() throws InterruptedException {
		//post
//		Post post = postService.findOne(2);
//		int likesOnStart = post.getLikers().size();

		// Priprema podataka
		User liker6 = userService.findOne(6).orElseThrow(() -> new RuntimeException("User not found"));
		User liker2 = userService.findOne(2).orElseThrow(() -> new RuntimeException("User not found"));
		User liker7 = userService.findOne(7).orElseThrow(() -> new RuntimeException("User not found"));
		User liker4 = userService.findOne(4).orElseThrow(() -> new RuntimeException("User not found"));
		User liker5 = userService.findOne(5).orElseThrow(() -> new RuntimeException("User not found"));

		// Paralelno izvršavanje
		Runnable task1 = () -> postService.addLike(2, liker2);
		Runnable task2 = () -> postService.addLike(2, liker6);
		Runnable task3 = () -> postService.addLike(2, liker4);
		Runnable task4 = () -> postService.addLike(2, liker7);
		Runnable task5 = () -> postService.addLike(2, liker5);

		ExecutorService executor = Executors.newFixedThreadPool(5);
		executor.submit(task1);
		executor.submit(task4);
		executor.submit(task2);
		executor.submit(task5);
		executor.submit(task3);
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.SECONDS);

		// Provera rezultata
		Post samePost = postService.findOneWithLikers(2);
		assertEquals(7, samePost.getLikes());
	}

	@Test
	public void testConcurrentUnLike() throws InterruptedException {
		//post
//		Post post = postService.findOne(1);
//		int likesOnStart = post.getLikers().size();

		// Priprema podataka
		User liker1 = userService.findOne(1).orElseThrow(() -> new RuntimeException("User not found"));
//		User liker2 = userService.findOne(2).orElseThrow(() -> new RuntimeException("User not found"));
		User liker3 = userService.findOne(3).orElseThrow(() -> new RuntimeException("User not found"));
		User liker4 = userService.findOne(4).orElseThrow(() -> new RuntimeException("User not found"));
//		User liker6 = userService.findOne(6).orElseThrow(() -> new RuntimeException("User not found"));

		// Paralelno izvršavanje
		Runnable task1 = () -> postService.removeLike(1, liker1);
//		Runnable task2 = () -> postService.addLike(1, liker2);
		Runnable task3 = () -> postService.removeLike(1, liker3);
//		Runnable task4 = () -> postService.addLike(1, liker6);
		Runnable task5 = () -> postService.removeLike(1, liker4);

		ExecutorService executor = Executors.newFixedThreadPool(3);
		executor.submit(task1);
//		executor.submit(task2);
		executor.submit(task3);
//		executor.submit(task4);
		executor.submit(task5);
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.SECONDS);

		// Provera rezultata
		Post samePost = postService.findOneWithLikers(1);
		assertEquals(1, samePost.getLikes());
	}

}

