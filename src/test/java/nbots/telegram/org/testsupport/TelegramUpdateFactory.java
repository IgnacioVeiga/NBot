package nbots.telegram.org.testsupport;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public final class TelegramUpdateFactory {
    private TelegramUpdateFactory() {
    }

    public static Update textUpdate(long chatId, long userId, String userName, String firstName, String languageCode, String text) {
        return textUpdate(chatId, userId, userName, firstName, languageCode, 100, text, null);
    }

    public static Update textUpdateWithReply(
            long chatId,
            long userId,
            String userName,
            String firstName,
            String languageCode,
            int messageId,
            String text,
            int replyToMessageId
    ) {
        Message reply = new Message();
        reply.setMessageId(replyToMessageId);
        reply.setChat(chat(chatId));
        return textUpdate(chatId, userId, userName, firstName, languageCode, messageId, text, reply);
    }

    private static Update textUpdate(
            long chatId,
            long userId,
            String userName,
            String firstName,
            String languageCode,
            int messageId,
            String text,
            Message replyTo
    ) {
        User user = new User(userId, firstName == null ? "User" : firstName, false);
        user.setUserName(userName);
        user.setLanguageCode(languageCode);

        Message message = new Message();
        message.setMessageId(messageId);
        message.setChat(chat(chatId));
        message.setFrom(user);
        message.setText(text);
        message.setReplyToMessage(replyTo);

        Update update = new Update();
        update.setMessage(message);
        return update;
    }

    private static Chat chat(long chatId) {
        return new Chat(chatId, "private");
    }
}
