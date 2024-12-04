package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.informatika.jpa.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public interface UserRepositoryCustom {
    @Query("select u from User u left join fetch u.posts")
    Page<User> findAllWithFilters(Pageable pageable, String firstName, String lastName, String email, Integer minPosts, Integer maxPosts, String sort);


    @Query("select u from User u left join fetch u.posts where u.id  in " +
            "(select u.id from User u join u.followByMe f where f.id = :userId)")
    Page<User> findAllFollowersWithFilters(Pageable pageable, String firstName, String lastName, String email, Integer minPosts, Integer maxPosts, String sort,int userId);

    @Query("select u from User u left join fetch u.posts")
    Page<User> findAllFollowingsWithFilters(Pageable pageable, String firstName, String lastName, String email, Integer minPosts, Integer maxPosts, String sort,int userId);
}

