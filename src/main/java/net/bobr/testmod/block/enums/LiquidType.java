package net.bobr.testmod.block.enums;

import net.minecraft.util.StringIdentifiable;

public enum LiquidType implements StringIdentifiable {
    NOTHING ("nothing"),
    WATER ("water"),
    MEAD ("mead"),
    BEER ("beer");

    private final String name;

    private LiquidType (String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }

    public int getId() {
        return switch (this) {
            case WATER -> 1;
            case MEAD -> 2;
            case BEER -> 3;
            default -> 0;
        };
    }

    public static LiquidType getLiquidType(int i) {
        return switch (i) {
            case 1 -> LiquidType.WATER;
            case 2 -> LiquidType.MEAD;
            case 3 -> LiquidType.BEER;
            default -> LiquidType.NOTHING;
        };
    }
}
