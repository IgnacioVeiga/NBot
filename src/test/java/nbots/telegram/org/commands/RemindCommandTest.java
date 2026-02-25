package nbots.telegram.org.commands;

import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.services.ReminderService;
import nbots.telegram.org.testsupport.BaseBotTest;
import nbots.telegram.org.testsupport.TelegramUpdateFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RemindCommandTest extends BaseBotTest {
    @TempDir
    Path tempDir;

    @Test
    void validatesUsageAndNumericSeconds() {
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        RemindCommand command = new RemindCommand();
        command.execute(TelegramUpdateFactory.textUpdate(1L, 10L, "u", "User", "en", "/remind"));
        command.execute(TelegramUpdateFactory.textUpdate(1L, 10L, "u", "User", "en", "/remind abc hi"));

        assertTrue(messages.get(0).contains("Usage: /remind"));
        assertTrue(messages.get(1).contains("number of seconds"));
    }

    @Test
    void schedulesReminderAndSendsLocalizedReminderMessage() throws Exception {
        ReminderService.setRemindersFileOverrideForTests(tempDir.resolve("reminders.json"));
        CopyOnWriteArrayList<String> messages = new CopyOnWriteArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new RemindCommand().execute(TelegramUpdateFactory.textUpdate(1L, 10L, "u", "User", "en", "/remind 1 test"));

        waitUntil(() -> messages.stream().anyMatch(m -> m.contains("⏰ Reminder: test")), Duration.ofSeconds(3));
        assertTrue(messages.stream().anyMatch(m -> m.contains("I'll remind you")));
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
        boolean ok();
    }
}
