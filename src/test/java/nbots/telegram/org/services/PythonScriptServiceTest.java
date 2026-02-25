package nbots.telegram.org.services;

import nbots.telegram.org.components.AppEnvComponent;
import nbots.telegram.org.testsupport.BaseBotTest;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PythonScriptServiceTest extends BaseBotTest {
    @TempDir
    Path tempDir;

    @Test
    void rejectsExecutionWhenFeatureIsDisabled() {
        AppEnvComponent.setEnvOverrideForTests("PYTHON_COMMANDS_ENABLED", "false");

        PythonScriptService service = new PythonScriptService();
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.execute("hello_admin.py", List.of())
        );

        assertTrue(exception.getMessage().contains("disabled"));
    }

    @Test
    void rejectsInvalidScriptName() {
        AppEnvComponent.setEnvOverrideForTests("PYTHON_COMMANDS_ENABLED", "true");
        AppEnvComponent.setEnvOverrideForTests("PYTHON_SCRIPTS_DIR", tempDir.toString());

        PythonScriptService service = new PythonScriptService();
        assertThrows(IllegalArgumentException.class, () -> service.execute("../hack.py", List.of()));
    }

    @Test
    void executesSimplePythonScriptWhenPythonIsAvailable() throws Exception {
        assumePythonAvailable();
        Path scriptsDir = tempDir.resolve("scripts");
        Files.createDirectories(scriptsDir);
        Path script = scriptsDir.resolve("echo_args.py");
        Files.writeString(
                script,
                """
                import sys
                print("hello:" + ",".join(sys.argv[1:]))
                """,
                StandardCharsets.UTF_8
        );

        AppEnvComponent.setEnvOverrideForTests("PYTHON_COMMANDS_ENABLED", "true");
        AppEnvComponent.setEnvOverrideForTests("PYTHON_SCRIPTS_DIR", scriptsDir.toString());
        AppEnvComponent.setEnvOverrideForTests("PYTHON_BIN", "python3");

        PythonScriptService service = new PythonScriptService();
        PythonScriptService.ScriptExecutionResult result = service.execute("echo_args.py", List.of("a", "b"));

        assertEquals(0, result.exitCode());
        assertTrue(result.output().contains("hello:a,b"));
    }

    private static void assumePythonAvailable() {
        try {
            Process process = new ProcessBuilder("python3", "--version").start();
            int exitCode = process.waitFor();
            Assumptions.assumeTrue(exitCode == 0, "python3 not available");
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            Assumptions.abort("python3 not available");
        }
    }
}
