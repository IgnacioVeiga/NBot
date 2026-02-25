package nbots.telegram.org.commands;

import nbots.telegram.org.i18n.BotLanguage;
import nbots.telegram.org.i18n.I18n;
import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.utils.CommandUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

public class InfoCommand implements CommandHandler.Command {
    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        BotLanguage language = I18n.language(update);
        String user = CommandUtils.userDisplayName(update.getMessage().getFrom());
        Long userId = update.getMessage().getFrom() != null ? update.getMessage().getFrom().getId() : null;
        String na = I18n.t(language, "N/A", "N/D");
        String name = update.getMessage().getFrom() != null ? update.getMessage().getFrom().getFirstName() : na;
        String info = I18n.t(language, "User: ", "Usuario: ") + user
                + "\n" + I18n.t(language, "User ID: ", "ID de usuario: ") + (userId == null ? na : userId)
                + "\n" + I18n.t(language, "Name: ", "Nombre: ") + (name == null ? na : name)
                + "\n" + I18n.t(language, "Chat ID: ", "ID del chat: ") + chatId;
        MessageService.sendMessage(chatId, info);
    }
}
