import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.*;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class EventSystemAnhembiTest {

    @Test
    public void testRegisterUser() {
        EventSystemAnhembi eventSystem = new EventSystemAnhembi();
        eventSystem.registerUser("Joao couves", "SP", "j.couves@gmail.com");

        List<User> users = eventSystem.getUsers();
        assertEquals(1, users.size());
        assertEquals("Joao couves", users.get(0).name);
        assertEquals("SP", users.get(0).city);
        assertEquals("j.couves@gmail.com", users.get(0).email);
    }

    @Test
    public void testCreateEvent() {
        EventSystemAnhembi eventSystem = new EventSystemAnhembi();
        LocalDateTime dateTime = LocalDateTime.now();
        eventSystem.createEvent("Evento Teste", "Local Teste", "Categoria Teste", dateTime, "Descrição Teste");

        List<Event> events = eventSystem.getEvents();
        assertEquals(1, events.size());
        Event createdEvent = events.get(0);
        assertEquals("Evento Teste", createdEvent.name);
        assertEquals("Local Teste", createdEvent.address);
        assertEquals("Categoria Teste", createdEvent.category);
        assertEquals(dateTime, createdEvent.dateTime);
        assertEquals("Descrição Teste", createdEvent.description);
    }

    @Test
    public void testListEvents() {
        EventSystemAnhembi eventSystem = new EventSystemAnhembi();
        LocalDateTime dateTime1 = LocalDateTime.now();
        LocalDateTime dateTime2 = LocalDateTime.now().plusDays(1);

        eventSystem.createEvent("Evento 1", "Local 1", "Categoria 1", dateTime1, "Descrição 1");
        eventSystem.createEvent("Evento 2", "Local 2", "Categoria 2", dateTime2, "Descrição 2");


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        eventSystem.listEvents();
        String expectedOutput = "Name: Evento 1\n" +
                                "Category: Categoria 1\n" +
                                "Address: Local 1\n" +
                                "Date and Time: " + dateTime1.format(EventSystemAnhembi.dateTimeFormatter) + "\n" +
                                "Description: Descrição 1\n" +
                                "----------------------------\n" +
                                "Name: Evento 2\n" +
                                "Category: Categoria 2\n" +
                                "Address: Local 2\n" +
                                "Date and Time: " + dateTime2.format(EventSystemAnhembi.dateTimeFormatter) + "\n" +
                                "Description: Descrição 2\n" +
                                "----------------------------\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testJoinEvent() {
        EventSystemAnhembi eventSystem = new EventSystemAnhembi();
        User user = new User("Usuário Teste", "Cidade Teste", "email@teste.com");
        LocalDateTime dateTime = LocalDateTime.now();
        Event event = new Event("Evento Teste", "Local Teste", "Categoria Teste", dateTime, "Descrição Teste");

        eventSystem.createEvent("Evento Teste", "Local Teste", "Categoria Teste", dateTime, "Descrição Teste");
        eventSystem.joinEvent(user, event);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        eventSystem.joinEvent(user, event);
        assertEquals("Usuário Teste participou do evento: Evento Teste\n", outContent.toString());
    }

    @Test
    public void testCancelEvent() {
        EventSystemAnhembi eventSystem = new EventSystemAnhembi();
        User user = new User("Usuário Teste", "Cidade Teste", "email@teste.com");
        LocalDateTime dateTime = LocalDateTime.now();
        Event event = new Event("Evento Teste", "Local Teste", "Categoria Teste", dateTime, "Descrição Teste");

        eventSystem.createEvent("Evento Teste", "Local Teste", "Categoria Teste", dateTime, "Descrição Teste");
        eventSystem.cancelEvent(user, event);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        eventSystem.cancelEvent(user, event);
        assertEquals("Usuário Teste cancelou a participação no evento: Evento Teste\n", outContent.toString());
    }
}
