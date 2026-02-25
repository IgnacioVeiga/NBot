package nbots.telegram.org.commands;

import nbots.telegram.org.services.AdminAuthService;
import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.services.PythonScriptService;
import nbots.telegram.org.utils.CommandUtils;
import nbots.telegram.org.utils.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RunPythonCommand implements CommandHandler.Command {
    private static final PythonScriptService PYTHON_SCRIPT_SERVICE = new PythonScriptService();
    private static final int MAX_TELEGRAM_TEXT = 3500;

    @Override
    public void execute(Update update) {
        if (!AdminAuthService.isAdmin(update)) {
            return;
        }

        long chatId = update.getMessage().getChatId();
        String argsText = CommandUtils.commandArgs(update.getMessage().getText());
        if (argsText.isBlank()) {
            MessageService.sendMessage(chatId, "Usage: /pyrun <script.py> [args...] (experimental, disabled by default)");
            return;
        }

        String[] tokens = argsText.split("\\s+");
        String scriptName = tokens[0];
        List<String> scriptArgs = tokens.length > 1 ? Arrays.asList(tokens).subList(1, tokens.length) : List.of();

        try {
            PythonScriptService.ScriptExecutionResult result = PYTHON_SCRIPT_SERVICE.execute(scriptName, scriptArgs);
            String output = result.output();
            if (output == null || output.isBlank()) {
                output = "(no output)";
            }
            if (output.length() > MAX_TELEGRAM_TEXT) {
                output = output.substring(0, MAX_TELEGRAM_TEXT) + "\n... (output truncated)";
            }
            MessageService.sendMessage(chatId, "Python exit code: " + result.exitCode() + "\n" + output);
        } catch (IllegalStateException e) {
            MessageService.sendMessage(chatId, "Command disabled. Set PYTHON_COMMANDS_ENABLED=true to use /pyrun.");
        } catch (IllegalArgumentException e) {
            MessageService.sendMessage(chatId, e.getMessage());
        } catch (IOException e) {
            Logger.log("Error ejecutando script Python", e);
            MessageService.sendMessage(chatId, "Error running Python script: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            MessageService.sendMessage(chatId, "Python script execution was interrupted.");
        }
    }
}
