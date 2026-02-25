package nbots.telegram.org.commands;

import nbots.telegram.org.i18n.BotLanguage;
import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.services.UserLanguageService;
import nbots.telegram.org.testsupport.BaseBotTest;
import nbots.telegram.org.testsupport.TelegramUpdateFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HelpCommandTest extends BaseBotTest {
    @TempDir
    Path tempDir;

    @Test
    void returnsEnglishHelpByDefault() {
        UserLanguageService.setPreferencesFileOverrideForTests(tempDir.resolve("prefs.json"));
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new HelpCommand().execute(TelegramUpdateFactory.textUpdate(1L, 10L, "u", "User", "en", "/help"));

        assertTrue(messages.getFirst().contains("Available commands:"));
        assertTrue(messages.getFirst().contains("/lang <en|es>"));
    }

    @Test
    void returnsSpanishHelpWhenLanguagePreferenceIsSpanish() {
        UserLanguageService.setPreferencesFileOverrideForTests(tempDir.resolve("prefs.json"));
        UserLanguageService.setLanguage(10L, BotLanguage.ES);

        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new HelpCommand().execute(TelegramUpdateFactory.textUpdate(1L, 10L, "u", "User", "en", "/help"));

        assertTrue(messages.getFirst().contains("Comandos disponibles:"));
        assertTrue(messages.getFirst().contains("Guarda tu idioma preferido"));
    }
}
