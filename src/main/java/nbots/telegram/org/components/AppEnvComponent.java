package nbots.telegram.org.components;

public class AppEnvComponent {
    public static String getBotToken() { return System.getenv("BOT_TOKEN"); }
    public static String getWeatherApiKey() { return System.getenv("WEATHER_API_KEY"); }
}
