package nbots.telegram.org.components;

public class AppEnvComponent {
    private static final String botToken = System.getenv("TELEGRAM_TOKEN");

    public static String getBotToken() {
        return botToken;
    }
}
