package rs.ac.uns.ftn.informatika.jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rs.ac.uns.ftn.informatika.jpa.custommq.Exchange;
import rs.ac.uns.ftn.informatika.jpa.custommq.Producer;


@Configuration
public class ProducerConfig {

    @Bean
    public Producer producer(Exchange rabbitCareExchange) {
        return new Producer(rabbitCareExchange);
    }
}

