package nbots.telegram.org.services;

import nbots.telegram.org.i18n.BotLanguage;
import nbots.telegram.org.utils.Logger;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class UserLanguageService {
    private static final Path DEFAULT_PREFERENCES_FILE = Path.of("data", "user_preferences.json");
    private static volatile Path preferencesFileOverrideForTests;

    private UserLanguageService() {
    }

    public static BotLanguage resolveLanguage(Update update) {
        if (update == null || !update.hasMessage()) {
            return BotLanguage.EN;
        }

        User user = update.getMessage().getFrom();
        if (user == null || user.getId() == null) {
            return BotLanguage.EN;
        }

        BotLanguage persisted = getLanguage(user.getId());
        if (persisted != null) {
            return persisted;
        }

        BotLanguage detected = BotLanguage.fromCode(user.getLanguageCode());
        return detected != null ? detected : BotLanguage.EN;
    }

    public static synchronized BotLanguage getLanguage(long userId) {
        JSONObject store = readStore();
        String value = store.optString(Long.toString(userId), "");
        return BotLanguage.fromCode(value);
    }

    public static synchronized void setLanguage(long userId, BotLanguage language) {
        if (language == null) {
            throw new IllegalArgumentException("language is required");
        }

        JSONObject store = readStore();
        store.put(Long.toString(userId), language.code());
        writeStore(store);
    }

    private static JSONObject readStore() {
        Path preferencesFile = preferencesFile();
        if (!Files.exists(preferencesFile)) {
            return new JSONObject();
        }

        try {
            String content = Files.readString(preferencesFile, StandardCharsets.UTF_8).trim();
            if (content.isEmpty()) {
                return new JSONObject();
            }
            return new JSONObject(content);
        } catch (Exception e) {
            Logger.log("Error reading user language preferences", e);
            return new JSONObject();
        }
    }

    private static void writeStore(JSONObject store) {
        Path preferencesFile = preferencesFile();
        try {
            Files.createDirectories(preferencesFile.getParent());
            Files.writeString(
                    preferencesFile,
                    store.toString(2),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );
        } catch (IOException e) {
            Logger.log("Error writing user language preferences", e);
        }
    }

    private static Path preferencesFile() {
        Path override = preferencesFileOverrideForTests;
        return override == null ? DEFAULT_PREFERENCES_FILE : override;
    }

    public static void setPreferencesFileOverrideForTests(Path preferencesFile) {
        preferencesFileOverrideForTests = preferencesFile;
    }

    public static void clearPreferencesFileOverrideForTests() {
        preferencesFileOverrideForTests = null;
    }
}
