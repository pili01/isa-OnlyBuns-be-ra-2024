package rs.ac.uns.ftn.informatika.jpa.additionalSerices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.CommentRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepository;
import rs.ac.uns.ftn.informatika.jpa.service.EmailService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsToEmail {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EmailService emailService;

    public void sendUserStatisticToEmail(){
        List<User> userList = userRepository.findAllWhoDidntLogForSevenDays();
//        System.out.println(userList);

        for(User user : userList){
            long newLikesCount = 0;
            long likesCount = 0;
            long newCommentsCount = 0;
            long commentsCount = 0;
            long newFollowersCount = 0;
            long followersCount = user.getFollowersCount();
            long newPostCount = 0;
            long postsCount = 0;

            List<User> followingUsers = userRepository.findFollowedByUserId(user.getId());
//            System.out.println("User ID: " + user.getId() + "\nKOGA PRATIM: " + followingUsers);
            List<Post> postList = postRepository.findAllMyPosts(user.getId());
//            System.out.println(postList);
            List<Integer> postIds = postList.stream()
                    .map(Post::getId)
                    .collect(Collectors.toList());

//            System.out.println("OVO JE BROJ POSTOVA: " + postIds);

            newLikesCount = postRepository.countLikesFromSevenDaysAgoForPosts(postIds, user.getLastLoggedTime());
            likesCount = postRepository.countLikesForPosts(postIds);

//            System.out.println("User ID: " + user.getId() + "\nNOVIH LAJKOVA: " + newLikesCount + "\nUKUPNO LAJKOVA: " + likesCount);

            newCommentsCount = commentRepository.countCommentFromSevenDayAgo(postIds, user.getLastLoggedTime());
            commentsCount = commentRepository.countCommentFromPost(postIds);

//            System.out.println("User ID: " + user.getId() + "\nNOVIH KOMENTARA: " + newCommentsCount + "\nUKUPNO KOMENTARA: " + commentsCount);

            newFollowersCount = userRepository.countNewFollowers(user.getId(), user.getLastLoggedTime());

//            System.out.println("User ID: " + user.getId() + "\nNOVIH PRATILACA: " + newFollowersCount + "\nUKUPNO PRATILACA: " + followersCount);

            for(User f : followingUsers){
                List<Post> followersPostList = postRepository.findAllMyPosts(f.getId());
                List<Integer> followersPostsIds = followersPostList.stream()
                        .map(Post::getId)
                        .collect(Collectors.toList());

                newPostCount += postRepository.countPostsByUserAndDate(followersPostsIds, user.getLastLoggedTime());
                postsCount += postRepository.countPostsByUser(followersPostsIds);
            }
            String msg = "<p>Greetings " + user.getFirstName() + ",</p>"
                    + "<p>Since you didn't visit our web app in 7 days</br>We are sending you what you missed on:</p></br>"
                    + "<p>Number of <b>new likes</b> your posts got is <b>" + newLikesCount + "</b> and now you have a total of <b>" + likesCount + "</b> likes.</p>"
                    + "<p>Number of <b>new comments</b> on your posts is <b>" + newCommentsCount + "</b> and now you have a total of <b>" + commentsCount + "</b> comments on your posts.</p>"
                    + "<p>Number of <b>new followers</b> you got is <b>" + newFollowersCount + "</b> and now you have a total of <b>" + followersCount + "</b> followers.</p>"
                    + "<p>Number of <b>new posts</b> your followers posted is <b>" + newPostCount + "</b> and now you have a total of <b>" + postsCount + "</b> posts you haven't seen.</p></br>"
                    + "<p>We are expecting you!</p>";

            System.out.println("\nGreetings " + user.getFirstName() + ",\n"
                    + "\nSince you didn't visit our web app in 7 days</br>We are sending you what you missed on:\n\n"
                    + "\nNumber of <b>new likes</b> your posts got is <b>" + newLikesCount + "</b> and now you have a total of <b>" + likesCount + "</b> likes.\n"
                    + "\nNumber of <b>new comments</b> on your posts is <b>" + newCommentsCount + "</b> and now you have a total of <b>" + commentsCount + "</b> comments on your posts.\n"
                    + "\nNumber of <b>new followers</b> you got is <b>" + newFollowersCount + "</b> and now you have a total of <b>" + followersCount + "</b> followers.\n"
                    + "\nNumber of <b>new posts</b> your followers posted is <b>" + newPostCount + "</b> and now you have a total of <b>" + postsCount + "</b> posts you haven't seen.\n\n"
                    + "\nWe are expecting you!\n\n");

            emailService.sendNotificationEmail(user.getEmail(), msg);
        }
    }
}
