package nbots.telegram.commands;

import nbots.telegram.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand {

    public void execute(Update update) {
        String messageText = "Hi, welcome!";
        long chatId = update.getMessage().getChatId();
        MessageService.sendMessage(chatId, messageText);
    }
}
