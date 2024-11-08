package rs.ac.uns.ftn.informatika.jpa.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/* primer logickog brisanja
 *
 * Prilikom poziva delete() metode repozitorijuma, okidace se ovaj upit koji radi soft delete
 * tako sto menja status deleted polja sa false na true.
 */
@SQLDelete(sql
        = "UPDATE comments "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")

@Entity
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "creationTime", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment1 = (Comment) o;
        return isDeleted == comment1.isDeleted && Objects.equals(id, comment1.id) && Objects.equals(comment, comment1.comment) && Objects.equals(createdAt, comment1.createdAt) && Objects.equals(author, comment1.author) && Objects.equals(post, comment1.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, comment, isDeleted, createdAt, author, post);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", isDeleted=" + isDeleted +
                ", createdAt=" + createdAt +
                ", author=" + author +
                ", post=" + post +
                '}';
    }
}
