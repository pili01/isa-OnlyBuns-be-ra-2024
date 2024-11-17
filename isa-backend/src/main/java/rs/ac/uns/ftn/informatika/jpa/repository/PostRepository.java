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

    @Query("SELECT COUNT(p) FROM Post p WHERE p.createdAt >= CURRENT_DATE - 7")
    long countPostsByWeek();

    @Query("SELECT COUNT(p) FROM Post p WHERE p.createdAt >= CURRENT_DATE - 30")
    long countPostsByMonth();

    @Query("SELECT COUNT(p) FROM Post p WHERE p.createdAt >= CURRENT_DATE - 365")
    long countPostsByYear();
}
