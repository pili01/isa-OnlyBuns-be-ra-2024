package rs.ac.uns.ftn.informatika.jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rs.ac.uns.ftn.informatika.jpa.custommq.Exchange;
import rs.ac.uns.ftn.informatika.jpa.custommq.Queue;

@Configuration
public class QueueConfig {

    @Bean
    public Exchange rabbitCareExchange() {
        return new Exchange("RabbitCareExchange");
    }

    @Bean
    public Queue rabbitCareQueue() {
        return new Queue("RabbitCareQueue");
    }

    // Metoda za povezivanje reda sa razmenom
    public void bindQueueToExchange(Exchange exchange, Queue queue) {
        exchange.bindQueue("care", queue);
    }
}
