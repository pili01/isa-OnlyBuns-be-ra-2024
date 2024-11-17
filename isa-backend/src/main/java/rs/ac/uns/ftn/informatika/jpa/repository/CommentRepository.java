package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;

import java.util.List;
import java.util.Map;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    public Page<Comment> findAll(Pageable pageable);

    @Query("SELECT new map(MONTH(c.createdAt) as month, COUNT(c) as count) " +
            "FROM Comment c WHERE YEAR(c.createdAt) = :year GROUP BY MONTH(c.createdAt)")
    List<Map<String, Object>> countCommentsByMonth(int year);

    @Query("SELECT EXTRACT(WEEK FROM c.createdAt) AS week, COUNT(c) AS count " +
            "FROM Comment c WHERE EXTRACT(YEAR FROM c.createdAt) = :year AND EXTRACT(MONTH FROM c.createdAt) = :month " +
            "GROUP BY EXTRACT(WEEK FROM c.createdAt) ORDER BY week")
    List<Map<String, Object>> countCommentsByWeek(int year, int month);

    @Query("SELECT EXTRACT(YEAR FROM c.createdAt) AS year, COUNT(c) AS count " +
            "FROM Comment c GROUP BY EXTRACT(YEAR FROM c.createdAt) ORDER BY year")
    List<Map<String, Object>> countCommentsByYear();
}
