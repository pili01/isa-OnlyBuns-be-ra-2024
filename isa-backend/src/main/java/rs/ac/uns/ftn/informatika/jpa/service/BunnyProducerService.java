package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.custommq.Exchange;
import rs.ac.uns.ftn.informatika.jpa.custommq.Message;

@Service
public class BunnyProducerService {
    private final Exchange exchange;

    public BunnyProducerService(Exchange exchange) {
        this.exchange = exchange;
    }

    public void sendMessage(String routingKey, String name, String location) {
        exchange.publish(routingKey, new Message(name, location));
    }
}
