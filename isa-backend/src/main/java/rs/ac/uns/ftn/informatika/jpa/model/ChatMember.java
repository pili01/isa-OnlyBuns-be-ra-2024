package rs.ac.uns.ftn.informatika.jpa.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@SQLDelete(sql
        = "UPDATE chat_member "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
@Entity
@Table(name = "chat_member")
public class ChatMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Chat chat;

    @Column(name = "deleted", nullable = false)
    private boolean isDeleted;

    private LocalDateTime joinedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    @Override
    public String toString() {
        return "ChatMember{" +
                "id=" + id +
                ", user=" + user +
                ", chat=" + chat +
                ", isDeleted=" + isDeleted +
                ", joinedAt=" + joinedAt +
                '}';
    }
}