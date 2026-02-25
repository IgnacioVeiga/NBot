package nbots.telegram.org.commands;

import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.testsupport.BaseBotTest;
import nbots.telegram.org.testsupport.TelegramUpdateFactory;
import nbots.telegram.org.utils.MessageHistory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HistoryCommandTest extends BaseBotTest {
    @TempDir
    Path tempDir;

    @Test
    void returnsEmptyMessageWhenNoHistoryForChat() {
        MessageHistory.setHistoryDirOverrideForTests(tempDir.resolve("history"));
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new HistoryCommand().execute(TelegramUpdateFactory.textUpdate(1L, 10L, "u", "User", "en", "/history"));

        assertTrue(messages.getFirst().contains("No message history yet."));
    }

    @Test
    void readsOnlyCurrentChatHistory() {
        MessageHistory.setHistoryDirOverrideForTests(tempDir.resolve("history"));
        MessageHistory.logMessage(1L, "u1", "hello");
        MessageHistory.logMessage(2L, "u2", "other chat");

        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new HistoryCommand().execute(TelegramUpdateFactory.textUpdate(1L, 10L, "u", "User", "en", "/history"));

        assertTrue(messages.getFirst().contains("hello"));
        assertFalse(messages.getFirst().contains("other chat"));
    }
}
