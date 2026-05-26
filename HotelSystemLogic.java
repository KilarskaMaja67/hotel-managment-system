
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import File.Read;
import File.Save;
import Observer.DataObserver;
import Observer.HotelLogger;
import Password.WeakPasswordException;
import People.*;
import People.Employees.*;
import Reservation.Reservation;
import Room.*;
import Room.Sorting.SortByPrice;
import Room.Strategy.*;

public class HotelSystemLogic
{
    private List<Person> users;
    private List<Reservation> reservations;
    private List<Integer> IDr;
    private List<Room> roomData;
    private List<DataObserver>observers=new ArrayList<>();

    private List<String> massageQueue;
    private static final String QUEUE_FILE = "massage_queue.ser";

    public void addObserver(DataObserver o)
    {
        observers.add(o);
    }

    public void notifyObservers(){
        for(DataObserver o: observers)
        {
            o.DataChanged();
        }
    }

    private Person currentUser;

    private static final String USERS_FILE = "hotel_users.ser";
    private static final String ROOMS_FILE = "pokoje.ser";
    private static final String RESERVATIONS_FILE = "reservations.ser";

    private static Random generator = new Random();

    public HotelSystemLogic()
    {
        this.users = Read.read(USERS_FILE);
        this.roomData = Read.read(ROOMS_FILE);
        this.reservations = Read.read(RESERVATIONS_FILE);
        this.massageQueue = Read.read(QUEUE_FILE);

        if (this.users == null) this.users = new ArrayList<>();
        if (this.roomData == null) this.roomData = new ArrayList<>();
        if (this.reservations == null) this.reservations = new ArrayList<>();
        if (this.massageQueue == null) this.massageQueue = new ArrayList<>();

        this.IDr = new ArrayList<>();
        for (Reservation r : reservations) {
            IDr.add(r.getReservationID());
        }

        initializeDefaultUsers();
        initializeDefaultRooms();
    }

    private void saveUsers() { Save.save(users, USERS_FILE); }
    private void saveRooms() { Save.save(roomData, ROOMS_FILE); }
    private void saveReservations() { Save.save(reservations, RESERVATIONS_FILE); }
    private void saveQueue() { Save.save(massageQueue, QUEUE_FILE); }

    public Person handleLogin(String login, String password) throws Exception{
        Person user=findUserByLogin(login);
        if(user!=null && user.checkPassword(password))
        {
            if (user instanceof Guest) {
                Guest g = (Guest) user;
                if (g.isBlacklisted()) {
                    throw new Exception("ACCOUNT BANNED. Contact security.");
                }
            }
            HotelLogger.log("AUTHENTICATION", "User " + login + " logged in successfully.");
            this.currentUser=user;
            return user;
        }
        else
        {
            HotelLogger.log("AUTHENTICATION", "Failed login attempt for user: " + login );
            throw new Exception("Incorrect login or password");
        }
    }

    public void handleLogout()
    {

        HotelLogger.log("AUTHENTICATION", "User " + this.currentUser.getLogin() + " logged out.");
        this.currentUser=null;
    }

    public void handleRegistration(String name, String surname, String login, String password1, String password2) throws Exception {
        if (findUserByLogin(login) != null) {
            throw new Exception("Login is already taken.");
        }
        if (!password1.equals(password2)) {
            throw new Exception("New passwords must match.");
        }

        Guest newGuest = new Guest(name, surname, login, password1);
        users.add(newGuest);
        saveUsers();

        notifyObservers();
        HotelLogger.log("AUTHENTICATION", "New account created: " + login + " (" + name + " " + surname + ").");
    }

    public void forcePasswordChange(Employee emp, String newPass1, String newPass2, String oldPass) throws Exception{
        if (!emp.checkPassword(oldPass)) {
            throw new Exception("Old password is incorrect.");
        }
        if (!newPass1.equals(newPass2)) {
            throw new Exception("New passwords must match.");
        }
        if (emp.checkPassword(newPass1)) {
            throw new Exception("New password cannot be the same as old password.");
        }
        emp.setPassword(newPass1);
        emp.setIsFirstLogin(false);
        saveUsers();
        HotelLogger.log("AUTHETICATION", emp.getLogin() +" "+emp.getName()+" changed their password");
    }

    public boolean isFirstLogin(Person user) {
        if (user instanceof Employee) {
            return ((Employee) user).getIsFirstLogin();
        }
        return false;
    }

    private void initializeDefaultUsers() {
        try {
            if (findUserByLogin("manager") == null) {
                users.add(new Manager("Jan", "Kowalski", "manager", "Manager1!", 100.0));
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        HotelLogger.log("INITIALIZATION", "default users created");

    }

    private void initializeDefaultRooms(){
        if(roomData.isEmpty()){
            roomData.add(new Room(101,1,RoomType.valueOf("STANDARD")));
            roomData.add(new Room(102,1,RoomType.valueOf("STANDARD")));
            roomData.add(new Room(103,1,RoomType.valueOf("FAMILY")));
            roomData.add(new Room(104,1,RoomType.valueOf("FAMILY")));
            roomData.add(new Room(105,1,RoomType.valueOf("STANDARD")));
            roomData.add(new Room(106,1,RoomType.valueOf("STANDARD")));
            roomData.add(new Room(107,1,RoomType.valueOf("STANDARD")));
            roomData.add(new Room(108,1,RoomType.valueOf("FAMILY")));
            roomData.add(new Room(201,2,RoomType.valueOf("BUSINESS")));
            roomData.add(new Room(202,2,RoomType.valueOf("BUSINESS")));
            roomData.add(new Room(203,2,RoomType.valueOf("BUSINESS")));
            roomData.add(new Room(204,2,RoomType.valueOf("STANDARD")));
            roomData.add(new Room(301,3,RoomType.valueOf("SUITE")));
            roomData.add(new Room(302,3,RoomType.valueOf("SUITE")));
            roomData.add(new Room(303,3,RoomType.valueOf("VIP")));
            Save.save(roomData,ROOMS_FILE);
            HotelLogger.log("INITIALIZATION", "default rooms created");

        }
    }

    public void handleHiring(String position, String name, String surname, String login, String password, double rate) throws Exception {
        if (findUserByLogin(login)!=null)
            throw new Exception ("Login is already taken");
        try {
            Manager.hireEmployee(users, position, name, surname, login, password, rate);
            saveUsers();
        } catch (WeakPasswordException e) {
            throw new Exception ("Password error: " + e.getMessage());
        } catch (Exception e) {
            throw new Exception ("Error: " + e.getMessage());
        }

        notifyObservers();
        HotelLogger.log("EMPLOYEE ACTION", "hired new employee: " + position+ " " + name);


    }

    public String displayEmployees( String filterPosition) {
        return Manager.displayEmployeesString(this.users, filterPosition);
    }

    public String generateOccupancyReport() {
        StringBuilder sb = new StringBuilder("=== OCCUPANCY REPORT ===\n\n");
        sb.append(String.format("%-20s | %-10s | %-10s | %-10s\n", "Room Type", "Total", "Free", "Occupied"));
        sb.append("-------------------------------------------------------------\n");
        int totalFreeInHotel = 0;
        int totalOccupiedInHotel = 0;
        for (RoomType type : RoomType.values()) {
            int countTotal = 0; int countFree = 0; int countOccupied = 0;
            for (Room room : roomData) {
                if (room.getType() == type) {
                    countTotal++;
                    if (room.isOccupied()) countOccupied++; else countFree++;
                }
            }
            sb.append(String.format("%-20s | %-10d | %-10d | %-10d\n", type.getDisplayName(), countTotal, countFree, countOccupied));
            totalFreeInHotel += countFree; totalOccupiedInHotel += countOccupied;
        }
        sb.append("-------------------------------------------------------------\n");
        sb.append(String.format("%-20s | %-10d | %-10d | %-10d\n", "TOTAL", (totalFreeInHotel + totalOccupiedInHotel), totalFreeInHotel, totalOccupiedInHotel));
        return sb.toString();
    }

    public String generateCleanlinessReport() {
        StringBuilder sb = new StringBuilder("=== CLEANLINESS REPORT ===\n\n");
        sb.append(String.format("%-20s | %-10s | %-10s | %-10s\n", "Room Type", "Total", "Clean", "Dirty"));
        sb.append("-------------------------------------------------------------\n");
        int totalCleanInHotel = 0; int totalDirtyInHotel = 0;
        List<Room> dirtyRoomList = new ArrayList<>();
        for (RoomType type : RoomType.values()) {
            int countTotal = 0; int countClean = 0; int countDirty = 0;
            for (Room room : roomData) {
                if (room.getType() == type) {
                    countTotal++;
                    if (room.isClean()) countClean++; else { countDirty++; dirtyRoomList.add(room); }
                }
            }
            sb.append(String.format("%-20s | %-10d | %-10d | %-10d\n", type.getDisplayName(), countTotal, countClean, countDirty));
            totalCleanInHotel += countClean; totalDirtyInHotel += countDirty;
        }
        sb.append("-------------------------------------------------------------\n");
        sb.append("TO CLEAN (Total): " + totalDirtyInHotel + "\n");
        if (totalDirtyInHotel > 0) {
            sb.append("\n[!] ROOMS TO CLEAN LIST:\n");
            Collections.sort(dirtyRoomList, (r1, r2) -> Integer.compare(r1.getRoomNumber(), r2.getRoomNumber()));
            int counter = 0;
            for (Room r : dirtyRoomList) {
                sb.append(String.format("[No. %d (%s)]  ", r.getRoomNumber(), r.getType().name().substring(0, 3)));
                counter++;
                if (counter % 3 == 0) sb.append("\n");
            }
            sb.append("\n");
        } else {
            sb.append("\nAll rooms are clean.\n");
        }
        return sb.toString();
    }

    public String displayGuests() {
        return Recepcionist.displayGuestsString(this.users, this.reservations);
    }

    public String handleCheckIn(int reservationId) throws Exception {
        Reservation reservation = findReservationByID(reservationId);
        if (reservation == null) throw new Exception("No reservation found with that ID.");
        Person person = findUserByLogin(reservation.getUsername());
        if (!(person instanceof Guest)) throw new Exception("User assigned to reservation is not a guest.");
        Guest guest = (Guest) person;
        Room room = reservation.getRoom();
        guest.getCard().addRoom(room.getRoomNumber());
        room.checkIn();
        guest.setCheckedIn(true);
        saveUsers(); saveRooms(); notifyObservers();
        HotelLogger.log("RECEPTION", "checked in guest: " + guest.getLogin() +". Reservation Id: "+reservationId);

        return "Checked in: " + guest.getName() + " " + guest.getSurname() + "\nIssued card for room number: " + room.getRoomNumber();
    }

    public String handleCheckInGuestVers(int reservationId) throws Exception {
        Reservation reservation = findReservationByID(reservationId);
        if (reservation == null) throw new Exception("No reservation found with that ID.");
        Person person = findUserByLogin(reservation.getUsername());
        if (!(person instanceof Guest)) throw new Exception("No such reservation.");
        Guest guest = (Guest) person;
        Room room = reservation.getRoom();
        guest.getCard().activate();
        guest.getCard().addRoom(room.getRoomNumber());
        room.setOccupied(true);
        room.checkIn();
        guest.setCheckedIn(true);

        saveUsers(); saveRooms(); notifyObservers();
        HotelLogger.log("GUEST", "self check-in for guest: "+guest.getName() +". Reservation Id: "+reservationId);

        return "Checked in: " + guest.getName() + " " + guest.getSurname() + "\nYou may now open room number: " + room.getRoomNumber();
    }

    public String handleCheckOut(int reservationId) throws Exception {
        Reservation reservation = findReservationByID(reservationId);
        if (reservation == null) throw new Exception("No reservation found with that ID.");
        Person person = findUserByLogin(reservation.getUsername());
        if (!(person instanceof Guest)) throw new Exception("User assigned to reservation is not a guest.");
        Guest guest = (Guest) person;
        Room room = reservation.getRoom();
        guest.setCheckedIn(false);
        room.setOccupied(false);
        room.setClean(false);
        room.checkOut();
        guest.getCard().deactivate();
        guest.getCard().deleteRoom(room.getRoomNumber());
        saveUsers(); saveRooms(); notifyObservers();
        HotelLogger.log("RECEPTION", "checked out guest: "+guest.getName()+ ". Reservation Id: "+reservationId);

        return "Checked out: " + guest.getName() + " " + guest.getSurname() + "\nReturned card for room number: " + room.getRoomNumber();
    }

    public String verifyPerson(String name, String surname) {
        return SecurityGuard.checkPersonString(users, name, surname);
    }

    public String getRoomList() {
        StringBuilder sb = new StringBuilder("--- FULL ROOM LIST ---\n");
        for (RoomType rt : RoomType.values()) {
            sb.append(rt.toString()).append("\n");
        }
        return sb.toString();
    }

    public String getAvailableRoomsList(LocalDate checkIn, LocalDate checkOut) {
        StringBuilder sb = new StringBuilder("--- AVAILABLE ROOMS---\n");
        boolean any = false;
        ArrayList<Room> available_rooms = FindAvailableRooms(checkIn, checkOut, roomData);
        for (Room r : available_rooms) {
            sb.append(r.toString()).append("\n");
            any = true;
        }
        return any ? sb.toString() : "No free rooms available.";
    }

    public String filterRoomOffers(double maxPrice, String requiredEquipment, int sortMode) {
        List<RoomType> filteredList = new ArrayList<>();
        StringBuilder sb = new StringBuilder("--- FILTERED ROOMS ---\n");
        for (RoomType rt : RoomType.values()) {
            if (maxPrice > 0 && rt.getBasePrice() > maxPrice) continue;
            if (requiredEquipment != null && !requiredEquipment.trim().isEmpty()) {
                boolean hasEquipment = false;
                for (Equipment eq : rt.getEquipment()) {
                    if (eq.name().equalsIgnoreCase(requiredEquipment) ||
                            eq.getDescription().toLowerCase().contains(requiredEquipment.toLowerCase())) {
                        hasEquipment = true; break;
                    }
                }
                if (!hasEquipment) continue;
            }
            filteredList.add(rt);
        }
        if (filteredList.isEmpty()) return "No rooms to show.";
        if (sortMode == 1) Collections.sort(filteredList, new SortByPrice());
        else if (sortMode == 2) { Collections.sort(filteredList, new SortByPrice()); Collections.reverse(filteredList); }
        for (RoomType rt : filteredList) { sb.append(rt.toString()).append("\n"); }
        return sb.toString();
    }

    public String makeReservation(Guest guest, String roomType, LocalDate dateIn, LocalDate dateOut) {
        List<Room> roomlist = findRoomByRoomType(roomType);
        if (roomlist == null) return "No such room.";
        List<Room> available_rooms = FindAvailableRooms(dateIn, dateOut, roomlist);
        if (available_rooms.isEmpty()) return "Room isn't available.";
        Room r  = available_rooms.get(0);

        long nights = ChronoUnit.DAYS.between(dateIn, dateOut);
        if (nights < 1) nights = 1;

        PricingStrategy strategy = new StandardPricingStrategy();
        String strategyName;

        int month = dateIn.getMonthValue();
        boolean isHighSeason = (month >= 6 && month <= 8);
        boolean isLongStay = (nights > 7);

        if (isHighSeason && isLongStay) {
            strategy = new HighSeasonLongStayPricingStrategy();
            strategyName = "High Season + Long Stay Discount (-10% on seasonal)";
        } else if (isHighSeason) {
            strategy = new HighSeasonPricingStrategy();
            strategyName = "High Season (+50%)";
        } else if (isLongStay) {
            strategy = new LongStayDiscountStrategy();
            strategyName = "Long Stay Discount (-20%)";
        } else {
            strategy = new StandardPricingStrategy();
            strategyName = "Standard";
        }

        double finalPrice = r.calculatePrice((int) nights, strategy);

        if (!guest.payFromWallet(finalPrice)) {
            return "FAILED. Insufficient wallet funds. Cost: " + finalPrice + " PLN. Your Wallet: " + guest.getWalletBalance();
        }

        int newID = generateID();
        Reservation newRes = new Reservation(newID, guest.getLogin(), r, dateIn, dateOut, finalPrice,strategyName);
        reservations.add(newRes); IDr.add(newID);
        guest.addTransaction("Reservation #" + newID + ": -" + finalPrice + " PLN");
        saveReservations();
        saveUsers();
        notifyObservers();
        HotelLogger.log("GUEST", "reservation made & paid: "+guest.getName()+ ". Cost: "+finalPrice);
        //guest.getCard().chargeRes(finalPrice);

        return "Reservation successful! ID: " + newID + ".\nPrice Factor: " + strategyName + "\nPaid " + finalPrice + " PLN from Wallet.";    }

    public String cancelReservation(Guest guest, int resID) {
        Reservation toCancel = null;
        for (Reservation res : reservations) {
            if (res.getReservationID() == resID) { toCancel = res; break; }
        }
        if (toCancel == null || !toCancel.getUsername().equals(guest.getLogin())) return "No such reservation.";
        reservations.remove(toCancel); IDr.remove(Integer.valueOf(resID));
        toCancel.getRoom().setOccupied(false);
        saveReservations(); notifyObservers();
        guest.addToWallet(toCancel.getTotalPrice());
        HotelLogger.log("GUEST", "reservation canceled: "+guest.getName()+ ". Reservation Id: "+resID);

        return "Succesful annuled reservation #" + resID;
    }

    public String getMyReservations(Guest guest) {
        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for (Reservation r : reservations) {
            if (r.getUsername().equals(guest.getLogin())) { sb.append(r.toString()).append("\n"); found = true; }
        }
        return found ? sb.toString() : "No reservations under your name.";
    }

    public boolean handleOpenDoor(Guest guest, int roomNumber) throws Exception {
        if (guest.getCard() == null) throw new Exception("You don't have a card! You must check in at the reception first.");
        HotelLogger.log("GUEST", "opened door for room: "+roomNumber);

        return guest.getCard().canOpenRoom(roomNumber);
    }

    public void handleOrderService(Guest guest, double price, String serviceName) throws Exception {
        if (!guest.getCheckedIn()) throw new Exception("You aren't currently checked in, so you can't order anything");
        guest.getCard().charge(price);
        saveUsers();
    }

    public String getCardBalance(Guest guest) {
        if (guest.getCard() != null) return guest.getCard().getBalance() + " PLN";
        if(!guest.getCheckedIn()) return "Your card isn't active";
        return "Your card isn't active";
    }

    private int generateID() {
        int id = generator.nextInt(2000000);
        while (IDr.contains(id)) id = generator.nextInt(2000000);
        return id;
    }

    private Room findRoomByNumber(int num) {
        for (Room r : roomData) if (r.getRoomNumber() == num) return r;
        return null;
    }

    private List<Room> findRoomByRoomType(String roomType) {
        ArrayList<Room> roomlist = new ArrayList<>();
        for (Room r : roomData) if (r.getType().getDisplayName().equals(roomType))  roomlist.add(r);
        return roomlist;
    }

    public Person findUserByLogin(String login) {
        if (users == null) return null;
        for (Person p : users) { if (p.getLogin().equals(login)) return p; }
        return null;
    }

    private Reservation findReservationByID(int id) {
        for (Reservation r : reservations) { if (r.getReservationID() == id) return r; }
        return null;
    }

    public Person getCurrentUser() { return currentUser; }

    private ArrayList<Room> FindAvailableRooms(LocalDate in, LocalDate out, List<Room> roomlist){
        ArrayList<Room> available_rooms= new ArrayList<>();
        for(Room r: roomlist){
            boolean used=false;
            for(Reservation reservation: reservations){
                if( reservation.getRoom().getRoomNumber()==r.getRoomNumber()){
                    if(!in.isAfter(reservation.getCheck_outDate()) && !in.isBefore(reservation.getCheck_inDate())){ used= true; }
                    else{ if(in.isBefore(reservation.getCheck_inDate()) && !out.isBefore(reservation.getCheck_inDate())){ used= true; } }
                }
            }
            if(used==false && !r.isOutOfOrder()){ available_rooms.add(r); }
        }
        return available_rooms;
    }

    public double calculateReservationCost(String roomType, LocalDate start, LocalDate end) throws Exception {
        List<Room> rooms = findRoomByRoomType(roomType);
        if (rooms == null) throw new Exception("Room not found");
        Room room = rooms.get(0);
        long nightsLong = ChronoUnit.DAYS.between(start, end);
        int nights = (int) nightsLong;
        long tooFarintoFuture = ChronoUnit.DAYS.between(LocalDate.now(), end);
        int howManyDaysIntoFuture = (int) tooFarintoFuture;
        if(howManyDaysIntoFuture > 1095) throw new Exception("No booking possible for this date");
        if( start.isBefore(LocalDate.now())) throw new Exception ("Check-in must be today or later");
        if (nights < 1) throw new Exception("Reservation must be for at least 1 night.");

        PricingStrategy strategy;
        boolean isHighSeason = checkIfHighSeason(start);
        boolean isLongStay = (nights > 7);

        if (isHighSeason && isLongStay) {
            strategy = new HighSeasonLongStayPricingStrategy();
        } else if (isHighSeason) {
            strategy = new HighSeasonPricingStrategy();
        } else if (isLongStay) {
            strategy = new LongStayDiscountStrategy();
        } else {
            strategy = new StandardPricingStrategy();
        }

        return room.calculatePrice(nights, strategy);
    }

    private boolean checkIfHighSeason(LocalDate date) {
        int month = date.getMonthValue();
        return month >= 6 && month <= 8;
    }

    public String getMaidRoomList() {
        StringBuilder sb = new StringBuilder("--- ROOM STATUS ---\n");
        for (Room r : roomData) {
            if (!r.isClean() || r.isOccupied() || r.isOutOfOrder()) {
                String state = "";
                if (!r.isClean()) state += "[DIRTY] ";
                if (r.isOccupied()) state += "[OCCUPIED] ";
                if (r.isOutOfOrder()) state += "[BROKEN] ";

                sb.append("Room ").append(r.getRoomNumber()).append(": ").append(state).append("\n");
            }
        }
        return sb.length() > 20 ? sb.toString() : "Nothing to do.";
    }

    public String handleCleaningRoom(Employee maid, int roomNumber) throws Exception {
        Room r = findRoomByNumber(roomNumber);
        if (r == null) throw new Exception("Room not found.");
        r.setClean(true);
        r.setOutOfOrder(false);

        maid.setHoursWorked(maid.getHoursWorked() + 0.5);
        saveRooms(); saveUsers();
        HotelLogger.log("MAID", maid.getName()+" cleaned room numbber: "+roomNumber);

        return "Room " + roomNumber + " cleaned.";
    }

    public String reportDefect(int roomNumber) throws Exception {
        Room r = findRoomByNumber(roomNumber);
        if (r == null) throw new Exception("Room not found.");
        r.setOutOfOrder(true);
        saveRooms();
        HotelLogger.log("DAMAGE", roomNumber+" is out of order because of damage");

        return "Room " + roomNumber + " marked as OUT OF ORDER.";
    }

    public String refillMinibar(int roomNumber, double cost) throws Exception {
        Room r = findRoomByNumber(roomNumber);
        if (!r.isOccupied()) throw new Exception("Room is empty, nobody to charge.");
        Guest targetGuest = null;
        for (Person p : users) {
            if (p instanceof Guest) {
                Guest g = (Guest) p;
                if (g.getCheckedIn() && g.getCard() != null && g.getCard().canOpenRoom(roomNumber)) {
                    targetGuest = g; break;
                }
            }
        }
        if (targetGuest == null) throw new Exception("Could not find guest for this room.");
        targetGuest.getCard().charge(cost);
        saveUsers();
        return "Minibar refilled. Charged: " + cost + " PLN to " + targetGuest.getSurname();
    }

    public String processPayroll() {
        double total = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-25s | %-15s | %-8s | %-12s\n", "Employee", "Role", "Hours", "Payout"));
        sb.append("----------------------------------------------------------------------\n");

        for (Person p : users) {
            if (p instanceof Employee) {
                Employee e = (Employee) p;
                double salary = e.getHoursWorked() * e.getRate();
                if (salary > 0) {
                    sb.append(String.format("%-25s | %-15s | %-8.1f | %-12.2f PLN\n",
                            e.getSurname() + " " + e.getName(),
                            e.getPosition(),
                            e.getHoursWorked(),
                            salary));

                    total += salary;
                    e.setHoursWorked(0);
                }
            }
        }
        sb.append("----------------------------------------------------------------------\n");
        sb.append("TOTAL OUTFLOW: ").append(String.format("%.2f", total)).append(" PLN");
        saveUsers();
        return sb.toString();
    }

    public void addHoursToEmployee(String login, double hours) throws Exception {
        Person p = findUserByLogin(login);
        if (p instanceof Employee) {
            Employee e = (Employee) p;
            e.setHoursWorked(e.getHoursWorked() + hours);
            saveUsers();
        } else {
            throw new Exception("User is not an employee.");
        }
    }

    public void addToBlacklist(String login) throws Exception {
        Person p = findUserByLogin(login);
        if (p instanceof Guest) {
            ((Guest) p).setBlacklisted(true);
            saveUsers();
        } else {
            throw new Exception("User not found or not a guest.");
        }
    }

    public String getAvailableRoomsForSwap(int reservationId) {
        Reservation res = findReservationByID(reservationId);
        if (res == null) return "Error: Reservation not found.";

        Room currentRoom = res.getRoom();
        RoomType requiredType = currentRoom.getType();

        StringBuilder sb = new StringBuilder("Available rooms (" + requiredType.getDisplayName() + "):\n");
        boolean any = false;

        for (Room r : roomData) {
            if (r.getType() == requiredType &&
                    r.getRoomNumber() != currentRoom.getRoomNumber() &&
                    !r.isOccupied() &&
                    !r.isOutOfOrder() &&
                    r.isClean())
            {
                sb.append("Room ").append(r.getRoomNumber()).append("\n");
                any = true;
            }
        }

        if (!any) return "No other rooms of this standard available.";
        return sb.toString();
    }

    public String swapRoomForGuest(int reservationId, int newRoomNumber) throws Exception {
        Reservation res = findReservationByID(reservationId);
        if (res == null) throw new Exception("Reservation not found.");
        Room oldRoom = res.getRoom();
        Room newRoom = findRoomByNumber(newRoomNumber);

        if (newRoom == null || newRoom.isOccupied() || newRoom.isOutOfOrder())
            throw new Exception("New room unavailable.");


        oldRoom.setOutOfOrder(true);
        oldRoom.checkOut();
        newRoom.checkIn();
        res.setRoom(newRoom);

        Person p = findUserByLogin(res.getUsername());
        if (p instanceof Guest) {
            Guest g = (Guest) p;
            if (g.getCard() != null) {
                g.getCard().addRoom(newRoomNumber);
                g.getCard().deleteRoom(oldRoom.getRoomNumber());
            }
        }
        saveReservations(); saveRooms(); saveUsers();
        notifyObservers();
        return "Moved guest to Room " + newRoomNumber + ". Old Room " + oldRoom.getRoomNumber() + " marked as Broken.";
    }

    public void addToMassageQueue(String guestLogin) {
        if (!massageQueue.contains(guestLogin)) {
            massageQueue.add(guestLogin);
            saveQueue();
        }
    }

    public String getMassageQueue() {
        if (massageQueue.isEmpty()) return "Queue empty.";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s | %-25s \n", "Login", "Name Surname"));
        sb.append("------------------------------------\n");

        for(String login : massageQueue) {
            Person p = findUserByLogin(login);
            String nameInfo = "Unknown";


            if (p != null) {
                nameInfo = p.getName() + " " + p.getSurname();
                if (p instanceof Guest) {
                    Guest g = (Guest) p;
                    if (g.getCard() != null) {
                        String cardStr = g.getCard().toString();
                    }
                }
            }
            sb.append(String.format("%-15s | %-25s \n", login, nameInfo));
        }
        return sb.toString();
    }

    public String performMassage(Masseur masseur, String guestLogin) throws Exception {
        Person p = findUserByLogin(guestLogin);
        if (!(p instanceof Guest)) throw new Exception("Guest not found.");

        Guest g = (Guest) p;
        if (g.getCard() == null) throw new Exception("Guest has no card.");

        boolean success = g.getCard().charge(150.0);
        if(!success) throw new Exception("Not enough money on card!");

        masseur.setHoursWorked(masseur.getHoursWorked() + 1.0);

        massageQueue.remove(guestLogin);

        saveUsers();
        saveQueue();

        return "Massage done for " + g.getSurname();
    }

    public String transferWalletToCard(String guestLogin, double amount) throws Exception {
        Person p = findUserByLogin(guestLogin);
        if (!(p instanceof Guest)) throw new Exception("User not found or not a guest.");
        Guest guest = (Guest) p;

        if (amount <= 0) throw new Exception("Amount must be positive.");
        if (guest.getCard() == null) throw new Exception("Guest has no active hotel card to top up.");

        if (guest.payFromWallet(amount)) {
            guest.getCard().topUp(amount);
            saveUsers();
            return "Success. Moved " + amount + " PLN from Wallet to Hotel Card.";
        } else {
            throw new Exception("Insufficient funds in Guest's Wallet. Current balance: " + guest.getWalletBalance());
        }
    }

    public String topUpGuestWallet(String login, double amount) throws Exception {
        Person p = findUserByLogin(login);
        if (!(p instanceof Guest)) throw new Exception("Error.");
        Guest g = (Guest) p;

        if (amount <= 0) throw new Exception("Amount must be positive.");

        g.addToWallet(amount);
        g.addTransaction("Wallet Top Up (Bank): +" + amount + " PLN");
        saveUsers();
        notifyObservers();
        return "Wallet topped up! New balance: " + g.getWalletBalance();
    }

    public String topUpGuestCard(int reservationId, String amountStr) throws Exception
    {
        Reservation reservation = findReservationByID(reservationId);
        if (reservation == null) throw new Exception("No reservation found with that ID.");
        Person person = findUserByLogin(reservation.getUsername());
        if (!(person instanceof Guest)) throw new Exception("User assigned to reservation is not a guest.");
        Guest guest = (Guest) person;

        if(!guest.getCheckedIn()) throw new Exception("User must have an active card/be checked in.");

        double amount = Double.parseDouble(amountStr);
        if (amount <= 0) throw new Exception("Amount must be positive.");

        if (guest.payFromWallet(amount)) {
            guest.getCard().topUp(amount);
            guest.addTransaction("Transfer to Room Card: -" + amount + " PLN");
            saveUsers();
            notifyObservers();
            return "Success! Transferred " + amount + " PLN from Wallet to Card.\nNew Wallet Balance: " + guest.getWalletBalance();
        } else {
            throw new Exception("Insufficient funds in Guest's Wallet! Wallet: " + guest.getWalletBalance());
        }
    }
}
