package rs.ac.uns.ftn.informatika.jpa.repositoryImplementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<User> findAllWithFilters(Pageable pageable, String firstName, String lastName, String email, Integer minPosts, Integer maxPosts, String sort) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (firstName != null && !firstName.isEmpty()) {
            predicates.add(cb.like(cb.lower(user.get("firstName")), "%" + firstName.toLowerCase() + "%"));
        }
        if (lastName != null && !lastName.isEmpty()) {
            predicates.add(cb.like(cb.lower(user.get("lastName")), "%" + lastName.toLowerCase() + "%"));
        }
        if (email != null && !email.isEmpty()) {
            predicates.add(cb.like(cb.lower(user.get("email")), "%" + email.toLowerCase() + "%"));
        }
        if (minPosts != null) {
            predicates.add(cb.greaterThanOrEqualTo(user.get("numberOfPosts"), minPosts));
        }
        if (maxPosts != null) {
            predicates.add(cb.lessThanOrEqualTo(user.get("numberOfPosts"), maxPosts));
        }

        query.where(predicates.toArray(new Predicate[0]));

        if ("emaildesc".equalsIgnoreCase(sort)) {
            query.orderBy(cb.desc(user.get("email")));
        } else if ("emailasc".equalsIgnoreCase(sort)) {
            query.orderBy(cb.asc(user.get("email")));
        } else if ("numprdesc".equalsIgnoreCase(sort)) {
            query.orderBy(cb.desc(user.get("followingCount")));
        } else if ("numprasc".equalsIgnoreCase(sort)) {
            query.orderBy(cb.asc(user.get("followingCount")));
        }

        List<User> result = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(result, pageable, result.size());
    }

    @Override
    public Page<User> findAllFollowersWithFilters(Pageable pageable, String firstName, String lastName, String email, Integer minPosts, Integer maxPosts, String sort, int userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> userRoot = query.from(User.class);
        userRoot.fetch("posts", JoinType.LEFT); // Fetch posts

        // Lista predikata za filtriranje
        List<Predicate> predicates = new ArrayList<>();

        // Dodavanje predikata za praćene korisnike
        Subquery<Integer> subquery = query.subquery(Integer.class);
        Root<User> subRoot = subquery.from(User.class);
        Join<Object, Object> followByMeJoin = subRoot.join("followByMe");
        subquery.select(subRoot.get("id")).where(cb.equal(followByMeJoin.get("id"), userId));

        predicates.add(userRoot.get("id").in(subquery));

        // Filtriranje prema firstName
        if (firstName != null && !firstName.isEmpty()) {
            predicates.add(cb.like(cb.lower(userRoot.get("firstName")), "%" + firstName.toLowerCase() + "%"));
        }

        // Filtriranje prema lastName
        if (lastName != null && !lastName.isEmpty()) {
            predicates.add(cb.like(cb.lower(userRoot.get("lastName")), "%" + lastName.toLowerCase() + "%"));
        }

        // Filtriranje prema email
        if (email != null && !email.isEmpty()) {
            predicates.add(cb.like(cb.lower(userRoot.get("email")), "%" + email.toLowerCase() + "%"));
        }

        // Filtriranje prema broju postova
        if (minPosts != null) {
            predicates.add(cb.greaterThanOrEqualTo(userRoot.get("numberOfPosts"), minPosts));
        }
        if (maxPosts != null) {
            predicates.add(cb.lessThanOrEqualTo(userRoot.get("numberOfPosts"), maxPosts));
        }

        // Dodavanje predikata u upit
        query.where(predicates.toArray(new Predicate[0]));

        // Dodavanje sortiranja
        if (sort != null && sort.equalsIgnoreCase("asc")) {
            query.orderBy(cb.asc(userRoot.get("firstName")));
        } else {
            query.orderBy(cb.desc(userRoot.get("firstName")));
        }

        // Izvršavanje upita
        List<User> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // Brojanje ukupnih rezultata
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);
        countRoot.join("followByMe");
        countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public Page<User> findAllFollowingsWithFilters(Pageable pageable, String firstName, String lastName, String email, Integer minPosts, Integer maxPosts, String sort, int userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> userRoot = query.from(User.class);
        userRoot.fetch("posts", JoinType.LEFT); // Fetch posts

        // Lista predikata za filtriranje
        List<Predicate> predicates = new ArrayList<>();

        // Dodavanje predikata za praćene korisnike
        Subquery<Integer> subquery = query.subquery(Integer.class);
        Root<User> subRoot = subquery.from(User.class);
        Join<Object, Object> followMeJoin = subRoot.join("followMe");
        subquery.select(subRoot.get("id")).where(cb.equal(followMeJoin.get("id"), userId));

        predicates.add(userRoot.get("id").in(subquery));

        // Filtriranje prema firstName
        if (firstName != null && !firstName.isEmpty()) {
            predicates.add(cb.like(cb.lower(userRoot.get("firstName")), "%" + firstName.toLowerCase() + "%"));
        }

        // Filtriranje prema lastName
        if (lastName != null && !lastName.isEmpty()) {
            predicates.add(cb.like(cb.lower(userRoot.get("lastName")), "%" + lastName.toLowerCase() + "%"));
        }

        // Filtriranje prema email
        if (email != null && !email.isEmpty()) {
            predicates.add(cb.like(cb.lower(userRoot.get("email")), "%" + email.toLowerCase() + "%"));
        }

        // Filtriranje prema broju postova
        if (minPosts != null) {
            predicates.add(cb.greaterThanOrEqualTo(userRoot.get("numberOfPosts"), minPosts));
        }
        if (maxPosts != null) {
            predicates.add(cb.lessThanOrEqualTo(userRoot.get("numberOfPosts"), maxPosts));
        }

        // Dodavanje predikata u upit
        query.where(predicates.toArray(new Predicate[0]));

        // Dodavanje sortiranja
        if (sort != null && sort.equalsIgnoreCase("asc")) {
            query.orderBy(cb.asc(userRoot.get("firstName")));
        } else {
            query.orderBy(cb.desc(userRoot.get("firstName")));
        }

        // Izvršavanje upita
        List<User> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // Brojanje ukupnih rezultata
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);
        countRoot.join("followByMe");
        countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }
}
