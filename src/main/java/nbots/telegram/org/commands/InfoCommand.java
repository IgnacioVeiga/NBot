package nbots.telegram.org.commands;

import nbots.telegram.org.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class InfoCommand implements CommandHandler.Command {
    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        String user = update.getMessage().getFrom().getUserName();
        String name = update.getMessage().getFrom().getFirstName();
        String info = "Usuario: @" + user + "\nNombre: " + name + "\nChat ID: " + chatId;
        MessageService.sendMessage(chatId, info);
    }
}

