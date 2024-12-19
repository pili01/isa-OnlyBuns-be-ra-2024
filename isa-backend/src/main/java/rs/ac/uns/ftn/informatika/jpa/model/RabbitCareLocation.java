package rs.ac.uns.ftn.informatika.jpa.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rabbit_care_locations")
public class RabbitCareLocation {

    @Id
    private String id;

    private String name;

    private String location;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "RabbitCareLocation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
