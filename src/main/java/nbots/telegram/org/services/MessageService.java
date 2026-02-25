package nbots.telegram.org.services;

import nbots.telegram.org.components.AppEnvComponent;
import nbots.telegram.org.utils.Logger;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageService {
    @FunctionalInterface
    public interface MessageSender {
        void send(long chatId, String text);
    }

    private static volatile MessageSender senderOverrideForTests;
    private static volatile OkHttpTelegramClient telegramClient;

    private MessageService() {
    }

    private static OkHttpTelegramClient getTelegramClient() {
        OkHttpTelegramClient client = telegramClient;
        if (client == null) {
            synchronized (MessageService.class) {
                client = telegramClient;
                if (client == null) {
                    // Lazy init avoids requiring BOT_TOKEN during unit tests that stub the sender.
                    client = new OkHttpTelegramClient(AppEnvComponent.getBotToken());
                    telegramClient = client;
                }
            }
        }
        return client;
    }

    public static void sendMessage(long chatId, String text) {
        MessageSender override = senderOverrideForTests;
        if (override != null) {
            override.send(chatId, text);
            return;
        }

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            getTelegramClient().execute(message);
        } catch (TelegramApiException e) {
            Logger.log("Exception sending message: " + e.getMessage());
        }
    }

    public static void setSenderOverrideForTests(MessageSender sender) {
        senderOverrideForTests = sender;
    }

    public static void clearSenderOverrideForTests() {
        senderOverrideForTests = null;
    }
}
