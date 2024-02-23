import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventSystemAnhembi {
	private List<User> users;
	private List<Event> events;
	
    public EventSystemAnhembi() {
        this.users = new ArrayList<>();
        this.events = new ArrayList<>();
    }
	
    public void registerUser(String name, String city, String email) {
        User user = new User(name, city, email);
        users.add(user);
    }
    
    public void joinEvent(User user, Event event) {
        if (events.contains(event)) {
            System.out.println(user.name + " participou do evento: " + event.name);
        } else {
            System.out.println("Evento não encontrado.");
        }
    }
    
    public List<User> getUsers() {
        return users;
    }
}
