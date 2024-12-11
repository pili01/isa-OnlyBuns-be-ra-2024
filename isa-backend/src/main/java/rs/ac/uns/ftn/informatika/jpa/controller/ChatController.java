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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.jpa.dto.ChatDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;
import rs.ac.uns.ftn.informatika.jpa.mapper.ChatDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.model.Message;
import rs.ac.uns.ftn.informatika.jpa.repository.ChatRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.MessageRepository;
import rs.ac.uns.ftn.informatika.jpa.service.ChatService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "api/chat")
public class ChatController {

    @Autowired
    private final MessageRepository messageRepository;
    @Autowired
    private ChatService chatService;
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatDTOMapper chatDTOMapper;

    public ChatController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PreAuthorize("hasAuthority('AUTHENTICATED')")
    @GetMapping(value = "/get/{userId}/{chatId}")
    public ResponseEntity<ChatDTO> getChat(@PathVariable Integer userId, @PathVariable Integer chatId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ChatDTO chat = chatDTOMapper.fromChatToDTO(chatService.getPrivateChat(username, userId, chatId));

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

//    @PreAuthorize("hasAnyAuthority('AUTHENTICATED')")
    @MessageMapping("/chat.sendMessage/{chatId}")
    @SendTo("/topic/chat/{chatId}")
    public Message sendMessage(@DestinationVariable int chatId, @Payload String content) {
         // Dobavljanje korisničkog imena iz Principal objekta
        Message message = new Message();
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        //message.setSender(username); // Postavljanje korisničkog imena kao pošiljaoca
        return message;
    }
}
