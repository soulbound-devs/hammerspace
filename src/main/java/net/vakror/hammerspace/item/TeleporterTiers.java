package net.vakror.hammerspace.item;

public enum TeleporterTiers implements ITeleporterTier {
    WOOD(5, 5, 5, "wood"),
    IRON(10, 10, 10, "iron"),
    GOLD(15, 15, 15, "gold"),
    EMERALD(20, 20, 20, "emerald"),
    DIAMOND(30, 30, 30, "diamond"),
    NETHERITE(50, 50, 50, "netherite");

    private final int maxWidth;
    private final int maxHeight;
    private final int maxLength;
    private final String id;

    TeleporterTiers(int maxWidth, int maxHeight, int maxLength, String id) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.maxLength = maxLength;
        this.id = id;
    }

    @Override
    public int getMaxWidth() {
        return maxWidth;
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public String getId() {
        return id;
    }
}
