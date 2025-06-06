package nbots.telegram.org;

import nbots.telegram.org.components.AppEnvComponent;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {
    public static void main(String[] args) {
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(AppEnvComponent.getBotToken(), new NBot());

            System.out.println("Bot successfully started!");

            Thread.currentThread().join();
        } catch (Exception e) {
            System.out.println("Exception! \n" + e.getMessage());
        }
    }
}
