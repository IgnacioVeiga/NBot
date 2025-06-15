package nbots.telegram.org.commands;

import nbots.telegram.org.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand implements CommandHandler.Command {
    @Override
    public void execute(Update update) {
        String messageText = "¡Hola! ¿En que te ayudo?";
        long chatId = update.getMessage().getChatId();
        MessageService.sendMessage(chatId, messageText);
    }
}
