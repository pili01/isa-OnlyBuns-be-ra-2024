package rs.ac.uns.ftn.informatika.jpa.dto;
import rs.ac.uns.ftn.informatika.jpa.model.Comment;
import rs.ac.uns.ftn.informatika.jpa.model.Post;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PostDTO {
    private Integer id;
    private String description;
    private String imagePath;
    private LocalDateTime createdAt;
    private LocationDTO location;
    private String authorUsername;
    private int likes;
    private Set<CommentDTO> comments=new HashSet<>();

    public PostDTO() {
    }

    public PostDTO(Post post) {
        this.id = post.getId();
        this.description = post.getDescription();
        this.imagePath = post.getImagePath();
        this.createdAt = post.getCreatedAt();
        this.location = new LocationDTO(post.getLocation());
        this.authorUsername = post.getAuthor().getUsername();
        this.likes = 0;
    }

    public PostDTO(Integer id, String description, String imagePath, LocalDateTime createdAt, LocationDTO location, String authorUsername, int likes) {
        this.id = id;
        this.description = description;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.location = location;
        this.authorUsername = authorUsername;
        this.likes = likes;
    }

    public Set<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(Set<CommentDTO> comments) {
        this.comments = comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
