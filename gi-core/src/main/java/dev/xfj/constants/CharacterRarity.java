package dev.xfj.constants;

public enum CharacterRarity {
    QUALITY_PURPLE(4),
    QUALITY_ORANGE(5),
    QUALITY_ORANGE_SP(5);

    private final int star;

    CharacterRarity(int star) {
        this.star = star;
    }

    public int getStar() {
        return star;
    }
}
