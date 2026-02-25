package nbots.telegram.org.commands;

import nbots.telegram.org.i18n.BotLanguage;
import nbots.telegram.org.i18n.I18n;
import nbots.telegram.org.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand implements CommandHandler.Command {
    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        BotLanguage language = I18n.language(update);
        MessageService.sendMessage(chatId, I18n.t(language, "Hi! How can I help you?", "¡Hola! ¿En qué te ayudo?"));
    }
}
