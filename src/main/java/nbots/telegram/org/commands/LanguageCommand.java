package nbots.telegram.org.commands;

import nbots.telegram.org.i18n.BotLanguage;
import nbots.telegram.org.i18n.I18n;
import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.services.UserLanguageService;
import nbots.telegram.org.utils.CommandUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class LanguageCommand implements CommandHandler.Command {
    @Override
    public void execute(Update update) {
        if (update == null || !update.hasMessage() || update.getMessage().getFrom() == null) {
            return;
        }

        long chatId = update.getMessage().getChatId();
        User user = update.getMessage().getFrom();
        BotLanguage currentLanguage = UserLanguageService.resolveLanguage(update);
        String args = CommandUtils.commandArgs(update.getMessage().getText());

        if (args.isBlank()) {
            String current = currentLanguage.code().toUpperCase();
            MessageService.sendMessage(
                    chatId,
                    I18n.t(
                            currentLanguage,
                            "Current language: " + current + "\nUsage: /lang <en|es>",
                            "Idioma actual: " + current + "\nUso: /lang <en|es>"
                    )
            );
            return;
        }

        String languageToken = args.split("\\s+", 2)[0];
        BotLanguage requestedLanguage = BotLanguage.fromCode(languageToken);
        if (requestedLanguage == null) {
            MessageService.sendMessage(
                    chatId,
                    I18n.t(
                            currentLanguage,
                            "Invalid language. Use /lang en or /lang es.",
                            "Idioma inválido. Usa /lang en o /lang es."
                    )
            );
            return;
        }

        UserLanguageService.setLanguage(user.getId(), requestedLanguage);
        MessageService.sendMessage(
                chatId,
                I18n.t(
                        requestedLanguage,
                        "Language saved: English",
                        "Idioma guardado: Español"
                )
        );
    }
}
