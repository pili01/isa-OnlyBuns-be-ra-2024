package rs.ac.uns.ftn.informatika.jpa.custommq;

import rs.ac.uns.ftn.informatika.jpa.model.RabbitCareLocation;
import rs.ac.uns.ftn.informatika.jpa.repository.RabbitCareLocationRepository;

public class Consumer implements Runnable {
    private final Queue queue;
    private RabbitCareLocationRepository rabbitCareLocationRepository;

    public Consumer(Queue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            Message message = queue.dequeue(); // dequeue ne baca InterruptedException
            processMessage(message);
        }
    }

    private void processMessage(Message message) {
        System.out.println("Processing message: " + message);

        // Kreiraj i sačuvaj RabbitCareLocation u bazi
        RabbitCareLocation location = new RabbitCareLocation();
        location.setId(message.getId());
        location.setName(message.getName());
        location.setLocation(message.getLocation());

        // Sačuvaj u bazi pomoću repositorija
        rabbitCareLocationRepository.save(location);
        System.out.println("Saved RabbitCareLocation to database: " + location);
    }

    // Setter za injektovanje RabbitCareLocationRepository
    public void setRabbitCareLocationRepository(RabbitCareLocationRepository rabbitCareLocationRepository) {
        this.rabbitCareLocationRepository = rabbitCareLocationRepository;
    }
}
