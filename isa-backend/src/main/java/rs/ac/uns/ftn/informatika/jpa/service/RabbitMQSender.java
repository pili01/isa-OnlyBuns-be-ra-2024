package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendMessage(String id, String name, String location) {
        // Formatiranje poruke kao JSON string
        String message = String.format("{\"id\":\"%s\", \"name\":\"%s\", \"location\":\"%s\"}", id, name, location);

        // Slanje poruke na Exchange sa routing key-om
        rabbitTemplate.convertAndSend("bunnyExchange", "bunny.routing.key", message);
        System.out.println("Message sent to RabbitMQ: " + message);
    }
}
