package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    public Comment save(Comment comment) {return commentRepository.save(comment);}
    public void remove(Integer id) {commentRepository.deleteById(id);}
    public List<Comment> findAll() {return commentRepository.findAll();}
    public Page<Comment> findAll(Pageable pageable) {return commentRepository.findAll(pageable);}
}
