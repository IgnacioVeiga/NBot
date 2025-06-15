package nbots.telegram.org.components;

public class AppEnvComponent {
    public static String getBotToken() { return EnvLoader.get("BOT_TOKEN"); }
    public static String getWeatherApiKey() {
        return EnvLoader.get("WEATHER_API_KEY");
    }
}
