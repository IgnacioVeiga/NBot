package nbots.telegram.org.commands;

import nbots.telegram.org.components.AppEnvComponent;
import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.testsupport.BaseBotTest;
import nbots.telegram.org.testsupport.TelegramUpdateFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PythonCommandsAuthorizationTest extends BaseBotTest {
    @Test
    void pythonCommandsAreSilentForNonAdmin() {
        AppEnvComponent.setEnvOverrideForTests("ADMIN_USER_ID", "99");
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new RunPythonCommand().execute(TelegramUpdateFactory.textUpdate(1L, 10L, "u", "User", "en", "/pyrun hello_admin.py"));
        new PyHelloCommand().execute(TelegramUpdateFactory.textUpdate(1L, 10L, "u", "User", "en", "/pyhello"));

        assertTrue(messages.isEmpty());
    }

    @Test
    void adminSeesDisabledMessageWhenPythonCommandsAreOff() {
        AppEnvComponent.setEnvOverrideForTests("ADMIN_USER_ID", "99");
        AppEnvComponent.setEnvOverrideForTests("PYTHON_COMMANDS_ENABLED", "false");
        List<String> messages = new ArrayList<>();
        MessageService.setSenderOverrideForTests((chatId, text) -> messages.add(text));

        new PyHelloCommand().execute(TelegramUpdateFactory.textUpdate(1L, 99L, "admin", "Admin", "en", "/pyhello"));

        assertTrue(messages.getFirst().contains("Command disabled."));
    }
}
