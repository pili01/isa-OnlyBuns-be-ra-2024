package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post findOne(Integer id) {
        return postRepository.findById(id).orElseGet(null);
    }
    public Post save(Post post) {return postRepository.save(post);}
    public void remove(Integer id) {postRepository.deleteById(id);}
    public List<Post> findAll() {return postRepository.findAll();}
    public Page<Post> findAll(Pageable pageable) {return postRepository.findAll(pageable);}
    public Post findOneWithComments(Integer id) {return postRepository.findOneWithComments(id);}
    public Post findOneWithLikers(Integer id){return postRepository.findOneWithLikers(id);} //nije radilo
    @Transactional
    public Post addLike(Integer id, User user) {
        Post post = findOne(id);
        if(post == null){
            return null;
        }
        post.addLike(user);
        return postRepository.save(post);
    }
}
