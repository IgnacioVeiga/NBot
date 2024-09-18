package nbots.telegram;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {
    public static void main(String[] args) {
        final String botToken = System.getenv("TELEGRAM_TOKEN");
        // Using try-with-resources to allow autoclose to run upon finishing
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new EchoBot(botToken));
            System.out.println("Bot successfully started!");
            // Ensure this process wait forever
            Thread.currentThread().join();
        } catch (Exception e) {
            System.out.println("Exception! \n" + e.getMessage());
        }

    }
}