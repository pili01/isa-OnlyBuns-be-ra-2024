package rs.ac.uns.ftn.informatika.jpa.controller;

import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.dto.ChatDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.MessageDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.UserDTO;
import rs.ac.uns.ftn.informatika.jpa.mapper.ChatDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.mapper.MessageDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.mapper.PostDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.mapper.UserDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.model.Message;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.ChatRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.MessageRepository;
import rs.ac.uns.ftn.informatika.jpa.service.ChatService;
import rs.ac.uns.ftn.informatika.jpa.service.UserService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/chat")
public class ChatController {

    @Autowired
    private final MessageRepository messageRepository;
    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatDTOMapper chatDTOMapper;

    @Autowired
    private MessageDTOMapper messageDTOMapper;
    @Autowired
    private UserDTOMapper userDTOMapper;

    public ChatController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    @GetMapping(value = "/get/{userId}/{chatId}")
    public ResponseEntity<ChatDTO> getChat(@PathVariable Integer userId, @PathVariable Integer chatId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ChatDTO chat = chatService.getPrivateChat(username, userId, chatId);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    @GetMapping(value = "/getParticipants/{chatId}")
    public ResponseEntity<List<UserDTO>> getChat(@PathVariable Integer chatId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Set<User> users = chatService.getChatParticipants(username, chatId);

        List<UserDTO> usersDTO = users.stream()
                .map(userDTOMapper::fromUsertoDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    @PostMapping(value = "/addUserToChat/{chatId}/{userId}")
    public ResponseEntity<UserDTO> addParticipantToChat(@PathVariable Integer chatId, @PathVariable Integer userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO userDTO = chatService.addUserToGroup(chatId, username, userId);
        if (userDTO == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    @PostMapping(value = "/removeUserFromChat/{chatId}/{userId}")
    public ResponseEntity<UserDTO> removeParticipantFromChat(@PathVariable Integer chatId, @PathVariable Integer userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO userDTO = chatService.removeUserFromGroup(chatId, username, userId);
        if (userDTO == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    @PostMapping(value = "/{chatName}")
    public ResponseEntity<ChatDTO> createGroupChat(@PathVariable String chatName) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ChatDTO chat = chatService.createGroupChat(username, chatName);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }
}
