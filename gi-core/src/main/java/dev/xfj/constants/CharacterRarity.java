package dev.xfj.constants;

public enum CharacterRarity {
    QUALITY_PURPLE(4),
    QUALITY_ORANGE(5),
    QUALITY_ORANGE_SP(5); //Only used for Aloy

    private final int starValue;

    CharacterRarity(int starValue) {
        this.starValue = starValue;
    }

    public int getStarValue() {
        return starValue;
    }
}
