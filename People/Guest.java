package People;

import Password.WeakPasswordException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Guest extends Person {
    private boolean isCheckedIn;
    private HotelCard hotelCard;
    private boolean isBlacklisted;
    private double walletBalance;
    private List<String> transactionHistory;

    public Guest(String name, String surname, String login, String password) throws WeakPasswordException {
        super (name, surname, login, password);
        this.isCheckedIn = false;
        this.hotelCard=new HotelCard();
        this.isBlacklisted = false;
        this.walletBalance=0.0;
        this.transactionHistory= new ArrayList<>();
    }

    public boolean getCheckedIn() {
        return isCheckedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        isCheckedIn = checkedIn;
    }

    public void receiveCard(HotelCard card) {
        this.hotelCard = card;
        this.isCheckedIn = true;
    }

    public void returnCard() {
        if(this.hotelCard != null) {
            this.hotelCard.deactivate();
            this.hotelCard = null;
        }
        this.isCheckedIn = false;
    }

    public HotelCard getCard() {
        return hotelCard;
    }

    public boolean isBlacklisted() { return isBlacklisted; }
    public void setBlacklisted(boolean blacklisted) { isBlacklisted = blacklisted; }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public void addToWallet(double amount) {
        this.walletBalance += amount;
    }

    public boolean payFromWallet(double amount) {
        if (this.walletBalance >= amount) {
            this.walletBalance -= amount;
            return true;
        }
        return false;
    }

    public void addTransaction(String description) {
        if (this.transactionHistory == null) this.transactionHistory = new ArrayList<>();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.transactionHistory.add(timestamp + " | " + description);
    }

    public String getHistoryString() {
        if (this.transactionHistory == null || this.transactionHistory.isEmpty()) {
            return "No transactions yet.";
        }
        StringBuilder sb = new StringBuilder();

        for (int i = transactionHistory.size() - 1; i >= 0; i--) {
            sb.append(transactionHistory.get(i)).append("\n");
        }
        return sb.toString();
    }
}
