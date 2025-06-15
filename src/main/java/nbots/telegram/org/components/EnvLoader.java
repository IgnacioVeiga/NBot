package nbots.telegram.org.components;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvLoader {
    private static final Properties properties = new Properties();
    static {
        try (FileInputStream fis = new FileInputStream(".env")) {
            properties.load(fis);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el archivo .env: " + e.getMessage());
        }
    }
    public static String get(String key) {
        String env = System.getenv(key);
        if (env != null) return env;
        return properties.getProperty(key);
    }
}

