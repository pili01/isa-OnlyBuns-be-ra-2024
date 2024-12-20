package rs.ac.uns.ftn.informatika.jpa.custommq;

public class Producer {
    private final Exchange exchange;

    public Producer(Exchange exchange) {
        this.exchange = exchange;
    }

    public void sendMessage(String routingKey, String id, String name, String location) {
        Message message = new Message(id, name, location);
        exchange.publish(routingKey, message);
    }
}

