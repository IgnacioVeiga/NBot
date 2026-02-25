package nbots.telegram.org.services;

import nbots.telegram.org.components.AppEnvComponent;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public final class AdminAuthService {
    private AdminAuthService() {
    }

    public static boolean isAdmin(Update update) {
        if (update == null || !update.hasMessage()) {
            return false;
        }

        User user = update.getMessage().getFrom();
        if (user == null || user.getId() == null) {
            return false;
        }

        Long adminUserId = AppEnvComponent.getAdminUserId();
        if (adminUserId == null) {
            return false;
        }

        return adminUserId.equals(user.getId());
    }
}
