package rs.ac.uns.ftn.informatika.jpa.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Kreiranje reda
    @Bean
    public Queue bunnyQueue() {
        return new Queue("bunnyQueue", false); // false znači da red nije trajan
    }

    // Kreiranje Direct Exchange-a
    @Bean
    public DirectExchange bunnyExchange() {
        return new DirectExchange("bunnyExchange");
    }

    // Povezivanje reda sa Exchange-om putem routing key-a
    @Bean
    public Binding binding(Queue bunnyQueue, DirectExchange bunnyExchange) {
        return BindingBuilder.bind(bunnyQueue).to(bunnyExchange).with("bunny.routing.key");
    }
}
