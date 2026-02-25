package nbots.telegram.org.commands;

import nbots.telegram.org.i18n.BotLanguage;
import nbots.telegram.org.i18n.I18n;
import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.utils.CommandUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

public class EchoCommand implements CommandHandler.Command {
    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        BotLanguage language = I18n.language(update);
        String echo = CommandUtils.commandArgs(update.getMessage().getText());
        if (echo.isEmpty()) {
            echo = I18n.t(language, "What do you want me to repeat?", "¿Qué quieres que repita?");
        }
        MessageService.sendMessage(chatId, echo);
    }
}
