package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.informatika.jpa.model.Post;
import rs.ac.uns.ftn.informatika.jpa.model.Student;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Integer> {
    public Page<Post> findAll(Pageable pageable);

    @Query("select p from Post p left join fetch p.comments where p.id =?1")
    public Optional<Post> findOneWithComments(Integer postId);
}
