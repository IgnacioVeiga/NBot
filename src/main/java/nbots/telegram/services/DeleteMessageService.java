package nbots.telegram.services;

import nbots.telegram.utils.BotConfig;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class DeleteMessageService {
    private static final OkHttpTelegramClient telegramClient = new OkHttpTelegramClient(BotConfig.getBotToken());

    public static void deleteMessage(long chatId, int messageId) {
        DeleteMessage deleteMessage = DeleteMessage.builder().chatId(chatId).messageId(messageId).build();

        try {
            telegramClient.execute(deleteMessage);
            System.out.println("Message with ID " + messageId + " deleted.");
        } catch (TelegramApiException e) {
            System.out.println("Error deleting message: " + e.getMessage());
        }
    }
}
