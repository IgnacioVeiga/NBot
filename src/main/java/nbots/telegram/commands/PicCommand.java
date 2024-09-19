package nbots.telegram.commands;

import nbots.telegram.services.PhotoService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PicCommand {

    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        String imageUrl = "https://png.pngtree.com/background/20230519/original/pngtree-this-is-a-picture-of-a-tiger-cub-that-looks-straight-picture-image_2660243.jpg";
        String caption = "Here is a picture of a tiger!";
        PhotoService.sendPhoto(chatId, imageUrl, caption);
    }
}
