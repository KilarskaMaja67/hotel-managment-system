package File;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class Read {
    public static <T> List<T> read(String nazwa) {
        File file = new File(nazwa);
        // jesli plik nie istnieje zwracamy pusta liste
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream dane = new ObjectInputStream(new FileInputStream(nazwa))) {
            Object obj = dane.readObject();
            // Sprawdzamy czy to jest lista
            if (obj instanceof List) {
                return (List<T>) obj;
            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //W razie bledu jest zwracana pusta lista
            return new ArrayList<>();
        }
    }
}
