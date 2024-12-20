package rs.ac.uns.ftn.informatika.jpa.custommq;

import java.util.LinkedList;

public class Queue {
    private final String name;
    private final LinkedList<Message> messages = new LinkedList<>();

    public Queue(String name) {
        this.name = name;
    }

    public synchronized void enqueue(Message message) {
        messages.add(message);
        System.out.println("Message added to queue [" + name + "]: " + message);
        notifyAll(); // Obaveštava potrošača da je nova poruka dostupna
    }

    public synchronized Message dequeue() {
        while (messages.isEmpty()) {
            try {
                wait(); // Čekaj dok ne stigne nova poruka
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return messages.poll();
    }

    public String getName() {
        return name;
    }
}
