package nbots.telegram.org.commands;

import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.utils.MessageHistory;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.List;

public class HistoryCommand implements CommandHandler.Command {
    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        List<String> history = MessageHistory.getHistory();
        if (history.isEmpty()) {
            MessageService.sendMessage(chatId, "No hay historial de mensajes.");
        } else {
            StringBuilder sb = new StringBuilder("Historial de mensajes:\n");
            for (int i = Math.max(0, history.size() - 10); i < history.size(); i++) {
                sb.append(history.get(i)).append("\n");
            }
            if (history.size() > 10) sb.insert(0, "(Mostrando los últimos 10 mensajes)\n");
            MessageService.sendMessage(chatId, sb.toString());
        }
    }
}

