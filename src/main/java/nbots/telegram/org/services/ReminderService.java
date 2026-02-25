package nbots.telegram.org.services;

import nbots.telegram.org.i18n.BotLanguage;
import nbots.telegram.org.utils.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ReminderService {
    private static final Path DEFAULT_REMINDERS_FILE = Path.of("data", "reminders.json");
    private static volatile Path remindersFileOverrideForTests;
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "nbot-reminders");
        thread.setDaemon(true);
        return thread;
    });
    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);

    private ReminderService() {
    }

    public static void initialize() {
        if (!INITIALIZED.compareAndSet(false, true)) {
            return;
        }

        List<ReminderItem> pending = loadReminders();
        long now = Instant.now().toEpochMilli();
        for (ReminderItem reminder : pending) {
            long delayMillis = Math.max(0, reminder.triggerAtEpochMillis() - now);
            scheduleExecution(reminder, delayMillis);
        }
        Logger.log("ReminderService initialized. Pending reminders recovered: " + pending.size());
    }

    public static void scheduleReminder(long chatId, int seconds, String message, BotLanguage language) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("seconds must be > 0");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message is required");
        }

        long triggerAt = Instant.now().plusSeconds(seconds).toEpochMilli();
        ReminderItem reminder = new ReminderItem(
                UUID.randomUUID().toString(),
                chatId,
                triggerAt,
                message.trim(),
                language == null ? BotLanguage.EN.code() : language.code()
        );

        addReminder(reminder);
        scheduleExecution(reminder, seconds * 1000L);
    }

    private static void scheduleExecution(ReminderItem reminder, long delayMillis) {
        EXECUTOR.schedule(() -> executeReminder(reminder), Math.max(0, delayMillis), TimeUnit.MILLISECONDS);
    }

    private static void executeReminder(ReminderItem reminder) {
        try {
            boolean spanish = "es".equalsIgnoreCase(reminder.languageCode());
            String prefix = spanish ? "⏰ Recordatorio: " : "⏰ Reminder: ";
            MessageService.sendMessage(reminder.chatId(), prefix + reminder.message());
        } finally {
            removeReminder(reminder.id());
        }
    }

    private static synchronized void addReminder(ReminderItem reminder) {
        List<ReminderItem> reminders = new ArrayList<>(loadReminders());
        reminders.add(reminder);
        saveReminders(reminders);
    }

    private static synchronized void removeReminder(String reminderId) {
        List<ReminderItem> reminders = new ArrayList<>(loadReminders());
        reminders.removeIf(reminder -> reminder.id().equals(reminderId));
        saveReminders(reminders);
    }

    private static synchronized List<ReminderItem> loadReminders() {
        Path remindersFile = remindersFile();
        if (!Files.exists(remindersFile)) {
            return List.of();
        }

        try {
            String content = Files.readString(remindersFile, StandardCharsets.UTF_8).trim();
            if (content.isEmpty()) {
                return List.of();
            }

            JSONArray array = new JSONArray(content);
            List<ReminderItem> reminders = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.optJSONObject(i);
                if (obj == null) {
                    continue;
                }
                String id = obj.optString("id", "");
                long chatId = obj.optLong("chatId", Long.MIN_VALUE);
                long triggerAt = obj.optLong("triggerAtEpochMillis", Long.MIN_VALUE);
                String message = obj.optString("message", "").trim();
                String languageCode = obj.optString("languageCode", "es").trim();
                if (id.isBlank() || chatId == Long.MIN_VALUE || triggerAt == Long.MIN_VALUE || message.isBlank()) {
                    Logger.log("Reminder ignorado por formato inválido en storage.");
                    continue;
                }
                reminders.add(new ReminderItem(id, chatId, triggerAt, message, languageCode));
            }
            return reminders;
        } catch (Exception e) {
            Logger.log("Error leyendo recordatorios persistidos", e);
            return List.of();
        }
    }

    private static synchronized void saveReminders(List<ReminderItem> reminders) {
        Path remindersFile = remindersFile();
        JSONArray array = new JSONArray();
        for (ReminderItem reminder : reminders) {
            array.put(new JSONObject()
                    .put("id", reminder.id())
                    .put("chatId", reminder.chatId())
                    .put("triggerAtEpochMillis", reminder.triggerAtEpochMillis())
                    .put("message", reminder.message())
                    .put("languageCode", reminder.languageCode()));
        }

        try {
            Files.createDirectories(remindersFile.getParent());
            Files.writeString(
                    remindersFile,
                    array.toString(2),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );
        } catch (IOException e) {
            Logger.log("Error guardando recordatorios persistidos", e);
        }
    }

    private static Path remindersFile() {
        Path override = remindersFileOverrideForTests;
        return override == null ? DEFAULT_REMINDERS_FILE : override;
    }

    public static void setRemindersFileOverrideForTests(Path remindersFile) {
        remindersFileOverrideForTests = remindersFile;
    }

    public static void clearRemindersFileOverrideForTests() {
        remindersFileOverrideForTests = null;
    }

    public static void resetStateForTests() {
        INITIALIZED.set(false);
    }

    private record ReminderItem(String id, long chatId, long triggerAtEpochMillis, String message, String languageCode) {
    }
}
