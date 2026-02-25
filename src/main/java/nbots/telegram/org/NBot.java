package nbots.telegram.org;

import nbots.telegram.org.commands.*;
import nbots.telegram.org.utils.CommandUtils;
import nbots.telegram.org.utils.MessageHistory;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import java.util.List;

public class NBot implements LongPollingUpdateConsumer {
    private final CommandHandler commandHandler;

    public NBot() {
        // Centralized registration keeps commands discoverable for new contributors.
        commandHandler = new CommandHandler();
        commandHandler.register("/start", new StartCommand());
        commandHandler.register("/pic", new PicCommand());
        commandHandler.register("/delete", new DeleteCommand());
        commandHandler.register("/help", new HelpCommand());
        commandHandler.register("/lang", new LanguageCommand());
        commandHandler.register("/language", new LanguageCommand());
        commandHandler.register("/admin", new AdminCommand());
        commandHandler.register("/echo", new EchoCommand());
        commandHandler.register("/info", new InfoCommand());
        commandHandler.register("/remind", new RemindCommand());
        commandHandler.register("/weather", new WeatherCommand());
        commandHandler.register("/quote", new QuoteCommand());
        commandHandler.register("/time", new TimeCommand());
        commandHandler.register("/history", new HistoryCommand());
        commandHandler.register("/ping", new PingCommand());
        commandHandler.register("/uptime", new UptimeCommand());
        commandHandler.register("/pyhello", new PyHelloCommand());
        commandHandler.register("/pyrun", new RunPythonCommand());
    }

    @Override
    public void consume(List<Update> updates) {
        if (updates == null || updates.isEmpty()) {
            return;
        }

        for (Update update : updates) {
            if (update != null && update.hasMessage() && update.getMessage().hasText()) {
                // History is stored per chat so experiments in one chat do not pollute another.
                MessageHistory.logMessage(
                        update.getMessage().getChatId(),
                        CommandUtils.userDisplayName(update.getMessage().getFrom()),
                        update.getMessage().getText()
                );
            }
            boolean handled = commandHandler.handle(update);
            if (!handled) {
                // Si no es comando conocido, no hacer nada o loguear
            }
        }
    }
}
