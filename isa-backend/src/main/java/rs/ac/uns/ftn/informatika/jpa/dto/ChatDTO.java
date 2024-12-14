package rs.ac.uns.ftn.informatika.jpa.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import rs.ac.uns.ftn.informatika.jpa.model.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ChatDTO {

    private int id;
    private ChatType type; // Korisnički ili grupni čet
    private String name;
    private String adminUsername;
    @JsonIgnore
    private Set<UserDTO> participants=new HashSet<>();
    private Set<MessageDTO> messages=new HashSet<>();
    private LocalDateTime lastActivity;

    public ChatDTO() {
    }

    //za dodavanje
    public ChatDTO(Chat chat) {
        this.id = chat.getId();
        this.type = chat.getType();
        this.name = chat.getName();
        this.adminUsername=chat.getAdmin().getUsername();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ChatType getType() {
        return type;
    }

    public void setType(ChatType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public Set<UserDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<UserDTO> participants) {
        this.participants = participants;
    }

    public Set<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(Set<MessageDTO> messages) {
        this.messages = messages;
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }
}
