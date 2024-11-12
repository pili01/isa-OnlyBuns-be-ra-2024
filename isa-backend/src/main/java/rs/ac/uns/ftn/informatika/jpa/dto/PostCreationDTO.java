package rs.ac.uns.ftn.informatika.jpa.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PostCreationDTO {
    private Integer id = 0;
    private String description;
    private String imagePath;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocationDTO location;
    private UserDTO author = new UserDTO();

    public PostCreationDTO(){}

    public PostCreationDTO(Integer id, String description, String imagePath, LocalDateTime createdAt, LocationDTO location) {
        this.id = id;
        this.description = description;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.location = location;
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

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }
}


