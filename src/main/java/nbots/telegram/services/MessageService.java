package nbots.telegram.services;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageService {

    public static void sendMessage(String botToken, long chatId, String text) {
        OkHttpTelegramClient telegramClient = new OkHttpTelegramClient(botToken);
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Exception sending message: " + e.getMessage());
        }
    }
}
