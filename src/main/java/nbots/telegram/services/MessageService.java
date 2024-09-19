package nbots.telegram.services;

import nbots.telegram.utils.BotConfig;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageService {
    private static final OkHttpTelegramClient telegramClient = new OkHttpTelegramClient(BotConfig.getBotToken());

    public static void sendMessage(long chatId, String text) {
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
