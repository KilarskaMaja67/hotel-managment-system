package People.Employees;

import People.Guest;
import People.Person;
import Password.WeakPasswordException;

import java.util.List;

public class SecurityGuard extends Employee {
    public SecurityGuard (String name, String surname, String login, String password, double rate) throws WeakPasswordException {
        super(name, surname, login, password, "SecurityGuard", rate, 0, false, 26, true);
    }
    public void checkPerson(List<Person> users, String nameToCheck, String surnameToCheck) {
        boolean found = false;
        for (Person p : users) {
            if (p.getName().equalsIgnoreCase(nameToCheck) && p.getSurname().equalsIgnoreCase(surnameToCheck)) {
                found = true;
                System.out.println("ZNALEZIONO OSOBĘ: " + p.getName() + " " + p.getSurname());

                if (p instanceof Guest) {
                    Guest g = (Guest) p;
                    System.out.println("Rola: GOŚĆ HOTELOWY");
                    if (g.getCheckedIn()) {
                        System.out.println("-> MOŻNA WPUŚCIĆ.");
                    } else {
                        System.out.println("-> UWAGA: Gość nie jest aktualnie zameldowany.");
                    }

                } else if (p instanceof Employee) {
                    Employee e = (Employee) p;
                    System.out.println("Rola: PRACOWNIK");
                    System.out.println("Stanowisko: " + e.getPosition());
                }
                return;
            }
        }

        if (!found) {
            System.out.println(nameToCheck + " " + surnameToCheck + "' NIE istnieje w bazie.");
        }
    }

    //do gui
    public static String checkPersonString(List<Person> users, String nameToCheck, String surnameToCheck) {
        StringBuilder sb = new StringBuilder();

        for (Person p : users) {
            if (p.getName().equalsIgnoreCase(nameToCheck) && p.getSurname().equalsIgnoreCase(surnameToCheck)) {

                sb.append("FOUND PERSON: ").append(p.getName()).append(" ").append(p.getSurname()).append("\n");

                if (p instanceof Guest) {
                    Guest g = (Guest) p;
                    sb.append("Role: GUEST\n");
                    if (g.getCheckedIn()) {
                        sb.append("-> ENTRANCE ALLOWED.\n");
                    } else {
                        sb.append("-> WARNING: Guest isn't currently checked-in.\n");
                    }

                } else if (p instanceof Employee) {
                    Employee e = (Employee) p;
                    sb.append("Role: EMPLOYEE\n");
                    sb.append("Position: ").append(e.getPosition()).append("\n");
                }

                return sb.toString();
            }
        }

        return nameToCheck + " " + surnameToCheck + " Does NOT exist in the database.";
    }
}