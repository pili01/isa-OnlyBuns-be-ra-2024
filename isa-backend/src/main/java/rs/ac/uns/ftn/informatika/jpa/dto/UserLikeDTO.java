package rs.ac.uns.ftn.informatika.jpa.dto;

import java.math.BigInteger;

public class UserLikeDTO {
    private Integer id;
    private String username;
    private String email;
    private BigInteger likeCount;

    public UserLikeDTO(Integer id, String username, String email, BigInteger likeCount) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.likeCount = likeCount;
    }

    // Getteri i setteri
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigInteger getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(BigInteger likeCount) {
        this.likeCount = likeCount;
    }
}

