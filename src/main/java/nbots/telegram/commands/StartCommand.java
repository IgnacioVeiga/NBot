package nbots.telegram.commands;

import nbots.telegram.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand {
    private final String botToken;

    public StartCommand(String botToken) {
        this.botToken = botToken;
    }

    public void execute(Update update) {
        String messageText = "Hi, welcome!";
        long chatId = update.getMessage().getChatId();
        MessageService.sendMessage(botToken, chatId, messageText);
    }
}
