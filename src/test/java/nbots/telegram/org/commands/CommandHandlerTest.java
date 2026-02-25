package nbots.telegram.org.commands;

import nbots.telegram.org.testsupport.TelegramUpdateFactory;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandHandlerTest {
    @Test
    void handlesRegisteredCommandAndSupportsBotMention() {
        CommandHandler handler = new CommandHandler();
        AtomicInteger executions = new AtomicInteger();
        handler.register("/help", update -> executions.incrementAndGet());

        Update update = TelegramUpdateFactory.textUpdate(10L, 100L, "user", "User", "en", "/HELP@MyBot now");

        assertTrue(handler.handle(update));
        assertTrue(executions.get() == 1);
    }

    @Test
    void returnsFalseForUnknownOrNonCommandMessages() {
        CommandHandler handler = new CommandHandler();

        assertFalse(handler.handle(TelegramUpdateFactory.textUpdate(10L, 100L, "user", "User", "en", "hello")));
        assertFalse(handler.handle(TelegramUpdateFactory.textUpdate(10L, 100L, "user", "User", "en", "/unknown")));
        assertFalse(handler.handle(new Update()));
    }

    @Test
    void swallowsCommandExceptionsAndKeepsLoopAlive() {
        CommandHandler handler = new CommandHandler();
        handler.register("/boom", update -> {
            throw new RuntimeException("boom");
        });

        assertFalse(handler.handle(TelegramUpdateFactory.textUpdate(10L, 100L, "user", "User", "en", "/boom")));
    }
}
