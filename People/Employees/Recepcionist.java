package People.Employees;

import People.Guest;
import People.Person;
import Password.WeakPasswordException;
import Reservation.Reservation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Recepcionist extends Employee {
    public Recepcionist (String name, String surname, String login, String password, double rate) throws WeakPasswordException {
        super(name, surname, login, password, "Recepcionist", rate, 0, false, 26, true);
    }

    public void displayGuests(List<Person> users) {
        List<Guest> guestsOnly = new ArrayList<>();

        for (Person p : users) {
            if (p instanceof Guest) {
                guestsOnly.add((Guest) p);
            }
        }

        if (guestsOnly.isEmpty()) {
            System.out.println("Brak zarejestrowanych gości.");
            return;
        }
        guestsOnly.sort(Comparator.comparing(Person::getSurname)
                .thenComparing(Person::getName));
        for (Guest g : guestsOnly) {
            String status = g.getCheckedIn() ? "Checked-in" : "Not checked-in";
            System.out.printf(
                    g.getSurname() + " " + g.getName()+ " | " + g.getLogin()+ " | " + status);
        }
    }

    //do gui
    public static String displayGuestsString(List<Person> users, List <Reservation> reservations) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%-15s | %-10s | %-30s | %-10s | %-10s\n",
                "Name Surname", "Login", "Rooms", "Card(PLN)", "Wallet(PLN)"));
        sb.append("--------------------------------------------------------------------------------------------------------\n");

        for (Person p : users) {
            if (p instanceof Guest) {
                Guest g = (Guest) p;
                double cardBal = (g.getCard() != null) ? g.getCard().getBalance() : 0.0;

                List<String> bookingInfo = new ArrayList<>();

                for (Reservation r : reservations) {
                    if (r.getUsername().equals(g.getLogin())) {

                        String entry = r.getRoom().getRoomNumber() + "(#" + r.getReservationID() + ")";
                        bookingInfo.add(entry);
                    }
                }

                String roomsStr = bookingInfo.isEmpty() ? "---" : String.join(", ", bookingInfo);

                sb.append(String.format("%-15s | %-10s | %-30s | %-10.2f | %-10.2f\n",
                        g.getName() + " " + g.getSurname(),
                        g.getLogin(),
                        roomsStr,
                        cardBal,
                        g.getWalletBalance()));
            }
        }
        return sb.toString();

    }

}
    //tworzenie rezerwacji
    //przyjmowanie gosci
    //check out
