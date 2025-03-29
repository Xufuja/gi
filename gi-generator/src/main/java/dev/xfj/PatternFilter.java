package dev.xfj;

public enum PatternFilter {
    NONE,
    CHIORI_TEST,
    MORE_THAN_1_UNDERSCORE;

    public boolean filterOut(String string) {
        return switch (this) {
            case NONE -> false;
            case CHIORI_TEST -> !string.equals("ConfigAvatar_Chiori.json");
            case MORE_THAN_1_UNDERSCORE -> string.chars().filter(character -> character == '_').count() > 1;
        };
    }
}
