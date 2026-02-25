package nbots.telegram.org.i18n;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BotLanguageTest {
    @Test
    void fromCodeSupportsLanguageVariants() {
        assertEquals(BotLanguage.EN, BotLanguage.fromCode("en"));
        assertEquals(BotLanguage.EN, BotLanguage.fromCode("en-US"));
        assertEquals(BotLanguage.ES, BotLanguage.fromCode("es"));
        assertEquals(BotLanguage.ES, BotLanguage.fromCode("es_AR"));
    }

    @Test
    void fromCodeReturnsNullForUnsupportedOrBlank() {
        assertNull(BotLanguage.fromCode(null));
        assertNull(BotLanguage.fromCode(" "));
        assertNull(BotLanguage.fromCode("pt"));
    }
}
