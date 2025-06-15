package nbots.telegram.org.commands;

import nbots.telegram.org.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeCommand implements CommandHandler.Command {
    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        LocalDateTime now = LocalDateTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        MessageService.sendMessage(chatId, "Hora actual: " + time);
    }
}

