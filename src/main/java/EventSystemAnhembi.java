import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class EventSystemAnhembi {
    private List<User> users;
    private List<Event> events;
    private Connection connection;
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public EventSystemAnhembi() {
        this.users = new ArrayList<>();
        this.events = new ArrayList<>();
        initializeDatabase();
        loadEventsFromDatabase();
    }

    private void initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:eventsystem2.db");
            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTables() {
        try (Statement statement = connection.createStatement()) {
            String createUserTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL," +
                    "name TEXT NOT NULL," +
                    "city TEXT NOT NULL," +
                    "email TEXT NOT NULL," +
                    "password TEXT NOT NULL," +
                    "is_exhibitor BOOLEAN NOT NULL" +
                    ");";
            statement.executeUpdate(createUserTable);

            String createEventTable = "CREATE TABLE IF NOT EXISTS events (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "address TEXT NOT NULL," +
                    "category TEXT NOT NULL," +
                    "date_time TEXT NOT NULL," +
                    "description TEXT NOT NULL," +
                    "exhibitor_id INTEGER NOT NULL," +
                    "cancelled BOOLEAN NOT NULL" +
                    ");";

            statement.executeUpdate(createEventTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registerUser(String username, String name, String city, String email, String password, boolean isExhibitor) {
        User user = new User(username, name, city, email, password, isExhibitor);
        users.add(user);
        saveUserToDatabase(user);
    }

    private void saveUserToDatabase(User user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO users (username, name, city, email, password, is_exhibitor) VALUES (?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getCity());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setBoolean(6, user.isExhibitor());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createEvent(String name, String address, String category, LocalDateTime dateTime, String description, User exhibitor) {
        Event event = new Event(name, address, category, dateTime, description, exhibitor);
        events.add(event);
        saveEventToDatabase(event);
    }

    private void saveEventToDatabase(Event event) {
        if (events.stream().anyMatch(existingEvent ->
        existingEvent.getName().equalsIgnoreCase(event.getName()) &&
        existingEvent.getDateTime().isEqual(event.getDateTime()))) {
        System.out.println("Um evento com o mesmo nome e data/hora já está cadastrado. Tente novamente.");
        return;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(
        "INSERT INTO events (name, address, category, date_time, description, exhibitor_id, cancelled) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
        preparedStatement.setString(1, event.getName());
        preparedStatement.setString(2, event.getAddress());
        preparedStatement.setString(3, event.getCategory());
        preparedStatement.setString(4, event.getDateTime().format(dateTimeFormatter));
        preparedStatement.setString(5, event.getDescription());
        preparedStatement.setInt(6, event.getExhibitor().getId());
        preparedStatement.setBoolean(7, event.isCancelled());
        preparedStatement.executeUpdate();
        } catch (SQLException e) {
        	e.printStackTrace();
        }
    }

    public void listEvents() {
        Collections.sort(events, (e1, e2) -> e1.getDateTime().compareTo(e2.getDateTime()));

        for (Event event : events) {
            System.out.println("Name: " + event.getName());
            System.out.println("Category: " + event.getCategory());
            System.out.println("Address: " + event.getAddress());
            System.out.println("Date and Time: " + event.getDateTime().format(dateTimeFormatter));
            System.out.println("Description: " + event.getDescription());
            System.out.println("----------------------------");
        }
    }

    public void joinEvent(User user, Event event) {
        if (events.contains(event)) {
            System.out.println(user.getName() + " participou do evento: " + event.getName());
            user.confirmAttendance(event);
            updateEventInDatabase(event);
        } else {
            System.out.println("Evento não encontrado.");
        }
    }

    private void updateEventInDatabase(Event event) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE events SET cancelled = ? WHERE id = ?")) {
            preparedStatement.setBoolean(1, event.isCancelled());
            preparedStatement.setInt(2, event.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelEvent(User user, Event event) {
        if (events.contains(event)) {
            System.out.println(user.getName() + " cancelou a participação no evento: " + event.getName());
            user.cancelAttendance(event);
            updateEventInDatabase(event);
        } else {
            System.out.println("Evento não encontrado.");
        }
    }

    private void loadEventsFromDatabase() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM events")) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String eventName = resultSet.getString("name");
                String eventAddress = resultSet.getString("address");
                String eventCategory = resultSet.getString("category");
                LocalDateTime eventDateTime = LocalDateTime.parse(resultSet.getString("date_time"), dateTimeFormatter);
                String eventDescription = resultSet.getString("description");
                int exhibitorId = resultSet.getInt("exhibitor_id");
                boolean cancelled = resultSet.getBoolean("cancelled");

                User exhibitor = findUserById(exhibitorId);

                Event event = new Event(eventName, eventAddress, eventCategory, eventDateTime, eventDescription, exhibitor);
                event.setId(id);  

                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private User findUserById(int userId) {
        for (User user : users) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
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
            System.out.println("1. Login");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int mainChoice = scanner.nextInt();
            scanner.nextLine(); 

            switch (mainChoice) {
                case 1:
                    eventSystem.login();
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    System.exit(0);
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Usuário");
        System.out.println("2. Expositor");
        System.out.print("Selecione: ");
        int userTypeChoice = scanner.nextInt();
        scanner.nextLine(); 

        switch (userTypeChoice) {
            case 1:
                System.out.println("1. Já tenho cadastro");
                System.out.println("2. Não tenho cadastro");
                System.out.print("Selecione: ");
                int userExistenceChoice = scanner.nextInt();
                scanner.nextLine();

                if (userExistenceChoice == 1) {
                    loginUser();
                } else if (userExistenceChoice == 2) {
                    registerUser();
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                }
                break;
            case 2:
                System.out.println("1. Listar eventos");
                System.out.println("2. Cadastrar evento");
                System.out.println("3. Cancelar evento para todos");
                System.out.println("4. Voltar");
                System.out.print("Selecione: ");
                int exhibitorChoice = scanner.nextInt();
                scanner.nextLine();

                switch (exhibitorChoice) {
                    case 1:
                        listEvents();
                        break;
                    case 2:
                        createEvent();
                        break;
                    case 3:
                        cancelEventForAll();
                        break;
                    case 4:
                        // Voltar
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
        }
    }

    private void loginUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String password = scanner.nextLine();

        if (email == null || email.trim().isEmpty() || !isValidEmail(email)) {
            System.out.println("Email é obrigatório e deve ser válido. Tente novamente.");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            System.out.println("Senha é obrigatória. Tente novamente.");
            return;
        }

        User loggedInUser = validateUser(email, password);
        if (loggedInUser != null) {
            System.out.println("Login bem-sucedido! Bem-vindo, " + loggedInUser.getName() + ".");
            userMenu(loggedInUser);
        } else {
            System.out.println("Email ou senha incorretos. Tente novamente.");
        }
    }

    private User validateUser(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private void userMenu(User user) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Listar eventos disponíveis");
            System.out.println("2. Confirmar presença em evento");
            System.out.println("3. Listar eventos que confirmei presença");
            System.out.println("4. Cancelar presença em um evento");
            System.out.println("5. Comparecer a um evento");
            System.out.println("6. Sair da minha conta");
            System.out.print("Escolha uma opção: ");
            int userChoice = scanner.nextInt();
            scanner.nextLine(); 

            switch (userChoice) {
                case 1:
                    listAvailableEvents();
                    break;
                case 2:
                    confirmAttendance(user);
                    break;
                case 3:
                    listConfirmedEvents(user);
                    break;
                case 4:
                    cancelAttendance(user);
                    break;
                case 5:
                    markAttendance(user);
                    break;
                case 6:
                    System.out.println("Saindo da conta...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void listAvailableEvents() {
        System.out.println("Eventos Disponíveis:");
        for (Event event : events) {
            if (!event.isCancelled() && !event.getAttendees().contains(event.getExhibitor())) {
                System.out.println("ID: " + event.getId());
                System.out.println("Nome: " + event.getName());
                System.out.println("Categoria: " + event.getCategory());
                System.out.println("Endereço: " + event.getAddress());
                System.out.println("Data e Hora: " + event.getDateTime().format(dateTimeFormatter));
                System.out.println("Descrição: " + event.getDescription());
                System.out.println("Expositor: " + event.getExhibitor().getName());
                System.out.println("----------------------------");
            }
        }
    }

    private void confirmAttendance(User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do evento que deseja confirmar presença: ");
        int eventId = scanner.nextInt();
        scanner.nextLine(); 

        Event selectedEvent = findEventById(eventId);
        if (selectedEvent != null) {
            joinEvent(user, selectedEvent);
        } else {
            System.out.println("Evento não encontrado.");
        }
    }

    private void listConfirmedEvents(User user) {
        System.out.println("Eventos Confirmados para " + user.getName() + ":");
        for (Event event : user.getConfirmedEvents()) {
            System.out.println("Nome: " + event.getName());
            System.out.println("Categoria: " + event.getCategory());
            System.out.println("Endereço: " + event.getAddress());
            System.out.println("Data e Hora: " + event.getDateTime().format(dateTimeFormatter));
            System.out.println("Descrição: " + event.getDescription());
            System.out.println("Expositor: " + event.getExhibitor().getName());
            System.out.println("----------------------------");
        }
    }

    private void cancelAttendance(User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do evento que deseja cancelar presença: ");
        int eventId = scanner.nextInt();
        scanner.nextLine(); 

        Event selectedEvent = findEventById(eventId);
        if (selectedEvent != null) {
            cancelEvent(user, selectedEvent);
        } else {
            System.out.println("Evento não encontrado.");
        }
    }

    private void markAttendance(User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do evento que deseja comparecer: ");
        int eventId = scanner.nextInt();
        scanner.nextLine(); 

        Event selectedEvent = findEventById(eventId);
        if (selectedEvent != null) {
            System.out.println("Parabéns, " + user.getName() + "! Você compareceu ao evento: " + selectedEvent.getName());
            user.confirmAttendance(selectedEvent);
            updateEventInDatabase(selectedEvent);
        } else {
            System.out.println("Evento não encontrado.");
        }
    }

    private Event findEventById(int eventId) {
        for (Event event : events) {
            if (event.getId() == eventId) {
                return event;
            }
        }
        return null;
    }

    private void registerUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nome de usuário: ");
        String username = scanner.nextLine();
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Nome de usuário é obrigatório. Tente novamente.");
            return;
        }

        System.out.print("Nome: ");
        String name = scanner.nextLine();
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Nome é obrigatório. Tente novamente.");
            return;
        }

        System.out.print("Cidade: ");
        String city = scanner.nextLine();
        if (city == null || city.trim().isEmpty()) {
            System.out.println("Cidade é obrigatória. Tente novamente.");
            return;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine();
        if (email == null || email.trim().isEmpty() || !isValidEmail(email)) {
            System.out.println("Email é obrigatório e deve ser válido. Tente novamente.");
            return;
        }

        System.out.print("Senha: ");
        String password = scanner.nextLine();
        if (password == null || password.trim().isEmpty()) {
            System.out.println("Senha é obrigatória. Tente novamente.");
            return;
        }

        System.out.print("É expositor? (true/false): ");
        boolean isExhibitor;
        try {
            isExhibitor = scanner.nextBoolean();
        } catch (InputMismatchException e) {
            System.out.println("Opção inválida para expositor. Use 'true' ou 'false'. Tente novamente.");
            return;
        }

        registerUser(username, name, city, email, password, isExhibitor);
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }
    	
    private String getCategoryByChoice(int choice) {
        switch (choice) {
            case 1:
                return "Festas";
            case 2:
                return "Eventos";
            case 3:
                return "Esportivos";
            case 4:
                return "Shows";
            case 5:
                return "Festival";
            case 6:
                return "Concerto";
            case 7:
                return "Casamento";
            default:
                return null;
        }
    }
    private User getLoggedInExhibitor() {
        User exhibitor = findExhibitor();
        if (exhibitor == null) {
            System.out.println("Nenhum expositor cadastrado. Cadastre um expositor antes de criar eventos.");
            return null;
        }
        return exhibitor;
    }

    private User findExhibitor() {
        for (User user : users) {
            if (user.isExhibitor()) {
                return user;
            }
        }
        return null;
    }
    private void createEvent() {
        User exhibitor = getLoggedInExhibitor();
        if (exhibitor == null) {
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nome do evento: ");
        String eventName = scanner.nextLine();
        System.out.print("Endereço: ");
        String address = scanner.nextLine();
        System.out.print("Categoria (selecione o número): \n" +
                "1. Festas\n" +
                "2. Eventos\n" +
                "3. Esportivos\n" +
                "4. Shows\n" +
                "5. Festival\n" +
                "6. Concerto\n" +
                "7. Casamento\n" +
                "Selecione: ");
        int categoryChoice;
        try {
            categoryChoice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Opção inválida para categoria. Use um número de 1 a 7. Tente novamente.");
            scanner.nextLine();
            return;
        }
        String category = getCategoryByChoice(categoryChoice);
        if (category == null) {
            System.out.println("Opção inválida para categoria. Use um número de 1 a 7. Tente novamente.");
            return;
        }
        scanner.nextLine(); // Consume the newline character

        System.out.print("Data e Hora (DD/MM/YYYY HH:mm): ");
        String dateTimeStr = scanner.nextLine();
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, dateFormatter);
            if (dateTime.isBefore(LocalDateTime.now())) {
                System.out.println("A data do evento não pode ser no passado. Tente novamente.");
                return;
            }

            System.out.print("Descrição: ");
            String description = scanner.nextLine();

            createEvent(eventName, address, category, dateTime, description, exhibitor);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data e hora inválido. Use o formato DD/MM/YYYY HH:mm. Tente novamente.");
            return;
        }
    }



    private void cancelEventForAll() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do evento que deseja cancelar para todos: ");
        int eventId = scanner.nextInt();
        scanner.nextLine();

        Event selectedEvent = findEventById(eventId);
        if (selectedEvent != null) {
            cancelEventForAll(selectedEvent);
        } else {
            System.out.println("Evento não encontrado.");
        }
    }

    private void cancelEventForAll(Event event) {
        event.cancelEventForAll();
        updateEventInDatabase(event);
        System.out.println("Evento cancelado para todos os participantes.");
    }
}
