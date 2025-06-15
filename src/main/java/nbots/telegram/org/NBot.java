package nbots.telegram.org;

import nbots.telegram.org.commands.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import java.util.List;

public class NBot implements LongPollingUpdateConsumer {
    private final CommandHandler commandHandler;

    public NBot() {
        commandHandler = new CommandHandler();
        commandHandler.register("/start", new StartCommand());
        commandHandler.register("/pic", new PicCommand());
        commandHandler.register("/delete", new DeleteCommand());
        commandHandler.register("/help", new HelpCommand());
        commandHandler.register("/echo", new EchoCommand());
        commandHandler.register("/info", new InfoCommand());
        commandHandler.register("/remind", new RemindCommand());
        commandHandler.register("/weather", new WeatherCommand());
        commandHandler.register("/quote", new QuoteCommand());
        commandHandler.register("/time", new TimeCommand());
        commandHandler.register("/history", new HistoryCommand());
    }

    @Override
    public void consume(List<Update> updates) {
        for (Update update : updates) {
            boolean handled = commandHandler.handle(update);
            if (!handled) {
                // Si no es comando conocido, no hacer nada o loguear
            }
        }
    }
}
