package Room;

import java.util.Set;

public enum RoomType {
    STANDARD("Standard", 200.00, Set.of(
            Equipment.WIFI,
            Equipment.TV
    )),

    FAMILY("Family", 350.00, Set.of(
            Equipment.WIFI,
            Equipment.TV,
            Equipment.KITCHEN,
            Equipment.BALCONY
    )),

    BUSINESS("Business", 400.00, Set.of(
            Equipment.WIFI,
            Equipment.AC,
            Equipment.MINIBAR,
            Equipment.TV
    )),

    VIP("VIP", 600.00, Set.of(
            Equipment.WIFI,
            Equipment.AC,
            Equipment.MINIBAR,
            Equipment.BALCONY,
            Equipment.JACUZZI
    )),

    SUITE("Suite", 1000.00, Set.of(
            Equipment.WIFI,
            Equipment.AC,
            Equipment.MINIBAR,
            Equipment.JACUZZI,
            Equipment.KITCHEN,
            Equipment.TV
    ));

    private final String displayName;
    private final double basePrice;
    private final Set<Equipment> equipment;

    RoomType(String displayName, double basePrice, Set<Equipment> equipment) {
        this.displayName = displayName;
        this.basePrice = basePrice;
        this.equipment=equipment;
    }

    public Set<Equipment> getEquipment() {
        return equipment;
    }

    public String getDisplayName() { return displayName; }
    public double getBasePrice() { return basePrice; }

    public String toString() {
        StringBuilder equipmentBuilder = new StringBuilder();

        if (getEquipment().isEmpty()) {
            equipmentBuilder.append("No additional equipment");
        } else {
            for (Equipment equipment : getEquipment()) {
                equipmentBuilder.append(equipment.getDescription()).append(", ");
            }

            if (equipmentBuilder.length() > 2) {
                equipmentBuilder.setLength(equipmentBuilder.length() - 2);
            }
        }

        return
                " [" + getDisplayName() + "]" +
                        " - Price: " + getBasePrice() + " PLN" +
                        "\n\tRoom.Equipment: " + equipmentBuilder.toString();
    }

}



