package People.Employees;

import People.Person;
import Password.WeakPasswordException;

public abstract class Employee extends Person {
    private String position;
    private double rate; //stawka godzinowa
    private double hoursWorked; //przepracowane godziny
    private boolean leave; //czy na urlopie
    private int leaveBalance; //ile pracownik ma dni urlopu
    private boolean firstLogin;

    public Employee (String name, String surname, String login, String password, String position, double rate, double hoursWorked, boolean leave, int leaveBalance, boolean firstLogin) throws WeakPasswordException {
        super(name, surname, login, password);
        this.position = position;
        this.rate = rate;
        this.hoursWorked = hoursWorked;
        this.leave = leave;
        this.leaveBalance = leaveBalance;
        this.firstLogin = firstLogin;
    }

    public void promotion (double amount) {
        this.rate += amount;
        System.out.println("Nowa stawka wynosi: " + this.rate + " zł/h.");
    }

    public void paycheck () {
        double paycheck = this.rate * this.hoursWorked;
        System.out.println("Pracownikowi (" + this.getName() +") należy wypłacić: " + paycheck + " zł.");
        this.hoursWorked = 0;
    }

    public void goOnLeave (int daysAmount) {
        if (this.leave) {
            System.out.println("Pracownik już jest na urlopie!");
        } else {
            if (daysAmount < leaveBalance) {
                System.out.println("Pracownik nie ma wystrczających dni urlopu!");
            } else {
                this.leave = true;
                this.leaveBalance -= daysAmount;
                System.out.println("Urlop został przyznany!");
            }
        }
    }

    //gettery i settery
    public String getPosition() {
        return position;
    }

    public double getRate() {
        return rate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public boolean getisLeave() {
        return leave;
    }

    public int getLeaveBalance() {
        return leaveBalance;
    }

    public boolean getIsFirstLogin() {
        return firstLogin;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public void setLeave(boolean leave) {
        this.leave = leave;
    }

    public void setLeaveBalance(int leaveBalance) {
        this.leaveBalance = leaveBalance;
    }

    public void setIsFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }
}