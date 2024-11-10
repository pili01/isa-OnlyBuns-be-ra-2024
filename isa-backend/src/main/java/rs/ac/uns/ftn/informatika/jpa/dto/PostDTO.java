package rs.ac.uns.ftn.informatika.jpa.dto;
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
    private Set<CommentDTO> comments = new HashSet<>();
    private UserDTO author = new UserDTO();
//    private String authorUsername;
    private Set<UserDTO> likers = new HashSet<>();

    public PostDTO() {
    }

    //za dodavanje
    public PostDTO(Post post) {
        this.id = post.getId();
        this.description = post.getDescription();
        this.imagePath = post.getImagePath();
        this.createdAt = post.getCreatedAt();
        this.location = new LocationDTO(post.getLocation());
    }

    public PostDTO(Integer id, String description, String imagePath, LocalDateTime createdAt, LocationDTO location) {
        this.id = id;
        this.description = description;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.location = location;
    }

    // za prikaz
    public PostDTO(Integer id, String description, String imagePath, LocalDateTime createdAt, LocationDTO location, Set<CommentDTO> comments, UserDTO author, Set<UserDTO> likers) {
        this.id = id;
        this.description = description;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.location = location;
        this.comments = comments;
        this.author = author;
        this.likers = likers;
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

    public int getLikes() {
        return likers.size();
    }

    public Set<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(Set<CommentDTO> comments) {
        this.comments = comments;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }

    public Set<UserDTO> getLikers() {
        return likers;
    }

    public void setLikers(Set<UserDTO> likers) {
        this.likers = likers;
    }
}
