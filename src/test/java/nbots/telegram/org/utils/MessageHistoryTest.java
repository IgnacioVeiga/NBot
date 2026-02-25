package nbots.telegram.org.utils;

import nbots.telegram.org.testsupport.BaseBotTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageHistoryTest extends BaseBotTest {
    @TempDir
    Path tempDir;

    @Test
    void storesHistoryPerChat() {
        MessageHistory.setHistoryDirOverrideForTests(tempDir.resolve("history"));

        MessageHistory.logMessage(1L, "alice", "hello");
        MessageHistory.logMessage(2L, "bob", "world");

        assertEquals(1, MessageHistory.getHistory(1L).size());
        assertEquals("alice: hello", MessageHistory.getHistory(1L).getFirst());
        assertEquals("bob: world", MessageHistory.getHistory(2L).getFirst());
    }

    @Test
    void returnsEmptyListWhenHistoryFileDoesNotExist() {
        MessageHistory.setHistoryDirOverrideForTests(tempDir.resolve("history"));
        assertTrue(MessageHistory.getHistory(999L).isEmpty());
    }
}
