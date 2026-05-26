import File.Save;
import People.*;
import People.Employees.*;
import Room.*;
import Reservation.Reservation;
import java.util.ArrayList;
import java.util.List;

public class DataGenerator {
    public static void main(String[] args) {
        System.out.println("--- Generating Data (English Logins) ---");

        // Wspólne hasło dla wszystkich
        String commonPassword = "zaq1@WSX";

        List<Person> users = new ArrayList<>();
        try {
            System.out.println("Creating employees...");
            
            // Manager
            users.add(new Manager("John", "Smith", "manager", commonPassword, 100.0));
            
            // Maid
            users.add(new Maid("Anna", "Cleaner", "maid", commonPassword, 25.0));
            
            // Masseur
            users.add(new Masseur("Peter", "Relax", "masseur", commonPassword, 40.0));
            
            // Receptionist
            users.add(new Recepcionist("Kate", "Office", "receptionist", commonPassword, 30.0));
            
            // Security
            users.add(new SecurityGuard("Mark", "Safe", "security", commonPassword, 35.0));
            
            // Guest
            System.out.println("Creating test guest...");
            Guest guest = new Guest("Test", "Guest", "guest", commonPassword);
            
            HotelCard card = new HotelCard();
            card.topUp(5000.0);
            guest.receiveCard(card);
            users.add(guest);

            System.out.println("Users added successfully.");

        } catch (Exception e) {
            System.out.println("ERROR CREATING USERS: " + e.getMessage());
            e.printStackTrace();
            return; 
        }
        
        Save.save(users, "hotel_users.ser");

        System.out.println("Generating rooms...");
        List<Room> rooms = new ArrayList<>();
        
        rooms.add(new Room(101, 1, RoomType.STANDARD));
        rooms.add(new Room(102, 1, RoomType.STANDARD));
        rooms.add(new Room(103, 1, RoomType.FAMILY));
        rooms.add(new Room(104, 1, RoomType.FAMILY));
        rooms.add(new Room(105, 1, RoomType.STANDARD));
        rooms.add(new Room(106, 1, RoomType.STANDARD));
        
        rooms.add(new Room(201, 2, RoomType.BUSINESS));
        rooms.add(new Room(202, 2, RoomType.BUSINESS));
        rooms.add(new Room(203, 2, RoomType.BUSINESS));
        
        rooms.add(new Room(301, 3, RoomType.SUITE));
        rooms.add(new Room(302, 3, RoomType.SUITE));
        rooms.add(new Room(303, 3, RoomType.VIP));

        Save.save(rooms, "pokoje.ser");

        System.out.println("Resetting queues and reservations...");
        Save.save(new ArrayList<String>(), "massage_queue.ser");
        Save.save(new ArrayList<Reservation>(), "reservations.ser");

        System.out.println("-------------------------------------------------------");
        System.out.println("SUCCESS! Database files generated.");
        System.out.println("PASSWORD FOR ALL: " + commonPassword);
        System.out.println("");
        System.out.println("LOGINS:");
        System.out.println("- Manager:       manager");
        System.out.println("- Receptionist:  receptionist");
        System.out.println("- Security:      security");
        System.out.println("- Maid:          maid");
        System.out.println("- Masseur:       masseur");
        System.out.println("- Guest:         guest");
        System.out.println("-------------------------------------------------------");
    }
}
