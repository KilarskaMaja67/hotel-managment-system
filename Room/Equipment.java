package Room;

public enum Equipment {
    WIFI("Free Wi-Fi"),
    TV("Smart TV"),
    AC("Air Conditioning"),
    BALCONY("Balcony"),
    MINIBAR("Minibar"),
    JACUZZI("Private Jacuzzi"),
    KITCHEN("Kitchenette");

    private final String description;

    Equipment(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
