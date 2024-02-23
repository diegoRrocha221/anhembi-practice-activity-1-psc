import java.time.LocalDateTime;
import java.io.Serializable;

public class Event implements Serializable {
    String name;
    String address;
    String category;
    LocalDateTime dateTime;
    String description;

    public Event(String name, String address, String category, LocalDateTime dateTime, String description) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.dateTime = dateTime;
        this.description = description;
    }
}