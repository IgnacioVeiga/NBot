package nbots.telegram.org.services;

import nbots.telegram.org.components.AppEnvComponent;
import nbots.telegram.org.utils.Logger;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class DeleteMessageService {
    private static final OkHttpTelegramClient telegramClient = new OkHttpTelegramClient(AppEnvComponent.getBotToken());

    public static void deleteMessage(long chatId, int messageId) {
        DeleteMessage deleteMessage = DeleteMessage.builder().chatId(chatId).messageId(messageId).build();

        try {
            telegramClient.execute(deleteMessage);
            Logger.log("Message with ID " + messageId + " deleted.");
        } catch (TelegramApiException e) {
            Logger.log("Error deleting message: " + e.getMessage());
        }
    }
}
