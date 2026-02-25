package nbots.telegram.org.services;

import nbots.telegram.org.i18n.BotLanguage;
import nbots.telegram.org.testsupport.BaseBotTest;
import nbots.telegram.org.testsupport.TelegramUpdateFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserLanguageServiceTest extends BaseBotTest {
    @TempDir
    Path tempDir;

    @Test
    void persistsLanguageByTelegramUserId() {
        UserLanguageService.setPreferencesFileOverrideForTests(tempDir.resolve("prefs.json"));

        UserLanguageService.setLanguage(15L, BotLanguage.ES);

        assertEquals(BotLanguage.ES, UserLanguageService.getLanguage(15L));
    }

    @Test
    void resolveLanguageUsesStoredPreferenceBeforeTelegramLanguageCode() {
        UserLanguageService.setPreferencesFileOverrideForTests(tempDir.resolve("prefs.json"));
        UserLanguageService.setLanguage(20L, BotLanguage.EN);

        assertEquals(
                BotLanguage.EN,
                UserLanguageService.resolveLanguage(TelegramUpdateFactory.textUpdate(1L, 20L, "u", "User", "es", "/start"))
        );
    }

    @Test
    void resolveLanguageFallsBackToTelegramLanguageCodeThenEnglish() {
        UserLanguageService.setPreferencesFileOverrideForTests(tempDir.resolve("prefs.json"));

        assertEquals(
                BotLanguage.ES,
                UserLanguageService.resolveLanguage(TelegramUpdateFactory.textUpdate(1L, 21L, "u", "User", "es-AR", "/start"))
        );
        assertEquals(
                BotLanguage.EN,
                UserLanguageService.resolveLanguage(TelegramUpdateFactory.textUpdate(1L, 22L, "u", "User", "pt-BR", "/start"))
        );
    }
}
