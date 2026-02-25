package nbots.telegram.org.commands;

import nbots.telegram.org.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PingCommand implements CommandHandler.Command {
    @Override
    public void execute(Update update) {
        MessageService.sendMessage(update.getMessage().getChatId(), "pong");
    }
}
