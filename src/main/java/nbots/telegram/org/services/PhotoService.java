package nbots.telegram.org.services;

import nbots.telegram.org.components.AppEnvComponent;
import nbots.telegram.org.utils.Logger;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PhotoService {
    @FunctionalInterface
    public interface PhotoSender {
        void send(long chatId, String imageUrl, String caption);
    }

    private static volatile PhotoSender senderOverrideForTests;
    private static volatile OkHttpTelegramClient telegramClient;

    private PhotoService() {
    }

    private static OkHttpTelegramClient getTelegramClient() {
        OkHttpTelegramClient client = telegramClient;
        if (client == null) {
            synchronized (PhotoService.class) {
                client = telegramClient;
                if (client == null) {
                    client = new OkHttpTelegramClient(AppEnvComponent.getBotToken());
                    telegramClient = client;
                }
            }
        }
        return client;
    }

    public static void sendPhoto(long chatId, String imageUrl, String caption) {
        PhotoSender override = senderOverrideForTests;
        if (override != null) {
            override.send(chatId, imageUrl, caption);
            return;
        }

        SendPhoto photo = SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(imageUrl))
                .caption(caption)
                .build();
        try {
            getTelegramClient().execute(photo);
        } catch (TelegramApiException e) {
            Logger.log("Exception sending message: " + e.getMessage());
        }
    }

    public static void setSenderOverrideForTests(PhotoSender sender) {
        senderOverrideForTests = sender;
    }

    public static void clearSenderOverrideForTests() {
        senderOverrideForTests = null;
    }
}
