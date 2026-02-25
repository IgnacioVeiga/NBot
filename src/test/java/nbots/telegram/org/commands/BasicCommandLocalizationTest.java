package nbots.telegram.org.commands;

import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.testsupport.BaseBotTest;
import nbots.telegram.org.testsupport.TelegramUpdateFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BasicCommandLocalizationTest extends BaseBotTest {
    @Test
    void startCommandRespondsInUserLanguage() {
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new StartCommand().execute(TelegramUpdateFactory.textUpdate(1L, 10L, "u", "User", "en", "/start"));
        new StartCommand().execute(TelegramUpdateFactory.textUpdate(1L, 11L, "u2", "User", "es", "/start"));

        assertTrue(messages.get(0).contains("How can I help"));
        assertTrue(messages.get(1).contains("¿En qué te ayudo"));
    }

    @Test
    void infoCommandIncludesIdentifiersAndLocalizedLabels() {
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new InfoCommand().execute(TelegramUpdateFactory.textUpdate(55L, 10L, "nasho", "Ignacio", "es", "/info"));

        assertTrue(messages.getFirst().contains("Usuario: @nasho"));
        assertTrue(messages.getFirst().contains("ID de usuario: 10"));
        assertTrue(messages.getFirst().contains("ID del chat: 55"));
    }
}
