package nbots.telegram.org.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static void log(String message) {
        System.out.println(LocalDateTime.now().format(FORMATTER) + " - " + message);
    }

    public static void log(String message, Throwable throwable) {
        log(message + " | " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage());
    }
}
