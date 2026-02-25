package nbots.telegram.org.commands;

import nbots.telegram.org.i18n.BotLanguage;
import nbots.telegram.org.i18n.I18n;
import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.utils.MessageHistory;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.List;

public class HistoryCommand implements CommandHandler.Command {
    private static final int HISTORY_LIMIT = 10;
    private static final int TELEGRAM_SAFE_MESSAGE_SIZE = 3500;

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        BotLanguage language = I18n.language(update);
        List<String> history = MessageHistory.getHistory(chatId);
        if (history.isEmpty()) {
            MessageService.sendMessage(chatId, I18n.t(language, "No message history yet.", "No hay historial de mensajes."));
        } else {
            StringBuilder sb = new StringBuilder(I18n.t(language, "Message history:\n", "Historial de mensajes:\n"));
            for (int i = Math.max(0, history.size() - HISTORY_LIMIT); i < history.size(); i++) {
                String line = history.get(i);
                if (sb.length() + line.length() + 1 > TELEGRAM_SAFE_MESSAGE_SIZE) {
                    sb.append(I18n.t(language, "... (history truncated)", "... (historial truncado)"));
                    break;
                }
                sb.append(line).append("\n");
            }
            if (history.size() > HISTORY_LIMIT) {
                sb.insert(0, I18n.t(
                        language,
                        "(Showing the last " + HISTORY_LIMIT + " messages)\n",
                        "(Mostrando los últimos " + HISTORY_LIMIT + " mensajes)\n"
                ));
            }
            MessageService.sendMessage(chatId, sb.toString());
        }
    }
}
