package People;

import Password.PasswordValidator;
import Password.WeakPasswordException;

import java.nio.charset.StandardCharsets; //standard kodowania znakow
import java.security.MessageDigest; // sluzy do haszowania
import java.security.NoSuchAlgorithmException; //do obslugiwania bledu
import java.io.Serializable;


public abstract class Person implements Serializable {
    private String name;
    private String surname;
    private String login;
    private String password;

    public Person(String name, String surname, String login, String password) throws WeakPasswordException {
        PasswordValidator.validate(password);

        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = hashPassword(password);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // wybranie algorytmu hashowania (szybki, ale łatwo go złamać, ale do naszego projektu moze byc xd)
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Błąd algorytmu haszowania", e);
        }
    }

    public boolean checkPassword(String password) {
        String hashedPassword = hashPassword(password);
        return hashedPassword.equals(this.password);
    }

    //gettery i settery
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) throws WeakPasswordException {
        PasswordValidator.validate(password);
        this.password = hashPassword(password);
    }
}

