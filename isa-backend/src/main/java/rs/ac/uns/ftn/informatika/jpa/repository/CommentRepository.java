package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    public Page<Comment> findAll(Pageable pageable);
}
