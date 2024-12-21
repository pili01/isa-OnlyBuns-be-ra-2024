package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.informatika.jpa.model.Role;
import rs.ac.uns.ftn.informatika.jpa.model.User;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User findByEmail(String email);

    boolean existsById(int id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.id = :id")
    Optional<User> findByIdForUpdate(int id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsernameForUpdate(String username);

    Optional<User> findByUsername(String username);

    @Query("select u from User u where u.enabled = false")
    List<User> findAllUnverified();

    @Query("SELECT COUNT(u) FROM User u WHERE EXISTS (SELECT p FROM Post p WHERE p.author = u)")
    long countUsersWithPosts();

    @Query("SELECT COUNT(u) FROM User u WHERE EXISTS (SELECT c FROM Comment c WHERE c.author = u) AND NOT EXISTS (SELECT p FROM Post p WHERE p.author = u)")
    long countUsersWithCommentsOnly();

    @Query("SELECT u FROM User u JOIN u.followByMe f WHERE f.id = :userId")
    List<User> findFollowersByUserId(@Param("userId") Integer userId);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) and u.role.id=2")
    Page<User> findAllByUsernameContainingIgnoreCase(Pageable pageable, @Param("search") String search);



    @Query("SELECT COUNT(*) FROM User ")
    long countAllUsers();


}
