package nbots.telegram.org.commands;

import nbots.telegram.org.components.AppEnvComponent;
import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import org.json.JSONObject;

public class WeatherCommand implements CommandHandler.Command {
    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        String[] parts = text.split(" ", 2);
        if (parts.length < 2) {
            MessageService.sendMessage(chatId, "Uso: /weather <ciudad>");
            return;
        }
        String city = parts[1];
        String apiKey = AppEnvComponent.getWeatherApiKey();
        try {
            JSONObject obj = getJsonObject(city, apiKey);
            String weather = obj.getJSONArray("weather").getJSONObject(0).getString("description");
            double temp = obj.getJSONObject("main").getDouble("temp");
            MessageService.sendMessage(chatId, "Clima en " + city + ": " + weather + ", " + temp + "°C");
        } catch (Exception e) {
            MessageService.sendMessage(chatId, "No se pudo obtener el clima. ¿Ciudad válida?");
        }
    }

    @NotNull
    private static JSONObject getJsonObject(String city, String apiKey) throws IOException {
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric&lang=es";
        URL url = URI.create(urlString).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return new JSONObject(content.toString());
    }
}

