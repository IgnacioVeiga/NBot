package nbots.telegram.org.components;

import io.github.cdimascio.dotenv.Dotenv;
import nbots.telegram.org.utils.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppEnvComponent {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();
    // Test overrides let us exercise command behavior without touching the real environment.
    private static final Map<String, String> envOverridesForTests = new ConcurrentHashMap<>();

    private static String getEnv(String key) {
        if (envOverridesForTests.containsKey(key)) {
            return normalizeValue(envOverridesForTests.get(key));
        }

        String value = System.getenv(key);
        if (value == null) {
            value = dotenv.get(key);
        }
        return normalizeValue(value);
    }

    private static String normalizeValue(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public static String requireEnv(String key) {
        String value = getEnv(key);
        if (value == null) {
            throw new IllegalStateException("Missing required environment variable: " + key);
        }
        return value;
    }

    public static String getBotToken() {
        return requireEnv("BOT_TOKEN");
    }

    public static String getWeatherApiKey() {
        return getEnv("WEATHER_API_KEY");
    }

    public static boolean isPythonCommandsEnabled() {
        String value = getEnv("PYTHON_COMMANDS_ENABLED");
        if (value == null) {
            return false;
        }
        return value.equalsIgnoreCase("true")
                || value.equalsIgnoreCase("yes")
                || value.equals("1");
    }

    public static String getPythonExecutable() {
        return getEnv("PYTHON_BIN");
    }

    public static String getPythonScriptsDir() {
        return getEnv("PYTHON_SCRIPTS_DIR");
    }

    public static Long getAdminUserId() {
        String value = getEnv("ADMIN_USER_ID");
        if (value == null) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            Logger.log("Invalid ADMIN_USER_ID value. It must be a numeric Telegram user id.");
            return null;
        }
    }

    public static void setEnvOverrideForTests(String key, String value) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("key is required");
        }
        envOverridesForTests.put(key, value);
    }

    public static void clearEnvOverridesForTests() {
        envOverridesForTests.clear();
    }
}
