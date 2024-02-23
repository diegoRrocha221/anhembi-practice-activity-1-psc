// User.java
import java.util.ArrayList;
import java.util.List;

public class User {
    private static int nextId = 1;

    private int id;
    private String username;
    private String name;
    private String city;
    private String email;
    private String password;
    private boolean isExhibitor;
    private List<Event> confirmedEvents;

    public User(String username, String name, String city, String email, String password, boolean isExhibitor) {
        this.id = nextId++;
        this.username = username;
        this.name = name;
        this.city = city;
        this.email = email;
        this.password = password;
        this.isExhibitor = isExhibitor;
        this.confirmedEvents = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isExhibitor() {
        return isExhibitor;
    }

    public List<Event> getConfirmedEvents() {
        return confirmedEvents;
    }

    public void confirmAttendance(Event event) {
        confirmedEvents.add(event);
    }

    public void cancelAttendance(Event event) {
        confirmedEvents.remove(event);
    }
}
