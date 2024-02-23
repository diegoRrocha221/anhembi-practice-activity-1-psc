import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.*;
import java.time.LocalDateTime;

import org.junit.Test;

public class EventSystemAnhembiTest {

    @Test
    public void testRegisterUser() {
        EventSystemAnhembi eventSystem = new EventSystemAnhembi();
        eventSystem.registerUser("Joao couves", "SP", "j.couves@gmail.com");

        assertEquals(1, eventSystem.getUsers().size());
        assertEquals("Joao couves", eventSystem.getUsers().get(0).name);
        assertEquals("SP", eventSystem.getUsers().get(0).city);
        assertEquals("j.couves@gmail.com", eventSystem.getUsers().get(0).email);
    }
    
    @Test
    void testListEvents() {
        EventSystemAnhembi eventSystemAnhembi = new EventSystemAnhembi();
        Event event1 = new Event("Party1", "Venue1", "Social", LocalDateTime.now(), "Celebration1");
        Event event2 = new Event("Party2", "Venue2", "Social", LocalDateTime.now().plusDays(1), "Celebration2");

        eventSystemAnhembi.createEvent("Party1", "Venue1", "Social", LocalDateTime.now(), "Celebration1");
        eventSystemAnhembi.createEvent("Party2", "Venue2", "Social", LocalDateTime.now().plusDays(1), "Celebration2");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        eventSystemAnhembi.listEvents();

        String expectedOutput = "Name: Party1\nCategory: Social\nAddress: Venue1\nDate and Time: ";
        assertTrue(outContent.toString().contains(expectedOutput));
        expectedOutput = "Name: Party2\nCategory: Social\nAddress: Venue2\nDate and Time: ";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
}