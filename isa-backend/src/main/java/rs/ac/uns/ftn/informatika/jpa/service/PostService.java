package rs.ac.uns.ftn.informatika.jpa.service;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.ResourceNotFoundException;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.mapper.PostDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostDTOMapper postDTOMapper;
    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    public Post findOne(Integer id) {
        return postRepository.findById(id).orElseGet(null);
    }

    @Transactional
    public Post save(Post post) {
        logger.info("> post:{} creating by :{}",post.getId(),post.getAuthor().getId());
        User author = userRepository.findByIdForUpdate(post.getAuthor().getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        author.incrementPostNumber();
        userRepository.save(author);
        logger.info("> finished post:{} creating by :{}",post.getId(),post.getAuthor().getId());
        return postRepository.save(post);
    }

    @Transactional
    public Post remove(Integer id,int authorId) {
        Post post = findOne(id);
        if (post != null) {
            if(post.getAuthor().getId()==authorId){
                logger.info("> post:{} deleting by :{}",post.getId(),authorId);
                postRepository.deleteById(post.getId());
                User author = userRepository.findByIdForUpdate(authorId).orElseThrow(() -> new IllegalArgumentException("User not found"));
                author.incrementPostNumber();
                userRepository.save(author);
                logger.info("> finished post:{} deleting by :{}",post.getId(),post.getAuthor().getId());
                return post;
            }
        }
        return null;
    }

    @Transactional
    public Post modifyPost(int postId, Post modifiedPost) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
        post.setDescription(modifiedPost.getDescription());
        post.setImagePath(modifiedPost.getImagePath());
        post.setLocation(modifiedPost.getLocation());
        return postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Post> findAll(Pageable pageable) {
        Page<Post> posts=postRepository.findAll(pageable);
        posts.getContent().forEach(post -> Hibernate.initialize(post.getLikers()));
        return posts;
    }

    @Transactional
    public PostDTO findOneWithComments(Integer id) {
        Post post = postRepository.findOneWithComments(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return postDTOMapper.fromPosttoDTO(post); // mapiranje na DTO
    }
    
    public Post findOneWithLikers(Integer id){return postRepository.findOneWithLikers(id);} //nije radilo
    
    @Transactional
    public Post addLike(Integer id, User user) {
        Post post = findOne(id);
        if(post == null){
            return null;
        }

        if(post.getLikers().stream().anyMatch(t->t.getUsername().equalsIgnoreCase(user.getUsername()))){
            return null;
        }
        post.addLike(user);
        return postRepository.save(post);
    }

    @Transactional
    public Post removeLike(Integer id, User user) {
        Post post = findOne(id);
        if(post == null){
            return null;
        }
        if(!post.getLikers().stream().anyMatch(t->t.getUsername().equalsIgnoreCase(user.getUsername()))){
            return null;
        }
        User author = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(!author.equals(user)){
            return null;
        }
        post.removeLike(author);
        return postRepository.save(post);
    }

    @Transactional
    public Page<Post> findAllMy(Pageable page,int userId) {
        Page<Post> posts=postRepository.findAllMy(page,userId);
        posts.getContent().forEach(post -> Hibernate.initialize(post.getLikers()));
        return posts;
    }


    public int getNumberOfUserPosts(int id) {
        return postRepository.getPostByAuthorId(id);
    }



    public long getTotalPosts() {
        return postRepository.count();
    }

    public long getPostsLastMonth() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return postRepository.countPostsLastMonth(oneMonthAgo);
    }


    @Transactional(readOnly = true)
    public Post findMostLikedPost() {
        return postRepository.findAll()
                .stream()
                .max((post1, post2) -> Integer.compare(post1.getLikers().size(), post2.getLikers().size()))
                .orElseThrow(() -> new ResourceNotFoundException("No posts available"));
    }

    @Transactional
    public List<PostDTO> getTop10MostLikedPosts() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Post> posts = postRepository.findTop10MostLikedPosts(pageable);

        // Mapiranje entiteta Post u DTO PostDTO
        return posts.stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PostDTO> getTop5MostLikedPostsLastWeek(LocalDateTime startDate) {
        Pageable pageable = PageRequest.of(0, 5);
        List<Post> posts = postRepository.findTop5MostLikedPostsLastWeek(startDate, pageable);

        return posts.stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }




}

