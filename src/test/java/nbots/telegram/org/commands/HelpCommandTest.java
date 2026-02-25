package nbots.telegram.org.commands;

import nbots.telegram.org.components.AppEnvComponent;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
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
        assertFalse(messages.getFirst().contains("/admin -"));
        assertFalse(messages.getFirst().contains("/pyhello"));
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

    @Test
    void showsAdminAndPythonCommandsOnlyForAdminWhenPythonEnabled() {
        UserLanguageService.setPreferencesFileOverrideForTests(tempDir.resolve("prefs.json"));
        AppEnvComponent.setEnvOverrideForTests("ADMIN_USER_ID", "10");
        AppEnvComponent.setEnvOverrideForTests("PYTHON_COMMANDS_ENABLED", "true");

        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new HelpCommand().execute(TelegramUpdateFactory.textUpdate(1L, 10L, "admin", "Admin", "en", "/help"));

        assertTrue(messages.getFirst().contains("/admin -"));
        assertTrue(messages.getFirst().contains("/pyhello"));
        assertTrue(messages.getFirst().contains("/pyrun"));
    }

    @Test
    void hidesPythonCommandsForAdminWhenFeatureIsDisabled() {
        UserLanguageService.setPreferencesFileOverrideForTests(tempDir.resolve("prefs.json"));
        AppEnvComponent.setEnvOverrideForTests("ADMIN_USER_ID", "10");
        AppEnvComponent.setEnvOverrideForTests("PYTHON_COMMANDS_ENABLED", "false");

        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new HelpCommand().execute(TelegramUpdateFactory.textUpdate(1L, 10L, "admin", "Admin", "en", "/help"));

        assertTrue(messages.getFirst().contains("/admin -"));
        assertFalse(messages.getFirst().contains("/pyhello"));
        assertFalse(messages.getFirst().contains("/pyrun"));
    }
}
