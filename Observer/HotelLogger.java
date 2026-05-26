package Observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
public class HotelLogger {
    private static List<LogObserver> observers = new ArrayList<>();

    private static List<String> history = new ArrayList<>();

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void addObserver(LogObserver o) {
        observers.add(o);

        for (String oldLog : history) {
            o.onNewLog(oldLog);
        }
    }

    public static void log(String category, String text) {
        String timestamp = LocalDateTime.now().format(dtf);
        String formattedMessage = String.format("[%s] [%s] %s", timestamp, category, text);

        history.add(formattedMessage);

        for (LogObserver o : observers) {
            o.onNewLog(formattedMessage);
        }
    }
}