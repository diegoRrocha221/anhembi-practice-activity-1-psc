import java.io.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventSystemAnhembi {
	private List<User> users;
	private List<Event> events;
	public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
	public EventSystemAnhembi() {
        this.users = new ArrayList<>();
        this.events = new ArrayList<>();
    }
	
    public void registerUser(String name, String city, String email) {
        User user = new User(name, city, email);
        users.add(user);
    }
    
    public void createEvent(String name, String address, String category, LocalDateTime dateTime, String description) {
        Event event = new Event(name, address, category, dateTime, description);
        events.add(event);
        saveEventsToFile();
    }
    
    public void listEvents() {
        Collections.sort(events, (e1, e2) -> e1.dateTime.compareTo(e2.dateTime));

        for (Event event : events) {
            System.out.println("Name: " + event.name);
            System.out.println("Category: " + event.category);
            System.out.println("Address: " + event.address);
            System.out.println("Date and Time: " + event.dateTime.format(dateTimeFormatter));
            System.out.println("Description: " + event.description);
            System.out.println("----------------------------");
        }
    }
    
    public void joinEvent(User user, Event event) {
        if (events.contains(event)) {
            System.out.println(user.name + " participou do evento: " + event.name);
        } else {
            System.out.println("Evento não encontrado.");
        }
    }
    
    private void saveEventsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/main/resources/events.data"))) {
            oos.writeObject(events);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<User> getUsers() {
        return users;
    }
}
