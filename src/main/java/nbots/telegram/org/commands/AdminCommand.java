package nbots.telegram.org.commands;

import nbots.telegram.org.services.AdminAuthService;
import nbots.telegram.org.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AdminCommand implements CommandHandler.Command {
    @Override
    public void execute(Update update) {
        if (AdminAuthService.isAdmin(update)) {
            MessageService.sendMessage(update.getMessage().getChatId(), "true");
        }
    }
}
