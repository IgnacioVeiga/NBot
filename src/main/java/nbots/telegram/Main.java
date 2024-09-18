package nbots.telegram;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {
    public static void main(String[] args) {
        final String botToken = System.getenv("TELEGRAM_TOKEN");
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new NBot(botToken));
            System.out.println("Bot successfully started!");
            Thread.currentThread().join();
        } catch (Exception e) {
            System.out.println("Exception! \n" + e.getMessage());
        }
    }
}
