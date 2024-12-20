package rs.ac.uns.ftn.informatika.jpa.custommq;

import java.util.HashMap;
import java.util.Map;

public class Exchange {
    private final String name;
    private final Map<String, Queue> bindings = new HashMap<>();

    public Exchange(String name) {
        this.name = name;
    }

    public void bindQueue(String routingKey, Queue queue) {
        bindings.put(routingKey, queue);
    }

    public void publish(String routingKey, Message message) {
        Queue queue = bindings.get(routingKey);
        if (queue != null) {
            queue.enqueue(message);
        } else {
            System.err.println("No queue bound to routing key [" + routingKey + "]");
        }
    }
}
