import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EventSystemAnhembiTest {

    private EventSystemAnhembi eventSystem;

    @Before
    public void setUp() {
        eventSystem = new EventSystemAnhembi();
        clearDatabase();
    }

    @After
    public void tearDown() {
        clearDatabase();
    }

    @Test
    public void testRegisterUser() {
        eventSystem.registerUser("testUser", "Test Name", "Test City", "test@email.com", "password", false);

        List<User> users = eventSystem.getUsers();
        assertNotNull(users);
        assertEquals(1, users.size());

        User registeredUser = users.get(0);
        assertEquals("testUser", registeredUser.getUsername());
        assertEquals("Test Name", registeredUser.getName());
        assertEquals("Test City", registeredUser.getCity());
        assertEquals("test@email.com", registeredUser.getEmail());
        assertEquals("password", registeredUser.getPassword());
        assertEquals(false, registeredUser.isExhibitor());
    }

    @Test
    public void testCreateEvent() {
        User exhibitor = new User("exhibitor", "Exhibitor Name", "Exhibitor City", "exhibitor@email.com", "password", true);

        eventSystem.createEvent("Test Event", "Event Address", "Test Category", LocalDateTime.now().plusDays(1), "Event Description", exhibitor);

        List<Event> events = eventSystem.getEvents();
        assertNotNull(events);
        assertEquals(1, events.size());

        Event createdEvent = events.get(0);
        assertEquals("Test Event", createdEvent.getName());
        assertEquals("Event Address", createdEvent.getAddress());
        assertEquals("Test Category", createdEvent.getCategory());
        assertEquals(exhibitor, createdEvent.getExhibitor());
        assertEquals(false, createdEvent.isCancelled());
    }

    private void clearDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:eventsystem2.db")) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users")) {
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM events")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
