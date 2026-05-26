package Room;
import Room.Strategy.PricingStrategy;
import Room.Strategy.StandardPricingStrategy;

import java.io.Serializable;

public class Room implements Serializable{
    private int roomNumber;
    private int floor;
    private RoomType type;
    private boolean isClean;
    private boolean isOccupied;
    private boolean isOutOfOrder;

    public Room(int roomNumber, int floor, RoomType type) {
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.type = type;
        this.isClean = true;
        this.isOccupied=false;
        this.isOutOfOrder = false;
    }

    public void checkIn() {
        this.isOccupied = true;
    }

    public void checkOut() {
        this.isOccupied = false;
        this.isClean = false;
    }

    public double calculatePrice(int nights, PricingStrategy strategy) {
        return strategy.calculate(this.type.getBasePrice(), nights);
    }

    public double calculatePrice(int nights) {
        return calculatePrice(nights, new StandardPricingStrategy());
    }

    @Override
    public String toString() {
        StringBuilder equipmentBuilder = new StringBuilder();

        if (type.getEquipment().isEmpty()) {
            equipmentBuilder.append("No additional equipment");
        } else {
            for (Equipment equipment : type.getEquipment()) {
                equipmentBuilder.append(equipment.getDescription()).append(", ");
            }

            if (equipmentBuilder.length() > 2) {
                equipmentBuilder.setLength(equipmentBuilder.length() - 2);
            }
        }

        String status = isOutOfOrder ? " [OUT OF ORDER]" : "";

        return "Room nr " + roomNumber +
                " [" + type.getDisplayName() + "]" + status +
                " - Price: " + type.getBasePrice() + " PLN" +
                "\n\tEquipment: " + equipmentBuilder.toString();
    }

    public int getRoomNumber() { return roomNumber; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }

    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }

    public RoomType getType() { return type; }
    public void setType(RoomType type) { this.type = type; }

    public boolean isClean() { return isClean; }
    public void setClean(boolean clean) { isClean = clean; }

    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean occupied) { isOccupied = occupied; }

    public boolean isOutOfOrder() { return isOutOfOrder; }
    public void setOutOfOrder(boolean outOfOrder) { isOutOfOrder = outOfOrder; }
}
