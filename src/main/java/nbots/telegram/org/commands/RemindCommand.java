package nbots.telegram.org.commands;

import nbots.telegram.org.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.Timer;
import java.util.TimerTask;

public class RemindCommand implements CommandHandler.Command {
    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        String[] parts = text.split(" ", 3);
        if (parts.length < 3) {
            MessageService.sendMessage(chatId, "Uso: /remind <segundos> <mensaje>");
            return;
        }
        try {
            int seconds = Integer.parseInt(parts[1]);
            String reminder = parts[2];
            MessageService.sendMessage(chatId, "Te recordaré: '" + reminder + "' en " + seconds + " segundos.");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    MessageService.sendMessage(chatId, "⏰ Recordatorio: " + reminder);
                }
            }, seconds * 1000L);
        } catch (NumberFormatException e) {
            MessageService.sendMessage(chatId, "El tiempo debe ser un número de segundos.");
        }
    }
}

