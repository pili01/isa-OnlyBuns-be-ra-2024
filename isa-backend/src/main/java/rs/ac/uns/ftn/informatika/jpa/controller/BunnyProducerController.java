package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.service.BunnyProducerService;

@RestController
@RequestMapping("/api/messages")
public class BunnyProducerController {
    private final BunnyProducerService producerService;

    public BunnyProducerController(BunnyProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam String location) {
        producerService.sendMessage("care", id, name, location);
        return ResponseEntity.ok("Message sent: id=" + id + ", name=" + name + ", location=" + location);
    }
}
