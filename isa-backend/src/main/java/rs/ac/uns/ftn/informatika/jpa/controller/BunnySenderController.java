package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.jpa.service.RabbitMQSender;

@RestController
public class BunnySenderController {

    private final RabbitMQSender rabbitMQSender;

    public BunnySenderController(RabbitMQSender rabbitMQSender) {
        this.rabbitMQSender = rabbitMQSender;
    }

    @PostMapping ("/send")
    public String sendMessage(@RequestParam String id, @RequestParam String name, @RequestParam String location) {
        rabbitMQSender.sendMessage(id, name, location);
        return "Message sent: id=" + id + ", name=" + name + ", location=" + location;
    }
}
