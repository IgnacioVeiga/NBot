package nbots.telegram.org.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private final Map<String, Command> commands = new HashMap<>();

    public void register(String command, Command handler) {
        commands.put(command, handler);
    }

    public boolean handle(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            String cmd = text.split(" ")[0];
            Command handler = commands.get(cmd);
            if (handler != null) {
                handler.execute(update);
                return true;
            }
        }
        return false;
    }

    public interface Command {
        void execute(Update update);
    }
}

