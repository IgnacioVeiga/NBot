package nbots.telegram.org.services;

import nbots.telegram.org.i18n.BotLanguage;
import nbots.telegram.org.testsupport.BaseBotTest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReminderServiceTest extends BaseBotTest {
    @TempDir
    Path tempDir;

    @Test
    void scheduleReminderPersistsLanguageCode() throws Exception {
        Path remindersFile = tempDir.resolve("reminders.json");
        ReminderService.setRemindersFileOverrideForTests(remindersFile);

        // Keep the sender stub installed until the scheduled reminder fires to avoid using Telegram in tests.
        MessageService.setSenderOverrideForTests((chatId, text) -> {});
        ReminderService.scheduleReminder(1L, 1, "hola", BotLanguage.ES);

        String content = Files.readString(remindersFile, StandardCharsets.UTF_8);
        JSONArray array = new JSONArray(content);
        JSONObject reminder = array.getJSONObject(0);
        assertTrue("es".equals(reminder.getString("languageCode")));

        waitUntil(() -> Files.readString(remindersFile, StandardCharsets.UTF_8).contains("[]"), Duration.ofSeconds(3));
    }

    @Test
    void initializeRecoversPendingReminderAndSendsLocalizedPrefix() throws Exception {
        Path remindersFile = tempDir.resolve("reminders.json");
        ReminderService.setRemindersFileOverrideForTests(remindersFile);

        JSONArray array = new JSONArray();
        array.put(new JSONObject()
                .put("id", "r1")
                .put("chatId", 1L)
                .put("triggerAtEpochMillis", Instant.now().minusMillis(10).toEpochMilli())
                .put("message", "test")
                .put("languageCode", "en"));
        Files.writeString(remindersFile, array.toString(), StandardCharsets.UTF_8);

        CopyOnWriteArrayList<String> sent = new CopyOnWriteArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> sent.add(text));

        ReminderService.resetStateForTests();
        ReminderService.initialize();

        waitUntil(() -> sent.stream().anyMatch(m -> m.contains("⏰ Reminder: test")), Duration.ofSeconds(2));
    }

    private static void waitUntil(Check check, Duration timeout) throws Exception {
        long end = System.currentTimeMillis() + timeout.toMillis();
        while (System.currentTimeMillis() < end) {
            if (check.ok()) {
                return;
            }
            Thread.sleep(50);
        }
        throw new AssertionError("Condition not met within timeout");
    }

    @FunctionalInterface
    private interface Check {
        boolean ok() throws Exception;
    }
}
