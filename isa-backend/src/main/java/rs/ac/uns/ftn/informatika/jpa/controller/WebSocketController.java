package rs.ac.uns.ftn.informatika.jpa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rs.ac.uns.ftn.informatika.jpa.dto.MessageDTO;
import rs.ac.uns.ftn.informatika.jpa.mapper.MessageDTOMapper;
import rs.ac.uns.ftn.informatika.jpa.model.Message;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.service.ChatService;
import rs.ac.uns.ftn.informatika.jpa.service.UserService;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageDTOMapper messageDTOMapper;

    /*// REST enpoint
    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value="/sendMessageRest", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> message) {
        if (message.containsKey("message")) {
            if (message.containsKey("toId") && message.get("toId") != null && !message.get("toId").equals("")) {
                this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + message.get("toId"), message);
                this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + message.get("fromId"), message);
            } else {
                this.simpMessagingTemplate.convertAndSend("/socket-publisher", message);
            }
            return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
        }

        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /*
     * WebSockets endpoint
     *
     * Kao sto smo koristili @RequestMapping za RestController, @MessageMapping se koristi za websocket-e
     *
     * Poruka ce biti poslata svim klijentima koji su pretplatili na /socket-publisher topic,
     * a poruka koja im se salje je messageConverted (simpMessagingTemplate.convertAndSend metoda).
     *
     * Na ovaj endpoint klijenti salju poruke, ruta na koju klijenti salju poruke je /send/message (parametar @MessageMapping anotacije)
     *
     */
    @MessageMapping("/send/message")
    public MessageDTO broadcastNotification(String message) {
        Map<String, Object> messageConverted = parseMessage(message);
        MessageDTO messageDTO = new MessageDTO();
        if (messageConverted != null) {
            if (messageConverted.containsKey("senderUsername") && messageConverted.get("chatId") != null
                    && !messageConverted.get("senderUsername").equals("")) {
                String userName = messageConverted.get("senderUsername").toString();
                Optional<User> user = userService.findByUsername(userName);

                Message messageToSave = new Message();
                String content = messageConverted.get("content").toString();
                if (user.isPresent()) {
                    messageToSave.setSender(user.get());
                    messageToSave.setContent(content);
                    int chatId = Integer.parseInt(messageConverted.get("chatId").toString());
                    messageDTO = (chatService.saveMessage(chatId, messageToSave));
                    this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + (messageConverted.get("chatId")).toString(),
                            messageConverted);
                }
            } else {
                this.simpMessagingTemplate.convertAndSend("/socket-publisher", messageConverted);
            }
        }

        return messageDTO;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> retVal;

        try {
            retVal = mapper.readValue(message, Map.class); // Parsiranje JSON stringa
        } catch (IOException e) {
            retVal = null;
        }

        return retVal;
    }

}
