import java.io.*;
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
        loadEventsFromFile();
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

    public void cancelEvent(User user, Event event) {
        if (events.contains(event)) {
            System.out.println(user.name + " cancelou a participação no evento: " + event.name);
        } else {
            System.out.println("Evento não encontrado.");
        }
    }

    private void loadEventsFromFile() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("events.data");
             ObjectInputStream ois = new ObjectInputStream(is)) {
            events = (List<Event>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions
        }
    }

    private void saveEventsToFile() {
        try (OutputStream os = new FileOutputStream("src/test/resources/events.data");
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(events);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<User> getUsers() {
        return users;
    }
    
    public List<Event> getEvents() {
        return events;
    }
    
    public static void main(String[] args) {
        EventSystemAnhembi eventSystem = new EventSystemAnhembi();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Cadastrar Usuário");
            System.out.println("2. Criar Evento");
            System.out.println("3. Listar Eventos");
            System.out.println("4. Participar de um Evento");
            System.out.println("5. Cancelar Participação em um Evento");
            System.out.println("0. Sair");

            System.out.print("Escolha uma opção: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha após o próximo inteiro

            switch (choice) {
                case 1:
                    System.out.print("Nome do usuário: ");
                    String userName = scanner.nextLine();
                    System.out.print("Cidade: ");
                    String city = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    eventSystem.registerUser(userName, city, email);
                    break;

                case 2:
                    System.out.print("Nome do evento: ");
                    String eventName = scanner.nextLine();
                    System.out.print("Endereço: ");
                    String address = scanner.nextLine();
                    System.out.print("Categoria: ");
                    String category = scanner.nextLine();
                    System.out.print("Data e Hora (YYYY-MM-DD HH:mm): ");
                    String dateTimeStr = scanner.nextLine();
                    LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
                    System.out.print("Descrição: ");
                    String description = scanner.nextLine();
                    eventSystem.createEvent(eventName, address, category, dateTime, description);
                    break;

                case 3:
                    eventSystem.listEvents();
                    break;

                case 4:
                    System.out.print("Digite o nome do evento que deseja participar: ");
                    String eventToJoin = scanner.nextLine();
                    Event foundEventToJoin = eventSystem.findEventByName(eventToJoin);
                    if (foundEventToJoin != null) {
                        eventSystem.joinEvent(new User("Usuário Temporário", "Cidade Temporária", "email@temp.com"), foundEventToJoin);
                    } else {
                        System.out.println("Evento não encontrado.");
                    }
                    break;

                case 5:
                    System.out.print("Digite o nome do evento do qual deseja cancelar a participação: ");
                    String eventToCancel = scanner.nextLine();
                    Event foundEventToCancel = eventSystem.findEventByName(eventToCancel);
                    if (foundEventToCancel != null) {
                        eventSystem.cancelEvent(new User("Usuário Temporário", "Cidade Temporária", "email@temp.com"), foundEventToCancel);
                    } else {
                        System.out.println("Evento não encontrado.");
                    }
                    break;

                case 0:
                    System.out.println("Saindo do sistema...");
                    System.exit(0);

                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
    
    private Event findEventByName(String eventName) {
        for (Event event : events) {
            if (event.name.equals(eventName)) {
                return event;
            }
        }
        return null;
    }
}
