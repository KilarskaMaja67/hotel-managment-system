package People;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HotelCard implements Serializable {
    private String cardID;
    private double balance;
    private List<Integer> authorizedRoomNumbers;
    public boolean isActive;

    public HotelCard() {
        this.cardID = UUID.randomUUID().toString().substring(0, 8);
        this.balance = 0.0;
        this.isActive = true;
        this.authorizedRoomNumbers=new ArrayList<>();
    }

    public boolean charge(double amount) {
        if (!isActive) return false;
        this.balance =this.balance-amount;
        return true;
    }

    public void chargeRes(double amount){
        this.balance=this.balance-amount;
    }

    public void topUp(double amount) {
        this.balance =this.balance+ amount;
    }

    public boolean canOpenRoom(int roomNumber) {
        return isActive && this.authorizedRoomNumbers.contains(roomNumber);
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public double getBalance() {
        return balance;
    }

    public String getCardID() {
        return cardID;
    }

    @Override
    public String toString() {
        StringBuilder sb= new StringBuilder("Karta ID: " + cardID + " | Room(s): ");
        for(int rn: this.authorizedRoomNumbers)
        {
            sb.append(rn+" | ");
        }
        sb.append(" | Saldo: " + balance + " PLN");
        return sb.toString();
    }

    public void addRoom(int roomNumber)
    {
        this.authorizedRoomNumbers.add(roomNumber);
    }

    public void deleteRoom(int roomNumber)
    {
        this.authorizedRoomNumbers.remove((Integer)roomNumber);
    }
}
