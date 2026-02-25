package nbots.telegram.org.commands;

import nbots.telegram.org.services.DeleteMessageService;
import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.testsupport.BaseBotTest;
import nbots.telegram.org.testsupport.TelegramUpdateFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeleteCommandTest extends BaseBotTest {
    @Test
    void sendsUsageWhenTargetMessageIdIsMissing() {
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new DeleteCommand().execute(TelegramUpdateFactory.textUpdate(1L, 10L, "u", "User", "en", "/delete"));

        assertTrue(messages.getFirst().contains("Usage"));
    }

    @Test
    void sendsUsageWhenMessageIdIsInvalid() {
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new DeleteCommand().execute(TelegramUpdateFactory.textUpdate(1L, 10L, "u", "User", "en", "/delete abc"));

        assertTrue(messages.getFirst().contains("Usage"));
    }

    @Test
    void deletesReplyTargetWhenReplyMessageExists() {
        AtomicInteger deletedMessageId = new AtomicInteger();
        DeleteMessageService.setExecutorOverrideForTests((chatId, messageId) -> {
            deletedMessageId.set(messageId);
            return true;
        });

        new DeleteCommand().execute(TelegramUpdateFactory.textUpdateWithReply(1L, 10L, "u", "User", "en", 100, "/delete", 77));

        assertEquals(77, deletedMessageId.get());
    }

    @Test
    void sendsErrorWhenDeleteFails() {
        DeleteMessageService.setExecutorOverrideForTests((chatId, messageId) -> false);
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new DeleteCommand().execute(TelegramUpdateFactory.textUpdate(1L, 10L, "u", "User", "es", "/delete 7"));

        assertTrue(messages.getFirst().contains("No pude borrar"));
    }
}
