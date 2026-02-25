package nbots.telegram.org.testsupport;

import nbots.telegram.org.components.AppEnvComponent;
import nbots.telegram.org.services.DeleteMessageService;
import nbots.telegram.org.services.MessageService;
import nbots.telegram.org.services.PhotoService;
import nbots.telegram.org.services.ReminderService;
import nbots.telegram.org.services.UserLanguageService;
import nbots.telegram.org.utils.MessageHistory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseBotTest {
    @BeforeEach
    @AfterEach
    protected void resetGlobalTestState() {
        // Services are mostly static in this template, so tests must clean shared hooks and overrides.
        AppEnvComponent.clearEnvOverridesForTests();
        MessageService.clearSenderOverrideForTests();
        DeleteMessageService.clearExecutorOverrideForTests();
        PhotoService.clearSenderOverrideForTests();
        UserLanguageService.clearPreferencesFileOverrideForTests();
        MessageHistory.clearHistoryDirOverrideForTests();
        ReminderService.clearRemindersFileOverrideForTests();
        ReminderService.resetStateForTests();
    }
}
