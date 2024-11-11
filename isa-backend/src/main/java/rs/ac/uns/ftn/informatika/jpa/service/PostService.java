package rs.ac.uns.ftn.informatika.jpa.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.jpa.ResourceNotFoundException;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.mapper.PostDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostDTOMapper postDTOMapper;

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public void remove(Integer id) {
        postRepository.deleteById(id);
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
}

