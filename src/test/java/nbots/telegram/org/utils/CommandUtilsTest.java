package nbots.telegram.org.utils;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandUtilsTest {
    @Test
    void normalizeCommandRemovesBotMentionAndLowercases() {
        assertEquals("/help", CommandUtils.normalizeCommand(" /Help@MyBot   arg "));
    }

    @Test
    void commandArgsReturnsRemainderWithoutExtraSpaces() {
        assertEquals("hello world", CommandUtils.commandArgs("/echo   hello world "));
        assertEquals("", CommandUtils.commandArgs("/echo"));
    }

    @Test
    void userDisplayNamePrefersUsernameThenFullNameThenId() {
        User withUsername = new User(1L, "User", false);
        withUsername.setUserName("nasho");
        assertEquals("@nasho", CommandUtils.userDisplayName(withUsername));

        User withName = new User(2L, "Ignacio", false);
        withName.setFirstName("Ignacio");
        withName.setLastName("Veiga");
        assertEquals("Ignacio Veiga", CommandUtils.userDisplayName(withName));

        User fallback = new User(3L, " ", false);
        fallback.setFirstName(" ");
        assertEquals("user#3", CommandUtils.userDisplayName(fallback));
    }
}
