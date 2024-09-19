package nbots.telegram.utils;

public class BotConfig {
    private static final String botToken = System.getenv("TELEGRAM_TOKEN");

    public static String getBotToken() {
        return botToken;
    }
}
