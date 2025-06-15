package nbots.telegram.org.commands;

import nbots.telegram.org.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.Random;

public class QuoteCommand implements CommandHandler.Command {
    private static final String[] QUOTES = {
        "La vida es lo que pasa mientras estás ocupado haciendo otros planes. - John Lennon",
        "El único modo de hacer un gran trabajo es amar lo que haces. - Steve Jobs",
        "No cuentes los días, haz que los días cuenten. - Muhammad Ali",
        "El éxito es la suma de pequeños esfuerzos repetidos día tras día. - Robert Collier",
        "La mejor manera de predecir el futuro es crearlo. - Peter Drucker", 
        "La vida es 10% lo que me ocurre y 90% cómo reacciono a ello. - Charles R. Swindoll",
        "No es la montaña lo que conquistamos, sino a nosotros mismos. - Sir Edmund Hillary",
        "La felicidad no es algo hecho. Viene de tus propias acciones. - Dalai Lama",
        "El único límite a nuestros logros de mañana es nuestras dudas y vacilaciones de hoy. - Franklin D. Roosevelt",
        "La vida es realmente simple, pero insistimos en hacerla complicada. - Confucio"
    };

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        String quote = QUOTES[new Random().nextInt(QUOTES.length)];
        MessageService.sendMessage(chatId, quote);
    }
}

