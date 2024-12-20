package rs.ac.uns.ftn.informatika.jpa.custommq;

public class Message {
    private final String id;
    private final String name;
    private final String location;

    public Message(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Message{id='" + id + "', name='" + name + "', location='" + location + "'}";
    }
}

