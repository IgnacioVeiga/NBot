package nbots.telegram;

import nbots.telegram.commands.PicCommand;
import nbots.telegram.commands.StartCommand;
import nbots.telegram.commands.DeleteCommand;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import java.util.List;

public class NBot implements LongPollingUpdateConsumer {

    @Override
    public void consume(List<Update> updates) {
        for (Update update : updates) {
            // Commands only for messages with text here
            if (update.hasMessage() && update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();

                switch (messageText.split(" ")[0]) {
                    case "/start" -> new StartCommand().execute(update);
                    case "/pic" -> new PicCommand().execute(update);
                    case "/delete" -> new DeleteCommand().execute(update);
                    default -> System.out.println("Unknown command");
                }
            }
        }
    }
}
