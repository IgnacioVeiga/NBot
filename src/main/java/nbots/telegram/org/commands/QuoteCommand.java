package nbots.telegram.org.commands;

import nbots.telegram.org.i18n.BotLanguage;
import nbots.telegram.org.i18n.I18n;
import nbots.telegram.org.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.Random;

public class QuoteCommand implements CommandHandler.Command {
    private static final String[][] QUOTES = {
        {
            "Life is what happens while you are busy making other plans. - John Lennon",
            "La vida es lo que pasa mientras estás ocupado haciendo otros planes. - John Lennon"
        },
        {
            "The only way to do great work is to love what you do. - Steve Jobs",
            "El único modo de hacer un gran trabajo es amar lo que haces. - Steve Jobs"
        },
        {
            "Don't count the days, make the days count. - Muhammad Ali",
            "No cuentes los días, haz que los días cuenten. - Muhammad Ali"
        },
        {
            "Success is the sum of small efforts repeated day in and day out. - Robert Collier",
            "El éxito es la suma de pequeños esfuerzos repetidos día tras día. - Robert Collier"
        },
        {
            "The best way to predict the future is to create it. - Peter Drucker",
            "La mejor manera de predecir el futuro es crearlo. - Peter Drucker"
        },
        {
            "Life is 10% what happens to me and 90% how I react to it. - Charles R. Swindoll",
            "La vida es 10% lo que me ocurre y 90% cómo reacciono a ello. - Charles R. Swindoll"
        },
        {
            "It is not the mountain we conquer, but ourselves. - Sir Edmund Hillary",
            "No es la montaña lo que conquistamos, sino a nosotros mismos. - Sir Edmund Hillary"
        },
        {
            "Happiness is not something ready made. It comes from your own actions. - Dalai Lama",
            "La felicidad no es algo hecho. Viene de tus propias acciones. - Dalai Lama"
        },
        {
            "The only limit to our realization of tomorrow will be our doubts of today. - Franklin D. Roosevelt",
            "El único límite a nuestros logros de mañana es nuestras dudas y vacilaciones de hoy. - Franklin D. Roosevelt"
        },
        {
            "Life is really simple, but we insist on making it complicated. - Confucius",
            "La vida es realmente simple, pero insistimos en hacerla complicada. - Confucio"
        }
    };
    private static final Random RANDOM = new Random();

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        BotLanguage language = I18n.language(update);
        int index = RANDOM.nextInt(QUOTES.length);
        String quote = language == BotLanguage.ES ? QUOTES[index][1] : QUOTES[index][0];
        MessageService.sendMessage(chatId, quote);
    }
}
