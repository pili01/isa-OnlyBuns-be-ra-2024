package rs.ac.uns.ftn.informatika.jpa.dto;

import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class CommentDTO {
    private Integer id;

    @NotBlank(message = "Comment is required")
    @Size(min = 1, max = 255, message = "Comment must be between 1 and 255 characters")
    private String comment;

    @Past
    private LocalDateTime createdAt = LocalDateTime.now();
    private String authorUsername;

    public CommentDTO() {
    }

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreatedAt();
        this.authorUsername = comment.getAuthor().getUsername();
    }

    public CommentDTO(Integer id, String comment, LocalDateTime createdAt, String authorUsername) {
        this.id = id;
        this.comment = comment;
        this.createdAt = createdAt;
        this.authorUsername = authorUsername;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotBlank(message = "Comment is required") @Size(min = 1, max = 255, message = "Comment must be between 1 and 255 characters") String getComment() {
        return comment;
    }

    public void setComment(@NotBlank(message = "Comment is required") @Size(min = 1, max = 255, message = "Comment must be between 1 and 255 characters") String comment) {
        this.comment = comment;
    }

    public @Past LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@Past LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }
}
