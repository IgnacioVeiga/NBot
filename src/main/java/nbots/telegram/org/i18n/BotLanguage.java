package nbots.telegram.org.i18n;

public enum BotLanguage {
    EN("en"),
    ES("es");

    private final String code;

    BotLanguage(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    public static BotLanguage fromCode(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }

        String normalized = code.trim().toLowerCase();
        if (normalized.startsWith("es")) {
            return ES;
        }
        if (normalized.startsWith("en")) {
            return EN;
        }
        return null;
    }
}
