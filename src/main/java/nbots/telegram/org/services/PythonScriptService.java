package nbots.telegram.org.services;

import nbots.telegram.org.components.AppEnvComponent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PythonScriptService {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final int MAX_CAPTURE_BYTES = 32 * 1024;

    public ScriptExecutionResult execute(String scriptName, List<String> args) throws IOException, InterruptedException {
        if (!AppEnvComponent.isPythonCommandsEnabled()) {
            throw new IllegalStateException("PYTHON_COMMANDS_ENABLED is disabled.");
        }

        if (scriptName == null || scriptName.isBlank()) {
            throw new IllegalArgumentException("You must specify a .py script");
        }

        String normalizedName = scriptName.trim();
        // Restrict script names to a flat filename to prevent path traversal and accidental shell usage.
        if (!normalizedName.matches("[A-Za-z0-9._-]+\\.py")) {
            throw new IllegalArgumentException("Invalid script name. Only <name>.py is allowed");
        }

        Path scriptsDir = resolveScriptsDir();
        Files.createDirectories(scriptsDir);

        Path scriptPath = scriptsDir.resolve(normalizedName).normalize();
        // Ensure the final path is still inside the allowed scripts directory.
        if (!scriptPath.startsWith(scriptsDir)) {
            throw new IllegalArgumentException("Invalid script path.");
        }
        if (!Files.exists(scriptPath) || !Files.isRegularFile(scriptPath)) {
            throw new IllegalArgumentException("Script not found in " + scriptsDir + ": " + normalizedName);
        }

        List<String> command = new ArrayList<>();
        command.add(resolvePythonExecutable());
        command.add(scriptPath.toString());
        if (args != null) {
            command.addAll(args);
        }

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
        // Drain process output in parallel so verbose scripts cannot block on a full stdout buffer.
        Thread readerThread = new Thread(() -> consumeOutput(process.getInputStream(), outputBuffer), "py-runner-output");
        readerThread.setDaemon(true);
        readerThread.start();

        boolean finished = process.waitFor(DEFAULT_TIMEOUT.toSeconds(), TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();
            readerThread.join(1_000);
            throw new IOException("Timeout running Python script (" + DEFAULT_TIMEOUT.toSeconds() + "s).");
        }

        readerThread.join(1_000);
        int exitCode = process.exitValue();
        String output = outputBuffer.toString(StandardCharsets.UTF_8);

        return new ScriptExecutionResult(exitCode, output, command);
    }

    private static void consumeOutput(InputStream inputStream, ByteArrayOutputStream out) {
        byte[] buffer = new byte[1024];
        try (InputStream in = inputStream) {
            int read;
            while ((read = in.read(buffer)) != -1) {
                int writable = Math.min(read, MAX_CAPTURE_BYTES - out.size());
                if (writable > 0) {
                    out.write(buffer, 0, writable);
                }
            }
        } catch (IOException ignored) {
        }
    }

    private static Path resolveScriptsDir() {
        String configured = AppEnvComponent.getPythonScriptsDir();
        if (configured == null || configured.isBlank()) {
            return Path.of("scripts", "python").toAbsolutePath().normalize();
        }
        return Path.of(configured).toAbsolutePath().normalize();
    }

    private static String resolvePythonExecutable() {
        String configured = AppEnvComponent.getPythonExecutable();
        return (configured == null || configured.isBlank()) ? "python3" : configured;
    }

    public record ScriptExecutionResult(int exitCode, String output, List<String> command) {
    }
}
