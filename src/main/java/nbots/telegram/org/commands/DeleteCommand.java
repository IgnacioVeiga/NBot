package nbots.telegram.org.commands;

import nbots.telegram.org.i18n.BotLanguage;
import nbots.telegram.org.i18n.I18n;
import nbots.telegram.org.services.DeleteMessageService;
import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.utils.CommandUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DeleteCommand implements CommandHandler.Command {
    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        BotLanguage language = I18n.language(update);
        Integer targetMessageId = null;

        if (update.getMessage().getReplyToMessage() != null) {
            targetMessageId = update.getMessage().getReplyToMessage().getMessageId();
        } else {
            String args = CommandUtils.commandArgs(update.getMessage().getText());
            if (!args.isEmpty()) {
                try {
                    targetMessageId = Integer.parseInt(args);
                } catch (NumberFormatException e) {
                    MessageService.sendMessage(
                            chatId,
                            I18n.t(
                                    language,
                                    "Usage: reply to a message with /delete or use /delete <messageId>.",
                                    "Uso: responde a un mensaje con /delete o usa /delete <messageId>."
                            )
                    );
                    return;
                }
            }
        }

        if (targetMessageId == null) {
            MessageService.sendMessage(
                    chatId,
                    I18n.t(
                            language,
                            "Usage: reply to a message with /delete or use /delete <messageId>.",
                            "Uso: responde a un mensaje con /delete o usa /delete <messageId>."
                    )
            );
            return;
        }

        boolean deleted = DeleteMessageService.deleteMessage(chatId, targetMessageId);
        if (!deleted) {
            MessageService.sendMessage(
                    chatId,
                    I18n.t(
                            language,
                            "I couldn't delete that message. Check bot permissions and make sure the message exists.",
                            "No pude borrar ese mensaje. Verifica permisos del bot y que el mensaje exista."
                    )
            );
        }
    }
}
