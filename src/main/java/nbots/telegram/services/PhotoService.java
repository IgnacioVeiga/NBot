package nbots.telegram.services;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PhotoService {

    public static void sendPhoto(String botToken, long chatId, String imageUrl, String caption) {
        OkHttpTelegramClient telegramClient = new OkHttpTelegramClient(botToken);
        SendPhoto photo = SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(imageUrl))
                .caption(caption)
                .build();
        try {
            telegramClient.execute(photo);
        } catch (TelegramApiException e) {
            System.out.println("Exception sending photo: " + e.getMessage());
        }
    }
}
