package nbots.telegram.org.commands;

import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.utils.MessageHistory;
import org.telegram.telegrambots.meta.api.objects.Update;

public class EchoCommand implements CommandHandler.Command {
    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        String echo = text.replaceFirst("/echo", "").trim();
        if (echo.isEmpty()) {
            echo = "¿Qué quieres que repita?";
        }
        MessageService.sendMessage(chatId, echo);
        MessageHistory.logMessage(update.getMessage().getFrom().getUserName(), echo);
    }
}

