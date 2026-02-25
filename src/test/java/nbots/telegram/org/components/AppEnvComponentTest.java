package nbots.telegram.org.components;

import nbots.telegram.org.testsupport.BaseBotTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppEnvComponentTest extends BaseBotTest {
    @Test
    void testOverridesDriveBooleanAndNumericParsing() {
        AppEnvComponent.setEnvOverrideForTests("PYTHON_COMMANDS_ENABLED", "true");
        AppEnvComponent.setEnvOverrideForTests("ADMIN_USER_ID", "123");

        assertTrue(AppEnvComponent.isPythonCommandsEnabled());
        assertEquals(123L, AppEnvComponent.getAdminUserId());
    }

    @Test
    void blankOverridesBehaveAsMissingValues() {
        AppEnvComponent.setEnvOverrideForTests("PYTHON_COMMANDS_ENABLED", " ");
        AppEnvComponent.setEnvOverrideForTests("ADMIN_USER_ID", " ");

        assertFalse(AppEnvComponent.isPythonCommandsEnabled());
        assertNull(AppEnvComponent.getAdminUserId());
    }
}
