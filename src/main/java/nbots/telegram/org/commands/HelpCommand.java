package nbots.telegram.org.commands;

import nbots.telegram.org.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HelpCommand implements CommandHandler.Command {
    @Override
    public void execute(Update update) {
        String helpText = """
                Comandos disponibles:
                /start - Inicia el bot
                /pic - Envía una foto
                /delete - Borra un mensaje (responde a un mensaje para borrarlo)
                /help - Muestra esta ayuda
                /echo - Repite tu mensaje
                /info - Información del usuario/chat
                /remind - Programa un recordatorio simple
                /weather - Consulta el clima actual
                /quote - Envía una cita aleatoria
                /time - Muestra la hora actual
                /history - Muestra el historial de mensajes""";
        long chatId = update.getMessage().getChatId();
        MessageService.sendMessage(chatId, helpText);
    }
}

