// Event.java
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private static int nextId = 1;

    private int id;
    private String name;
    private String address;
    private String category;
    private LocalDateTime dateTime;
    private String description;
    private User exhibitor;
    private boolean cancelled;
    private List<User> attendees;

    public Event(String name, String address, String category, LocalDateTime dateTime, String description, User exhibitor) {
        this.id = nextId++;
        this.name = name;
        this.address = address;
        this.category = category;
        this.dateTime = dateTime;
        this.description = description;
        this.exhibitor = exhibitor;
        this.cancelled = false;
        this.attendees = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public User getExhibitor() {
        return exhibitor;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public List<User> getAttendees() {
        return attendees;
    }

    public void cancelEventForAll() {
        cancelled = true;
        attendees.clear();
    }

    public void addAttendee(User user) {
        attendees.add(user);
    }

    public void removeAttendee(User user) {
        attendees.remove(user);
    }
    public void setId(int id) {
        this.id = id;
    }
}
