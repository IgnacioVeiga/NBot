package nbots.telegram.org.utils;

import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Locale;

public final class CommandUtils {
    private CommandUtils() {
    }

    public static String normalizeCommand(String text) {
        if (text == null) {
            return "";
        }

        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return "";
        }

        String firstToken = trimmed.split("\\s+", 2)[0].toLowerCase(Locale.ROOT);
        int botMentionIndex = firstToken.indexOf('@');
        if (botMentionIndex > 0) {
            return firstToken.substring(0, botMentionIndex);
        }
        return firstToken;
    }

    public static String commandArgs(String text) {
        if (text == null) {
            return "";
        }

        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return "";
        }

        String[] parts = trimmed.split("\\s+", 2);
        return parts.length < 2 ? "" : parts[1].trim();
    }

    public static String userDisplayName(User user) {
        if (user == null) {
            return "unknown";
        }
        if (user.getUserName() != null && !user.getUserName().isBlank()) {
            return "@" + user.getUserName();
        }

        String firstName = user.getFirstName() == null ? "" : user.getFirstName().trim();
        String lastName = user.getLastName() == null ? "" : user.getLastName().trim();
        String fullName = (firstName + " " + lastName).trim();
        if (!fullName.isEmpty()) {
            return fullName;
        }

        return "user#" + user.getId();
    }
}
