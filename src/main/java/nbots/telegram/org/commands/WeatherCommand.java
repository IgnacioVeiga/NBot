package nbots.telegram.org.commands;

import nbots.telegram.org.i18n.BotLanguage;
import nbots.telegram.org.i18n.I18n;
import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.services.WeatherService;
import nbots.telegram.org.utils.CommandUtils;
import nbots.telegram.org.utils.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

public class WeatherCommand implements CommandHandler.Command {
    private static final WeatherService WEATHER_SERVICE = new WeatherService();

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        BotLanguage language = I18n.language(update);
        String city = CommandUtils.commandArgs(update.getMessage().getText());
        if (city.isBlank()) {
            MessageService.sendMessage(chatId, I18n.t(language, "Usage: /weather <city>", "Uso: /weather <ciudad>"));
            return;
        }

        try {
            WeatherService.WeatherInfo weather = WEATHER_SERVICE.getCurrentWeather(city, language);
            MessageService.sendMessage(
                    chatId,
                    I18n.t(language, "Weather in ", "Clima en ")
                            + weather.city() + ": " + weather.description() + ", " + weather.temperatureCelsius() + "°C"
            );
        } catch (IllegalStateException e) {
            MessageService.sendMessage(chatId, I18n.t(language, "WEATHER_API_KEY is not configured for /weather.", "Falta configurar WEATHER_API_KEY para usar /weather."));
        } catch (IllegalArgumentException e) {
            MessageService.sendMessage(chatId, I18n.t(language, "Usage: /weather <city>", "Uso: /weather <ciudad>"));
        } catch (IOException e) {
            Logger.log("Error consultando clima para " + city, e);
            MessageService.sendMessage(
                    chatId,
                    I18n.t(
                            language,
                            "Couldn't get weather data. Check the city name or service configuration.",
                            "No se pudo obtener el clima. Revisa la ciudad o la configuración del servicio."
                    )
            );
        } catch (Exception e) {
            Logger.log("Error inesperado en /weather", e);
            MessageService.sendMessage(
                    chatId,
                    I18n.t(
                            language,
                            "An error occurred while processing the weather response.",
                            "Ocurrió un error procesando la respuesta del clima."
                    )
            );
        }
    }
}
