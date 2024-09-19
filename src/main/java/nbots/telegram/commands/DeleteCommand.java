package nbots.telegram.commands;

import nbots.telegram.services.DeleteMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Random;

public class DeleteCommand {

    public void execute(Update update) {
        String command = update.getMessage().getText();

        if (command.equals("/delete random")) {
            long chatId = update.getMessage().getChatId();

            // TODO: get an existing message id
            Random random = new Random();
            int randomMessageId = random.nextInt(1000);

            DeleteMessageService.deleteMessage(chatId, randomMessageId);
        } else if (command.startsWith("/delete")) {
            if (update.getMessage().getReplyToMessage() != null) {
                int messageId = update.getMessage().getReplyToMessage().getMessageId();
                long chatId = update.getMessage().getChatId();
                DeleteMessageService.deleteMessage(chatId, messageId);
            }
        }
    }
}
