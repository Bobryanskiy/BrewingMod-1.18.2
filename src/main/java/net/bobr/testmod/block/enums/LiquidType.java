package net.bobr.testmod.block.enums;

import net.minecraft.util.StringIdentifiable;

public enum LiquidType implements StringIdentifiable {
    NOTHING (0,"nothing"),
    WATER (1,"water"),
    MEAD (2,"mead"),
    BEER (3,"beer");

    private final String name;
    private final int id;

    private LiquidType (int id, String name) {
        this.id = id;
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
        return this.id;
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
