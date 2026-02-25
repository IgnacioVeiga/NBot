package nbots.telegram.org.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class MessageHistory {
    private static final Path DEFAULT_HISTORY_DIR = Path.of("data", "history");
    private static volatile Path historyDirOverrideForTests;

    private MessageHistory() {
    }

    public static synchronized void logMessage(long chatId, String user, String text) {
        if (text == null || text.isBlank()) {
            return;
        }

        String safeUser = (user == null || user.isBlank()) ? "unknown" : user.trim();
        Path historyFile = historyFile(chatId);

        Path historyDir = historyDir();
        try {
            Files.createDirectories(historyDir);
        } catch (IOException e) {
            Logger.log("Error creando directorio de historial: " + e.getMessage());
            return;
        }

        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(
                historyFile,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        ))) {
            out.println(safeUser + ": " + text.trim());
        } catch (IOException e) {
            Logger.log("Error guardando historial: " + e.getMessage());
        }
    }

    public static synchronized List<String> getHistory(long chatId) {
        Path historyFile = historyFile(chatId);
        if (!Files.exists(historyFile)) {
            return List.of();
        }
        try {
            return Files.readAllLines(historyFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Logger.log("Error leyendo historial: " + e.getMessage());
            return List.of();
        }
    }

    private static Path historyFile(long chatId) {
        return historyDir().resolve("message_history_" + chatId + ".txt");
    }

    private static Path historyDir() {
        Path override = historyDirOverrideForTests;
        return override == null ? DEFAULT_HISTORY_DIR : override;
    }

    public static void setHistoryDirOverrideForTests(Path historyDir) {
        historyDirOverrideForTests = historyDir;
    }

    public static void clearHistoryDirOverrideForTests() {
        historyDirOverrideForTests = null;
    }
}
