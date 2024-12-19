package rs.ac.uns.ftn.informatika.jpa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.model.RabbitCareLocation;
import rs.ac.uns.ftn.informatika.jpa.repository.RabbitCareLocationRepository;

@Service
public class RabbitMQReceiver {

    private final RabbitCareLocationRepository repository;
    private final ObjectMapper objectMapper;

    public RabbitMQReceiver(RabbitCareLocationRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }


    @RabbitListener(queues = "bunnyQueue")
    public void receiveMessage(String message) {
        try {
            // Parsiranje JSON poruke u BunnyLocation objekat
            RabbitCareLocation location = objectMapper.readValue(message, RabbitCareLocation.class);

            // Čuvanje u bazi
            repository.save(location);
            System.out.println("Received and saved location: " + location);

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
}
