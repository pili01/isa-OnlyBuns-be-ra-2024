package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    public Comment save(Comment comment) {return commentRepository.save(comment);}
    public Comment remove(Integer id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isPresent()) {
            commentRepository.deleteById(id);
            return comment.get();
        }
        return null;
    }
    public List<Comment> findAll() {return commentRepository.findAll();}
    public Page<Comment> findAll(Pageable pageable) {return commentRepository.findAll(pageable);}
}
