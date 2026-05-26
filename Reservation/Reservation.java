package Reservation;
import Room.Room;
import java.io.Serializable;
import java.time.LocalDate;


public class Reservation implements Serializable{

    int reservationID;
    String username;
    Room room;
    LocalDate check_inDate;
    LocalDate check_outDate;
    double totalPrice;
    String pricingStrategyName;
    
    public Reservation(int reservationID, String username, Room room, LocalDate check_inDate, LocalDate check_outDate, double totalPrice, String pricingStrategyName) {
        this.reservationID=reservationID;
        this.username=username;
        this.room=room;
        this.check_inDate=check_inDate;
        this.check_outDate=check_outDate;
        this.totalPrice=totalPrice;
        this.pricingStrategyName=pricingStrategyName;
    }

    public int getReservationID() {
        return reservationID;
    }

    public String getUsername() {
        return username;
    }
    
    public LocalDate getCheck_inDate() {
        return check_inDate;
    }

    public LocalDate getCheck_outDate() {
        return check_outDate;
    }
    
    public Room getRoom(){
        return room;
    }
    public String getPricingStrategyName() { return pricingStrategyName; }

    public double getTotalPrice() {return totalPrice;}
    
    public String toString() {
        return "Reservation ID: "+ reservationID + " username: "+ username+'\n'+"Check-in date: " + check_inDate+ " Check-out date: "+ check_outDate+'\n'+ room.toString()+'\n'+"Total price: "+totalPrice+" PLN"+"  |  Price factor: "+pricingStrategyName+'\n';
    }

    public void setRoom( Room room) {
        this.room=room;
    }

    
    public void setUsername( String username) {
        this.username=username;
    }
    
    public void setCheck_inDate( LocalDate check_inDate) {
        this.check_inDate= check_inDate;
    }

    public void setCheck_outDate( LocalDate check_outDate) {
        this.check_outDate= check_outDate;
    }
    
    public void setReservationID(int reservationID) {
        this.reservationID= reservationID;
    }

  

}










