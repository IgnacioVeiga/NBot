package nbots.telegram.org.commands;

import nbots.telegram.org.components.AppEnvComponent;
import nbots.telegram.org.i18n.BotLanguage;
import nbots.telegram.org.i18n.I18n;
import nbots.telegram.org.services.AdminAuthService;
import nbots.telegram.org.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HelpCommand implements CommandHandler.Command {
    @Override
    public void execute(Update update) {
        BotLanguage language = I18n.language(update);
        boolean isAdmin = AdminAuthService.isAdmin(update);
        boolean pythonEnabled = AppEnvComponent.isPythonCommandsEnabled();

        StringBuilder helpText = new StringBuilder(I18n.t(language, """
                Available commands:
                /start - Start the bot
                /pic - Send a photo
                /delete - Delete a message by reply or /delete <messageId>
                /help - Show this help
                /lang <en|es> - Save your preferred language
                /echo <text> - Repeat your message
                /info - User/chat information
                /remind <seconds> <message> - Create a simple reminder
                /weather <city> - Check current weather
                /quote - Send a random quote
                /time - Show current time
                /history - Show message history
                /ping - Check if the bot responds
                /uptime - Show how long the bot has been running""", """
                Comandos disponibles:
                /start - Inicia el bot
                /pic - Envía una foto
                /delete - Borra un mensaje respondiendo a ese mensaje o usando /delete <messageId>
                /help - Muestra esta ayuda
                /lang <en|es> - Guarda tu idioma preferido
                /echo <texto> - Repite tu mensaje
                /info - Información del usuario/chat
                /remind <segundos> <mensaje> - Programa un recordatorio simple
                /weather <ciudad> - Consulta el clima actual
                /quote - Envía una cita aleatoria
                /time - Muestra la hora actual
                /history - Muestra el historial de mensajes
                /ping - Verifica si el bot responde
                /uptime - Muestra cuánto tiempo lleva iniciado"""));

        if (isAdmin) {
            helpText.append('\n').append(I18n.t(
                    language,
                    "/admin - Returns true if user matches ADMIN_USER_ID",
                    "/admin - Devuelve true si el usuario coincide con ADMIN_USER_ID"
            ));
        }

        if (isAdmin && pythonEnabled) {
            helpText.append('\n').append(I18n.t(
                    language,
                    "/pyhello [args...] - Run test Python script (admin only, experimental)",
                    "/pyhello [args...] - Ejecuta script Python de prueba (solo admin, experimental)"
            ));
            helpText.append('\n').append(I18n.t(
                    language,
                    "/pyrun <script.py> [args...] - Run a Python script (admin only, experimental, disabled by default)",
                    "/pyrun <script.py> [args...] - Ejecuta un script Python (solo admin, experimental, deshabilitado por defecto)"
            ));
        }

        long chatId = update.getMessage().getChatId();
        MessageService.sendMessage(chatId, helpText.toString());
    }
}
