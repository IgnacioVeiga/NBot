package nbots.telegram.services;

import nbots.telegram.utils.BotConfig;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class PhotoService {
    private static final OkHttpTelegramClient telegramClient = new OkHttpTelegramClient(BotConfig.getBotToken());

    public static void sendPhoto(long chatId, String imageUrl, String caption) {
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
