import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventSystemAnhembi {
	private List<User> users;
	
    public EventSystemAnhembi() {
        this.users = new ArrayList<>();
    }
	
    public void registerUser(String name, String city, String email) {
        User user = new User(name, city, email);
        users.add(user);
    }
    
    
    public List<User> getUsers() {
        return users;
    }
}
