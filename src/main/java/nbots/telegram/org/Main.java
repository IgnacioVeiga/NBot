package nbots.telegram.org;

import nbots.telegram.org.components.AppEnvComponent;
import nbots.telegram.org.services.ReminderService;
import nbots.telegram.org.utils.Logger;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {
    public static void main(String[] args) {
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(AppEnvComponent.getBotToken(), new NBot());
            ReminderService.initialize();

            Logger.log("Bot successfully started!");

            Thread.currentThread().join();
        } catch (Exception e) {
            Logger.log("No se pudo iniciar el bot", e);
            e.printStackTrace();
        }
    }
}
