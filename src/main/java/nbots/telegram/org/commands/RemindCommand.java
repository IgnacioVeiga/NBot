package nbots.telegram.org.commands;

import nbots.telegram.org.i18n.BotLanguage;
import nbots.telegram.org.i18n.I18n;
import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.services.ReminderService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RemindCommand implements CommandHandler.Command {
    private static final int MAX_SECONDS = 86_400;

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        BotLanguage language = I18n.language(update);
        String text = update.getMessage().getText();
        String[] parts = text.trim().split("\\s+", 3);
        if (parts.length < 3) {
            MessageService.sendMessage(chatId, I18n.t(language, "Usage: /remind <seconds> <message>", "Uso: /remind <segundos> <mensaje>"));
            return;
        }
        try {
            int seconds = Integer.parseInt(parts[1]);
            if (seconds <= 0) {
                MessageService.sendMessage(chatId, I18n.t(language, "Time must be greater than 0 seconds.", "El tiempo debe ser mayor a 0 segundos."));
                return;
            }
            if (seconds > MAX_SECONDS) {
                MessageService.sendMessage(
                        chatId,
                        I18n.t(
                                language,
                                "For now the maximum allowed is " + MAX_SECONDS + " seconds (24h).",
                                "Por ahora el máximo permitido es " + MAX_SECONDS + " segundos (24h)."
                        )
                );
                return;
            }

            String reminder = parts[2];
            MessageService.sendMessage(
                    chatId,
                    I18n.t(
                            language,
                            "I'll remind you: '" + reminder + "' in " + seconds + " seconds.",
                            "Te recordaré: '" + reminder + "' en " + seconds + " segundos."
                    )
            );
            ReminderService.scheduleReminder(chatId, seconds, reminder, language);
        } catch (NumberFormatException e) {
            MessageService.sendMessage(chatId, I18n.t(language, "Time must be a number of seconds.", "El tiempo debe ser un número de segundos."));
        } catch (IllegalArgumentException e) {
            MessageService.sendMessage(
                    chatId,
                    I18n.t(language, "Couldn't create reminder: ", "No se pudo crear el recordatorio: ") + e.getMessage()
            );
        }
    }
}
