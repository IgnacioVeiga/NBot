package nbots.telegram.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    public static void log(String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println(sdf.format(new Date()) + " - " + message);
    }
}
