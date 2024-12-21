package rs.ac.uns.ftn.informatika.jpa.custommq;

public class Message {

    private final String name;
    private final String location;

    

    public Message( String name, String location) {

        this.name = name;
        this.location = location;
    }


    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Message{name='" + name + "', location='" + location + "'}";
    }
}

