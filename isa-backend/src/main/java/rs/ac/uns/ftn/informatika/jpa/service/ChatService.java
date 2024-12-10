package rs.ac.uns.ftn.informatika.jpa.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.informatika.jpa.ResourceNotFoundException;
import rs.ac.uns.ftn.informatika.jpa.model.*;
import rs.ac.uns.ftn.informatika.jpa.repository.ChatRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.MessageRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepository;

import java.util.*;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Transactional(readOnly = false)
    public Chat createPrivateChat(String senderUsername, int recipientId) {
        User sender = userRepository.findByUsername(senderUsername).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Integer> ids = new ArrayList<>(Arrays.asList(recipientId, sender.getId()));
        Collections.sort(ids);
        Chat chat = new Chat();
        chat.setCode(ids.get(0).toString() + "," + ids.get(1).toString());
        chat.setType(ChatType.PRIVATE);
        chat.setAdmin(sender);
        chat.setName(sender.getEmail() + "-" + recipient.getEmail());
        Set<Message> messages = new HashSet<>();
        Set<User> participants = new HashSet<>();
        participants.add(sender);
        participants.add(recipient);
        chat.setParticipants(participants);
        chat.setMessages(messages);

        chat = chatRepository.save(chat);
        System.out.println("Chat saved successfully with ID: " + chat.getId());
        return chat;
    }

    @Transactional(readOnly = false)
    public Chat getPrivateChat(String senderUsername, int recipientId) {
        User sender = userRepository.findByUsername(senderUsername).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Integer> ids = new ArrayList<>(Arrays.asList(recipientId, sender.getId()));
        Collections.sort(ids);
        Optional<Chat> chat = chatRepository.findByCode(ids.get(0).toString() + "," + ids.get(1).toString());
        if (chat.isPresent()) {
            Hibernate.initialize(chat.get().getMessages());
            Hibernate.initialize(chat.get().getParticipants());
            return chat.get();
        }
        return createPrivateChat(senderUsername, recipientId);
    }

    public Chat createGroupChat(String adminUsername, String chatName) {
        User admin = userRepository.findByUsername(adminUsername).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Chat chat = new Chat();
        chat.setName(chatName);
        chat.setAdmin(admin);
        Set<User> participants = new HashSet<>();
        participants.add(admin);
        chat.setParticipants(participants);

        chatRepository.save(chat);
        return chat;
    }

    public void addUserToGroup(int chatId, String username) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        chat.getParticipants().add(user);
        chatRepository.save(chat);
    }

    @Transactional(readOnly = true)
    public Page<Message> getMessages(int chatId, Pageable pageable) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        return messageRepository.findAll(pageable);
    }
}