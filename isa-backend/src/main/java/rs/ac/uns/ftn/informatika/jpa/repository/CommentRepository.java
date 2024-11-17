package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    public Page<Comment> findAll(Pageable pageable);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.createdAt >= CURRENT_DATE - 7")
    long countCommentsByWeek();

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.createdAt >= CURRENT_DATE - 30")
    long countCommentsByMonth();

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.createdAt >= CURRENT_DATE - 365")
    long countCommentsByYear();
}
