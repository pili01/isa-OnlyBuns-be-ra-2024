package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.custommq.Consumer;
import rs.ac.uns.ftn.informatika.jpa.custommq.Queue;
import rs.ac.uns.ftn.informatika.jpa.repository.RabbitCareLocationRepository;

import javax.annotation.PostConstruct;

@Service
public class BunnyConsumerService {

    private final Queue rabbitCareQueue;
    private final RabbitCareLocationRepository rabbitCareLocationRepository;

    public BunnyConsumerService(Queue rabbitCareQueue, RabbitCareLocationRepository rabbitCareLocationRepository) {
        this.rabbitCareQueue = rabbitCareQueue;
        this.rabbitCareLocationRepository = rabbitCareLocationRepository;
    }

    @PostConstruct
    public void startConsumer() {
        Consumer consumer = new Consumer(rabbitCareQueue);
        consumer.setRabbitCareLocationRepository(rabbitCareLocationRepository);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
    }
}
