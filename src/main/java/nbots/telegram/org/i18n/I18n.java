package nbots.telegram.org.i18n;

import nbots.telegram.org.services.UserLanguageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public final class I18n {
    private I18n() {
    }

    public static BotLanguage language(Update update) {
        return UserLanguageService.resolveLanguage(update);
    }

    public static String t(BotLanguage language, String english, String spanish) {
        return language == BotLanguage.ES ? spanish : english;
    }
}
