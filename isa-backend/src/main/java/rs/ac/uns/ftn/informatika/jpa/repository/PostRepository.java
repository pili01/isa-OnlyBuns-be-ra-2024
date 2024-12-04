package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Student;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Integer> {
    public Page<Post> findAll(Pageable pageable);

    @Query("select p from Post p join fetch p.likers l where p.id =?1")
    public Post findOneWithLikers(Integer postId); //nije radilo
    
    @Query("select p from Post p left join fetch p.comments where p.id =?1")
    public Optional<Post> findOneWithComments(Integer postId);

    // Custom query to update the imagePath
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.imagePath = :newImagePath WHERE p.imagePath = :oldImagePath")
    public int updateImagePath(@Param("oldImagePath") String oldImagePath, @Param("newImagePath") String newImagePath);
    
    @Query("select p from Post p where p.author.id = ?1")
    Page<Post> findAllMy(Pageable pageable, int userId);

    @Query("select count(p) from Post p where p.author.id = ?1")
    public int getPostByAuthorId(int id);

    @Query("SELECT new map(MONTH(p.createdAt) as month, COUNT(p) as count) " +
            "FROM Post p WHERE YEAR(p.createdAt) = :year GROUP BY MONTH(p.createdAt)")
    List<Map<String, Object>> countPostsByMonth(int year);

    @Query("SELECT EXTRACT(WEEK FROM p.createdAt) AS week, COUNT(p) AS count " +
            "FROM Post p WHERE EXTRACT(YEAR FROM p.createdAt) = :year AND EXTRACT(MONTH FROM p.createdAt) = :month " +
            "GROUP BY EXTRACT(WEEK FROM p.createdAt) ORDER BY week")
    List<Map<String, Object>> countPostsByWeek(int year, int month);

    @Query("SELECT EXTRACT(YEAR FROM p.createdAt) AS year, COUNT(p) AS count " +
            "FROM Post p GROUP BY EXTRACT(YEAR FROM p.createdAt) ORDER BY year")
    List<Map<String, Object>> countPostsByYear();

    long count();

    @Query("SELECT COUNT(p) FROM Post p WHERE p.createdAt >= :startDate")
    long countPostsLastMonth(@Param("startDate") LocalDateTime startDate);


    @Query(" SELECT p FROM Post p LEFT JOIN p.likers l GROUP BY p ORDER BY COUNT(l) DESC ")
    List<Post> findTop10MostLikedPosts(Pageable pageable);



    @Query("SELECT p FROM Post p LEFT JOIN p.likers l WHERE p.createdAt >= :startDate GROUP BY p ORDER BY COUNT(l) DESC ")
    List<Post> findTop5MostLikedPostsLastWeek(@Param("startDate") LocalDateTime startDate, Pageable pageable);






}
