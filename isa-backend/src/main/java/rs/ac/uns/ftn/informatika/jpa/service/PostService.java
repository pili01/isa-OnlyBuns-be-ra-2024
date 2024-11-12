package rs.ac.uns.ftn.informatika.jpa.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import java.util.List;
import java.util.Set;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostDTOMapper postDTOMapper;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Post findOne(Integer id) {
        return postRepository.findById(id).orElseGet(null);
    }
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public Post remove(Integer id,int authorId) {
        Post post = findOne(id);
        if (post != null) {
            if(post.getAuthor().getId()==authorId){
                postRepository.deleteById(post.getId());
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
}

