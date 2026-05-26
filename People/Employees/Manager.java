package People.Employees;

import Password.WeakPasswordException;
import People.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Manager extends Employee {
    public Manager (String name, String surname, String login, String password, double rate) throws WeakPasswordException {
        super(name, surname, login, password, "Manager", rate, 0, false, 26, true);
    }
    public static void hireEmployee(List<Person> users, String type, String name, String surname, String login, String password, double rate) throws WeakPasswordException {
        Person newEmployee = null;

        switch (type) {
            case "Recepcionist":
                newEmployee = new Recepcionist(name, surname, login, password, rate);
                break;
            case "Masseur":
                newEmployee = new Masseur(name, surname, login, password, rate);
                break;
            case "Maid":
                newEmployee = new Maid(name, surname, login, password, rate);
                break;
            case "SecurityGuard":
                newEmployee = new SecurityGuard(name, surname, login, password, rate);
                break;
            case "Manager":
                newEmployee = new Manager(name, surname, login, password, rate);
                break;
            default:
                System.out.println("Unknown position: " + type);
                return;
        }

        if (newEmployee != null) {
            users.add(newEmployee);
        }
    }

    public void displayEmployees(List<Person> users, String filterPosition) {
        List<Employee> employeesOnly = new ArrayList<>();

        for (Person p : users) {
            if (p instanceof Employee) {
                Employee emp = (Employee) p;
                if (filterPosition == null || filterPosition.isEmpty() || emp.getPosition().equalsIgnoreCase(filterPosition)) {
                    employeesOnly.add(emp);
                }
            }
        }

        if (employeesOnly.isEmpty()) {
            System.out.println("No employees make the criteria.");
            return;
        }

        employeesOnly.sort(Comparator.comparing(Person::getSurname)
                .thenComparing(Person::getName));

        for (Employee e : employeesOnly) {
            System.out.printf(e.getSurname() + " " + e.getName() + " | " + e.getPosition() + " | " + e.getRate());
        }
    }

    //do gui
    public static String displayEmployeesString(List<Person> users, String filterPosition) {
        List<Employee> employeesOnly = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%-30s | %-20s | %s\n", "Name", "Position", "Rate"));
        sb.append("--------------------------------------------------------------------------\n");

        for (Person p : users) {
            if (p instanceof Employee) {
                Employee emp = (Employee) p;
                if (filterPosition == null || filterPosition.isEmpty() || filterPosition.equalsIgnoreCase("All") ||emp.getPosition().equalsIgnoreCase(filterPosition)) {
                    employeesOnly.add(emp);
                }
            }
        }

        if (employeesOnly.isEmpty()) {
            return "No employees make the criteria.";
        }

        employeesOnly.sort(Comparator.comparing(Person::getSurname)
                .thenComparing(Person::getName));

        for (Employee e : employeesOnly) {
            sb.append(String.format("%-30s | %-20s | %.2f PLN\n",
                    e.getSurname() + " " + e.getName(),
                    e.getPosition(),
                    e.getRate()));
        }

        return sb.toString();
    }
}