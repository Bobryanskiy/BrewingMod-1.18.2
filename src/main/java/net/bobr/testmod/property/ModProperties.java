package net.bobr.testmod.property;

import net.bobr.testmod.block.enums.LiquidType;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;

public class ModProperties {
    public static final IntProperty WATER_LEVEL = IntProperty.of("level", 0, 6);
    public static final EnumProperty<LiquidType> LIQUID_TYPE_ENUM_PROPERTY = EnumProperty.of("type", LiquidType.class);
    public static final BooleanProperty CORKED = BooleanProperty.of("corked");
}
