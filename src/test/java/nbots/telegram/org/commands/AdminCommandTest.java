package nbots.telegram.org.commands;

import nbots.telegram.org.components.AppEnvComponent;
import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.testsupport.BaseBotTest;
import nbots.telegram.org.testsupport.TelegramUpdateFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminCommandTest extends BaseBotTest {
    @Test
    void returnsTrueOnlyForConfiguredAdmin() {
        AppEnvComponent.setEnvOverrideForTests("ADMIN_USER_ID", "99");
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        AdminCommand command = new AdminCommand();
        command.execute(TelegramUpdateFactory.textUpdate(1L, 99L, "admin", "Admin", "en", "/admin"));
        command.execute(TelegramUpdateFactory.textUpdate(1L, 100L, "user", "User", "en", "/admin"));

        assertEquals(1, messages.size());
        assertEquals("true", messages.getFirst());
    }

    @Test
    void staysSilentWhenAdminIdIsNotConfigured() {
        AppEnvComponent.setEnvOverrideForTests("ADMIN_USER_ID", "");
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new AdminCommand().execute(TelegramUpdateFactory.textUpdate(1L, 99L, "admin", "Admin", "en", "/admin"));

        assertTrue(messages.isEmpty());
    }
}
