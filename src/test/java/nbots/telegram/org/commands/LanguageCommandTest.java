package nbots.telegram.org.commands;

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

class LanguageCommandTest extends BaseBotTest {
    @TempDir
    Path tempDir;

    @Test
    void showsCurrentLanguageAndUsageWhenNoArgs() {
        UserLanguageService.setPreferencesFileOverrideForTests(tempDir.resolve("prefs.json"));
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new LanguageCommand().execute(TelegramUpdateFactory.textUpdate(1L, 11L, "u", "User", "en", "/lang"));

        assertTrue(messages.getFirst().contains("Current language: EN"));
        assertTrue(messages.getFirst().contains("/lang <en|es>"));
    }

    @Test
    void savesLanguageAndUsesItForConfirmation() {
        UserLanguageService.setPreferencesFileOverrideForTests(tempDir.resolve("prefs.json"));
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new LanguageCommand().execute(TelegramUpdateFactory.textUpdate(1L, 12L, "u", "User", "en", "/lang es"));
        messages.clear();

        new LanguageCommand().execute(TelegramUpdateFactory.textUpdate(1L, 12L, "u", "User", "en", "/lang"));

        assertTrue(messages.getFirst().contains("Idioma actual: ES"));
    }

    @Test
    void rejectsInvalidLanguage() {
        UserLanguageService.setPreferencesFileOverrideForTests(tempDir.resolve("prefs.json"));
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new LanguageCommand().execute(TelegramUpdateFactory.textUpdate(1L, 13L, "u", "User", "es", "/lang pt"));

        assertTrue(messages.getFirst().contains("Idioma inválido"));
    }
}
