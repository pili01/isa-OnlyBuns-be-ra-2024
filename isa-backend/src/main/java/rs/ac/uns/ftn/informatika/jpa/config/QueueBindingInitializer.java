package rs.ac.uns.ftn.informatika.jpa.config;

import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.jpa.custommq.Exchange;
import rs.ac.uns.ftn.informatika.jpa.custommq.Queue;

import javax.annotation.PostConstruct;

@Component
public class QueueBindingInitializer {

    private final Exchange exchange;
    private final Queue queue;

    public QueueBindingInitializer(Exchange exchange, Queue queue) {
        this.exchange = exchange;
        this.queue = queue;
    }

    @PostConstruct
    public void init() {
        exchange.bindQueue("care", queue);
        System.out.println("Queue bound to exchange: RabbitCareQueue -> RabbitCareExchange");
    }
}
