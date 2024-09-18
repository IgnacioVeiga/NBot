package nbots.telegram;

import nbots.telegram.commands.PicCommand;
import nbots.telegram.commands.StartCommand;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import java.util.List;

public class NBot implements LongPollingUpdateConsumer {

    private final String botToken;

    public NBot(String botToken) {
        this.botToken = botToken;
    }

    @Override
    public void consume(List<Update> updates) {
        for (Update update : updates) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                switch (messageText) {
                    case "/start" -> new StartCommand(botToken).execute(update);
                    case "/pic" -> new PicCommand(botToken).execute(update);
                    default -> System.out.println("Unknown command");
                }
            }
        }
    }
}
