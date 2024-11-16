package rs.ac.uns.ftn.informatika.jpa.repositoryImplementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
}
