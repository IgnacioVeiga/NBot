package nbots.telegram.org.commands;

import nbots.telegram.org.components.AppEnvComponent;
import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.testsupport.BaseBotTest;
import nbots.telegram.org.testsupport.TelegramUpdateFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WeatherCommandTest extends BaseBotTest {
    @Test
    void showsLocalizedUsageWhenCityIsMissing() {
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new WeatherCommand().execute(TelegramUpdateFactory.textUpdate(1L, 10L, "u", "User", "en", "/weather"));
        new WeatherCommand().execute(TelegramUpdateFactory.textUpdate(1L, 11L, "u2", "User", "es", "/weather"));

        assertTrue(messages.get(0).contains("Usage: /weather <city>"));
        assertTrue(messages.get(1).contains("Uso: /weather <ciudad>"));
    }

    @Test
    void informsWhenApiKeyIsMissing() {
        AppEnvComponent.setEnvOverrideForTests("WEATHER_API_KEY", "");
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new WeatherCommand().execute(TelegramUpdateFactory.textUpdate(1L, 10L, "u", "User", "en", "/weather madrid"));

        assertTrue(messages.getFirst().contains("WEATHER_API_KEY"));
    }
}
