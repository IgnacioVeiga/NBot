package nbots.telegram.org.services;

import nbots.telegram.org.components.AppEnvComponent;
import nbots.telegram.org.utils.Logger;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class DeleteMessageService {
    @FunctionalInterface
    public interface DeleteExecutor {
        boolean delete(long chatId, int messageId);
    }

    private static volatile DeleteExecutor executorOverrideForTests;
    private static volatile OkHttpTelegramClient telegramClient;

    private DeleteMessageService() {
    }

    private static OkHttpTelegramClient getTelegramClient() {
        OkHttpTelegramClient client = telegramClient;
        if (client == null) {
            synchronized (DeleteMessageService.class) {
                client = telegramClient;
                if (client == null) {
                    client = new OkHttpTelegramClient(AppEnvComponent.getBotToken());
                    telegramClient = client;
                }
            }
        }
        return client;
    }

    public static boolean deleteMessage(long chatId, int messageId) {
        DeleteExecutor override = executorOverrideForTests;
        if (override != null) {
            return override.delete(chatId, messageId);
        }

        DeleteMessage deleteMessage = DeleteMessage.builder().chatId(chatId).messageId(messageId).build();

        try {
            getTelegramClient().execute(deleteMessage);
            Logger.log("Message with ID " + messageId + " deleted.");
            return true;
        } catch (TelegramApiException e) {
            Logger.log("Error deleting message: " + e.getMessage());
            return false;
        }
    }

    public static void setExecutorOverrideForTests(DeleteExecutor executor) {
        executorOverrideForTests = executor;
    }

    public static void clearExecutorOverrideForTests() {
        executorOverrideForTests = null;
    }
}
