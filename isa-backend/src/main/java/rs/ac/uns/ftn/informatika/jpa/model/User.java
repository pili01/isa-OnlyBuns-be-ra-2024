package rs.ac.uns.ftn.informatika.jpa.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SQLDelete(sql
        = "UPDATE users "
        + "SET deleted = true "
        + "WHERE id = ?")
@Where(clause = "deleted = false")
@Entity
@Table(name = "users")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "deleted", nullable = false)
    private boolean isDeleted;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    @Column(name = "number_of_posts")
    private int numberOfPosts;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @Column(name = "following_count")
    private int followingCount = 0;

    @Column(name = "followers_count")
    private int followersCount = 0;

    @Column(name = "last_logged_time")
    private LocalDateTime lastLoggedTime;

    // Lista korisnika koji me prate
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "followers",
            joinColumns = @JoinColumn(name = "followed_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<User> followMe = new HashSet<>();

    // Lista korisnika koje ja pratim
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "followings",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id")
    )
    private Set<User> followByMe = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Post> posts = new HashSet<Post>();

    @ManyToMany
    private Set<Chat> chats=new HashSet<Chat>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getNumberOfPosts() {
        return numberOfPosts;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public Set<User> getFollowMe() {
        return followMe;
    }

    public void setFollowMe(Set<User> followMe) {
        this.followMe = followMe;
    }

    public Set<User> getFollowByMe() {
        return followByMe;
    }

    public void setFollowByMe(Set<User> followByMe) {
        this.followByMe = followByMe;
    }

    public void setNumberOfPosts(int numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getLastLoggedTime() {
        return lastLoggedTime;
    }

    public void setLastLoggedTime(LocalDateTime lastLoggedTime) {
        this.lastLoggedTime = lastLoggedTime;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(this.role);
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void incrementFollowersCount() {
        followersCount++;
    }

    public void decrementFollowersCount() {
        followersCount--;
    }

    public void incrementFollowingCount() {
        followingCount++;
    }

    public void decrementFollowingCount() {
        followingCount--;
    }

    public void decrementPostNumber() {
        numberOfPosts--;
    }

    public void incrementPostNumber() {
        numberOfPosts++;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Post addPost(Post post) {
        posts.add(post);
        numberOfPosts++;
        post.setAuthor(this);
        return post;
    }

    public Chat addChat(Chat chat) {
        chats.add(chat);
        return chat;
    }

    public Chat removeChat(Chat chat) {
        chats.remove(chat);
        return chat;
    }

    public void removePost(Post post) {
        posts.remove(post);
        numberOfPosts--;
        post.setDeleted(true);
    }

    // Pre brisanja korisnika postavi sve postove na isDeleted = true
    @PreRemove
    private void preRemove() {
        for (Post post : posts) {
            post.setDeleted(true);
        }
        for (Chat chat : chats) {
            chat.setDeleted(true);
        }
    }

    public Set<Chat> getChats() {
        return chats;
    }

    public void setChats(Set<Chat> chats) {
        this.chats = chats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isDeleted == user.isDeleted &&
                Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(address, user.address) &&
                Objects.equals(email, user.email) &&
                Objects.equals(enabled, user.enabled) &&
                Objects.equals(role, user.role);
    }

    @Override
    public String toString() {
        return "\nUser{" +  "\n\t" +
                "id=" + id + "\n\t" +
                ", username='" + username + '\'' + "\n\t" +
                ", password='" + password + '\'' + "\n\t" +
                ", firstName='" + firstName + '\'' + "\n\t" +
                ", lastName='" + lastName + '\'' + "\n\t" +
                ", address='" + address + '\'' + "\n\t" +
                ", email='" + email + '\'' + "\n\t" +
                ", enabled=" + enabled + "\n\t" +
                ", isDeleted=" + isDeleted + "\n\t" +
                ", numberOfPosts=" + numberOfPosts + "\n\t" +
                ", creationTime=" + creationTime + "\n\t" +
                ", lastLoggedTime=" + lastLoggedTime + "\n\t" +
                ", followingCount=" + followingCount + "\n\t" +
                ", followersCount=" + followersCount + "\n\t" +
                ", role=" + role + "\n" +
                '}' + "\n";
    }
}
