package rs.ac.uns.ftn.informatika.jpa.custommq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.dto.PostDTO;

import java.util.Map;

@Component
public class AdvertisingProducer {

    private static final Logger log = LoggerFactory.getLogger(AdvertisingProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${advertising.exchange}")
    private String exchange;

    public AdvertisingProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPostToAdvertising(String message) {
        log.info("Sending post to advertising exchange: {}", message);
        rabbitTemplate.convertAndSend(exchange, "", message); // Routing key se ne koristi za fanout
    }
}
