package nbots.telegram.org.commands;

import nbots.telegram.org.i18n.BotLanguage;
import nbots.telegram.org.i18n.I18n;
import nbots.telegram.org.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Duration;
import java.time.Instant;

public class UptimeCommand implements CommandHandler.Command {
    private final Instant startedAt = Instant.now();

    @Override
    public void execute(Update update) {
        BotLanguage language = I18n.language(update);
        Duration uptime = Duration.between(startedAt, Instant.now());
        long totalSeconds = uptime.toSeconds();
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        String prefix = I18n.t(language, "Uptime", "Tiempo activo");
        String message = String.format("%s: %02dh %02dm %02ds", prefix, hours, minutes, seconds);
        MessageService.sendMessage(update.getMessage().getChatId(), message);
    }
}
