package nbots.telegram.org.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MessageHistory {
    private static final String HISTORY_FILE = "message_history.txt";

    public static void logMessage(String user, String text) {
        try (PrintWriter out = new PrintWriter(new FileWriter(HISTORY_FILE, true))) {
            out.println(user + ": " + text);
        } catch (IOException e) {
            Logger.log("Error guardando historial: " + e.getMessage());
        }
    }

    public static List<String> getHistory() {
        try {
            return Files.readAllLines(Paths.get(HISTORY_FILE));
        } catch (IOException e) {
            Logger.log("Error leyendo historial: " + e.getMessage());
            return List.of();
        }
    }
}

