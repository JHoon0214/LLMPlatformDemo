package cnsa.demo.service.UserService;
import java.util.regex.Pattern;
public class EmailValidator {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValid(String email) {
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }
}
