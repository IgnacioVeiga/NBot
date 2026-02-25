package nbots.telegram.org.services;

import nbots.telegram.org.components.AppEnvComponent;
import nbots.telegram.org.i18n.BotLanguage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WeatherService {
    private static final int HTTP_OK = 200;

    public WeatherInfo getCurrentWeather(String city, BotLanguage language) throws IOException {
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City is required");
        }

        String apiKey = AppEnvComponent.getWeatherApiKey();
        if (apiKey == null) {
            throw new IllegalStateException("Missing WEATHER_API_KEY");
        }

        JSONObject obj = getWeatherResponse(city.trim(), apiKey, language == null ? BotLanguage.EN : language);
        String description = obj.getJSONArray("weather").getJSONObject(0).getString("description");
        double temp = obj.getJSONObject("main").getDouble("temp");
        String resolvedCity = obj.optString("name", city.trim());
        return new WeatherInfo(resolvedCity, description, temp);
    }

    private JSONObject getWeatherResponse(String city, String apiKey, BotLanguage language) throws IOException {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String apiLanguage = language == BotLanguage.ES ? "es" : "en";
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + apiKey + "&units=metric&lang=" + apiLanguage;
        HttpURLConnection conn = (HttpURLConnection) URI.create(urlString).toURL().openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5_000);
        conn.setReadTimeout(5_000);

        try {
            int statusCode = conn.getResponseCode();
            InputStream responseStream = statusCode >= 400 ? conn.getErrorStream() : conn.getInputStream();
            if (responseStream == null) {
                throw new IOException("Respuesta vacía del servicio de clima.");
            }

            String responseBody = readBody(responseStream);
            JSONObject json = new JSONObject(responseBody);
            if (statusCode != HTTP_OK || extractApiCode(json) != HTTP_OK) {
                String apiMessage = json.optString("message", "respuesta inválida");
                throw new IOException("OpenWeather API error (" + statusCode + "): " + apiMessage);
            }
            return json;
        } finally {
            conn.disconnect();
        }
    }

    private int extractApiCode(JSONObject json) {
        Object cod = json.opt("cod");
        if (cod instanceof Number number) {
            return number.intValue();
        }
        if (cod instanceof String codString) {
            try {
                return Integer.parseInt(codString);
            } catch (NumberFormatException ignored) {
                return -1;
            }
        }
        return HTTP_OK;
    }

    private String readBody(InputStream inputStream) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        }
        return content.toString();
    }

    public record WeatherInfo(String city, String description, double temperatureCelsius) {
    }
}
