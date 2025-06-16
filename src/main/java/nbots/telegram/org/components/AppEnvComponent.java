package nbots.telegram.org.components;

import io.github.cdimascio.dotenv.Dotenv;

public class AppEnvComponent {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    private static String getEnv(String key) {
        String value = System.getenv(key);
        if (value == null) {
            value = dotenv.get(key);
        }
        return value;
    }

    public static String getBotToken() {
        return getEnv("BOT_TOKEN");
    }

    public static String getWeatherApiKey() {
        return getEnv("WEATHER_API_KEY");
    }
}
