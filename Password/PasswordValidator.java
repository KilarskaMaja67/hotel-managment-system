package Password;

public class PasswordValidator {

    public static void validate(String password) throws WeakPasswordException {
        if (password == "") {
            throw new WeakPasswordException("Hasło nie może być puste.");
        }

        if (password.length() < 8) {
            throw new WeakPasswordException("Hasło jest za krótkie! (min. 8 znaków)");
        }

        boolean hasDigit = false;
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            }
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (!Character.isLetterOrDigit(c)){
                //zakladamy ze spacja to tez znak specjalny
                hasSpecial = true;
            }
        }

        if (!hasLowerCase) {
            throw new WeakPasswordException("Hasło musi zawierać małą literę.");
        }

        if (!hasUpperCase) {
            throw new WeakPasswordException("Hasło musi zawierać wielką literę.");
        }

        if (!hasDigit) {
            throw new WeakPasswordException("Hasło musi zawierać cyfrę.");
        }

        if (!hasSpecial) {
            throw new WeakPasswordException("Hasło musi zawierać znak specjalny (np. @, #, !).");
        }
    }
}