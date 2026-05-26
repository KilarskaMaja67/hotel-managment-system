package People.Employees;

import Password.WeakPasswordException;

public class Maid extends Employee {
    private int cleanedRoomsNumber;
    public Maid (String name, String surname, String login, String password, double rate) throws WeakPasswordException {
        super(name, surname, login, password, "Maid", rate, 0, false, 26, true);
    }

}
