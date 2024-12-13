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

import java.time.LocalDateTime;
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
        chat.setLastActivity(LocalDateTime.now());
        chat.setName(sender.getUsername() + "-" + recipient.getUsername());
        Set<Message> messages = new HashSet<>();
        Set<User> participants = new HashSet<>();
        participants.add(sender);
        participants.add(recipient);
        chat.setParticipants(participants);
        chat.setMessages(messages);

        chat = chatRepository.save(chat);

        if (chat != null) {
            sender.addChat(chat);
            recipient.addChat(chat);
            userRepository.save(sender);
            userRepository.save(recipient);
        }

        System.out.println("Chat saved successfully with ID: " + chat.getId());
        return chat;
    }

    @Transactional(readOnly = false)
    public Chat getPrivateChat(String senderUsername, int recipientId, int chatId) {
        Optional<Chat> chat;
        if (chatId == -1) {
            User sender = userRepository.findByUsername(senderUsername).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            List<Integer> ids = new ArrayList<>(Arrays.asList(recipientId, sender.getId()));
            Collections.sort(ids);
            chat = chatRepository.findByCode(ids.get(0).toString() + "," + ids.get(1).toString());
        } else {
            chat = chatRepository.findById(chatId);
        }
        if (chat.isPresent()) {
            Hibernate.initialize(chat.get().getMessages());
            Hibernate.initialize(chat.get().getParticipants());
            return chat.get();
        }
        return createPrivateChat(senderUsername, recipientId);
    }

    @Transactional(readOnly = false)
    public Chat createGroupChat(String adminUsername, String chatName) {
        User admin = userRepository.findByUsername(adminUsername).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Chat chat = new Chat();
        chat.setName(chatName);
        chat.setAdmin(admin);
        chat.setType(ChatType.GROUP);
        Set<User> participants = new HashSet<>();
        participants.add(admin);
        chat.setParticipants(participants);
        chat.setLastActivity(LocalDateTime.now());
        Set<Message> messages = new HashSet<>();
        chat.setMessages(messages);

        chat = chatRepository.save(chat);

        if (chat != null) {
            admin.addChat(chat);
            userRepository.save(admin);
        }

        System.out.println("Group chat saved successfully with ID: " + chat.getId());

        return chat;
    }

    @Transactional(readOnly = false)
    public User addUserToGroup(int chatId, String username, int userId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        if (chat != null) {
            Hibernate.initialize(chat.getParticipants());
            if (Objects.equals(chat.getAdmin().getUsername(), username) && chat.getType() == ChatType.GROUP) {
                User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
                if (chat.addParticipant(user)) {
                    chatRepository.save(chat);
                    user.addChat(chat);
                    return userRepository.save(user);
                }
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Page<Message> getMessages(int chatId, Pageable pageable) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        return messageRepository.findAll(pageable);
    }

    @Transactional
    public Message saveMessage(int chatId, Message message) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        message.setChat(chat);
        message.setTimestamp(LocalDateTime.now());
        chat.setLastActivity(LocalDateTime.now());
        chatRepository.save(chat);
        return messageRepository.save(message);
    }

    @Transactional
    public Set<User> getChatParticipants(String username, Integer chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        if (chat.getType() == ChatType.GROUP) {
            Hibernate.initialize(chat.getParticipants());
            return chat.getParticipants();
        }
        return new HashSet<>();
    }

    @Transactional(readOnly = false)
    public User removeUserFromGroup(int chatId, String username, int userId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
        if (chat != null) {
            Hibernate.initialize(chat.getParticipants());
            if (Objects.equals(chat.getAdmin().getUsername(), username) && chat.getType() == ChatType.GROUP) {
                User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
                if (chat.removeParticipant(user)) {
                    chatRepository.save(chat);
                    user.removeChat(chat);
                    return userRepository.save(user);
                }
            }
        }
        return null;
    }
}