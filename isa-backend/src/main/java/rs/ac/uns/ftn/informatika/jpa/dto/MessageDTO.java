package rs.ac.uns.ftn.informatika.jpa.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import rs.ac.uns.ftn.informatika.jpa.model.Chat;
import rs.ac.uns.ftn.informatika.jpa.model.ChatType;
import rs.ac.uns.ftn.informatika.jpa.model.Message;
import rs.ac.uns.ftn.informatika.jpa.model.User;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class MessageDTO {

    private int id;
    private String content;
    private LocalDateTime timestamp;

    @JsonIgnore
    private UserDTO sender;

    public MessageDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public UserDTO getSender() {
        return sender;
    }

    public void setSender(UserDTO sender) {
        this.sender = sender;
    }

}