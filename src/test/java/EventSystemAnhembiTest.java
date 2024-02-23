import static org.junit.Assert.assertEquals;

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

}