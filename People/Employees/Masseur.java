package People.Employees;

import Password.WeakPasswordException;

public class Masseur extends Employee {
    public Masseur (String name, String surname, String login, String password, double rate) throws WeakPasswordException {
        super(name, surname, login, password, "Masseur", rate, 0, false, 26, true);
    }
    //bonus za kazda wymasowana opcje
    // wyswietl liste klientow
    //dodaj dzien w jaki można sie zapisac
    //dodaj dni w jakie jestes niedostepny
    //gosc nie przyszedl
    //gosc zostal wymasowany
}
