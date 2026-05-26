package File;

import java.io.*;
import java.util.List;

public class Save {
    public static <T> void save(List<T> lista, String nazwa) {
        try (ObjectOutputStream dane = new ObjectOutputStream(new FileOutputStream(nazwa))) {
            dane.writeObject(lista);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }
}
