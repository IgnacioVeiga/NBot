package nbots.telegram.org.commands;

import nbots.telegram.org.utils.CommandUtils;
import nbots.telegram.org.utils.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private final Map<String, Command> commands = new HashMap<>();

    public void register(String command, Command handler) {
        commands.put(command.toLowerCase(), handler);
    }

    public boolean handle(Update update) {
        if (update == null || !update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }

        // Telegram can send commands like /help@MyBot in group chats; normalize before lookup.
        String text = update.getMessage().getText();
        String commandKey = CommandUtils.normalizeCommand(text);
        if (!commandKey.startsWith("/")) {
            return false;
        }

        Command handler = commands.get(commandKey);
        if (handler == null) {
            return false;
        }

        try {
            handler.execute(update);
            return true;
        } catch (Exception e) {
            // Keep the bot loop alive even if a single command handler fails.
            Logger.log("Error ejecutando comando " + commandKey, e);
        }
        return false;
    }

    public interface Command {
        void execute(Update update);
    }
}
