package net.bobr.testmod.block.custom;

import net.bobr.testmod.block.entity.ModBlockEntities;
import net.bobr.testmod.block.entity.OakBarrelBlockEntity;
import net.bobr.testmod.block.enums.LiquidType;
import net.bobr.testmod.property.ModProperties;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.network.MessageType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class OakBarrelBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final Identifier CONTENTS = new Identifier("contents");
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final IntProperty WATER_LEVEL = ModProperties.WATER_LEVEL;
    public static final EnumProperty<LiquidType> LIQUID_TYPE_ENUM_PROPERTY = ModProperties.LIQUID_TYPE_ENUM_PROPERTY;
    public static final BooleanProperty CORKED = ModProperties.CORKED;

    public OakBarrelBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(WATER_LEVEL, 0).with(LIQUID_TYPE_ENUM_PROPERTY, LiquidType.NOTHING).with(CORKED, false));
    }

    private static final VoxelShape SHAPE_N = Stream.of(
            Block.createCuboidShape(0, 1, 1, 1, 2, 15),
            Block.createCuboidShape(15, 1, 1, 16, 2, 15),
            Block.createCuboidShape(0, 1, 15, 16, 3, 16),
            Block.createCuboidShape(10, 0, 15, 16, 1, 16),
            Block.createCuboidShape(6, 0.5, 15, 10, 1, 16),
            Block.createCuboidShape(0, 0, 15, 6, 1, 16),
            Block.createCuboidShape(0, 1, 0, 16, 3, 1),
            Block.createCuboidShape(10, 0, 0, 16, 1, 1),
            Block.createCuboidShape(6, 0.5, 0, 10, 1, 1),
            Block.createCuboidShape(0, 0, 0, 6, 1, 1),
            Block.createCuboidShape(7.800000000000001, 4.3, -2.5, 8.2, 4.7, -2),
            Block.createCuboidShape(7.5, 4, -2, 8.5, 5, 0),
            Block.createCuboidShape(7.100000000000001, 5.5, -1.5, 7.800000000000001, 5.9, -1),
            Block.createCuboidShape(7.4, 17, 4.9, 8.6, 17.9, 6.1),
            Block.createCuboidShape(7.800000000000001, 3.8, -1.5, 8.2, 6.3, -1),
            Block.createCuboidShape(7.6, 16, 5.1, 8.4, 17, 5.9),
            Block.createCuboidShape(1, 6, 16, 2, 12, 17),
            Block.createCuboidShape(2, 11, 16, 3, 13, 17),
            Block.createCuboidShape(3, 12, 16, 4, 14, 17),
            Block.createCuboidShape(4, 13, 16, 5, 15, 17),
            Block.createCuboidShape(5, 14, 16, 6, 16, 17),
            Block.createCuboidShape(10, 14, 16, 11, 16, 17),
            Block.createCuboidShape(11, 13, 16, 12, 15, 17),
            Block.createCuboidShape(12, 12, 16, 13, 14, 17),
            Block.createCuboidShape(13, 11, 16, 14, 13, 17),
            Block.createCuboidShape(13, 5, 16, 14, 7, 17),
            Block.createCuboidShape(12, 4, 16, 13, 6, 17),
            Block.createCuboidShape(10, 2, 16, 11, 4, 17),
            Block.createCuboidShape(11, 3, 16, 12, 5, 17),
            Block.createCuboidShape(4, 3, 16, 5, 5, 17),
            Block.createCuboidShape(5, 2, 16, 6, 4, 17),
            Block.createCuboidShape(3, 4, 16, 4, 6, 17),
            Block.createCuboidShape(2, 5, 16, 3, 7, 17),
            Block.createCuboidShape(6, 2, 16, 10, 3, 17),
            Block.createCuboidShape(6, 15, 16, 10, 16, 17),
            Block.createCuboidShape(14, 6, 16, 15, 12, 17),
            Block.createCuboidShape(1, 6, -1, 2, 12, 0),
            Block.createCuboidShape(2, 11, -1, 3, 13, 0),
            Block.createCuboidShape(3, 12, -1, 4, 14, 0),
            Block.createCuboidShape(4, 13, -1, 5, 15, 0),
            Block.createCuboidShape(5, 14, -1, 6, 16, 0),
            Block.createCuboidShape(10, 14, -1, 11, 16, 0),
            Block.createCuboidShape(11, 13, -1, 12, 15, 0),
            Block.createCuboidShape(12, 12, -1, 13, 14, 0),
            Block.createCuboidShape(13, 11, -1, 14, 13, 0),
            Block.createCuboidShape(13, 5, -1, 14, 7, 0),
            Block.createCuboidShape(12, 4, -1, 13, 6, 0),
            Block.createCuboidShape(10, 2, -1, 11, 4, 0),
            Block.createCuboidShape(11, 3, -1, 12, 5, 0),
            Block.createCuboidShape(4, 3, -1, 5, 5, 0),
            Block.createCuboidShape(5, 2, -1, 6, 4, 0),
            Block.createCuboidShape(3, 4, -1, 4, 6, 0),
            Block.createCuboidShape(2, 5, -1, 3, 7, 0),
            Block.createCuboidShape(6, 2, -1, 10, 3, 0),
            Block.createCuboidShape(6, 15, -1, 10, 16, 0),
            Block.createCuboidShape(14, 6, -1, 15, 12, 0),
            Block.createCuboidShape(0.9999000000000002, 5.9999, 2.9999000000000002, 2.0001000000000007, 12.0001, 4.0001),
            Block.createCuboidShape(12.9999, 4.9999, 2.9999000000000002, 14.0001, 13.0001, 4.0001),
            Block.createCuboidShape(11.9999, 3.9999000000000002, 2.9999000000000002, 13.0001, 14.0001, 4.0001),
            Block.createCuboidShape(10.9999, 2.9999000000000002, 2.9999000000000002, 12.0001, 15.0001, 4.0001),
            Block.createCuboidShape(3.9999000000000002, 2.9999000000000002, 2.9999000000000002, 5.0001, 15.0001, 4.0001),
            Block.createCuboidShape(2.9999000000000002, 3.9999000000000002, 2.9999000000000002, 4.0001, 14.0001, 4.0001),
            Block.createCuboidShape(1.9999000000000002, 4.9999, 2.9999000000000002, 3.0001000000000007, 13.0001, 4.0001),
            Block.createCuboidShape(4.9999, 1.9999000000000002, 2.9999000000000002, 11.0001, 16.0001, 4.0001),
            Block.createCuboidShape(13.9999, 5.9999, 2.9999000000000002, 15.0001, 12.0001, 4.0001),
            Block.createCuboidShape(0.9999000000000002, 5.9999, 11.9999, 2.0001000000000007, 12.0001, 13.0001),
            Block.createCuboidShape(12.9999, 4.9999, 11.9999, 14.0001, 13.0001, 13.0001),
            Block.createCuboidShape(11.9999, 3.9999000000000002, 11.9999, 13.0001, 14.0001, 13.0001),
            Block.createCuboidShape(10.9999, 2.9999000000000002, 11.9999, 12.0001, 15.0001, 13.0001),
            Block.createCuboidShape(3.9999000000000002, 2.9999000000000002, 11.9999, 5.0001, 15.0001, 13.0001),
            Block.createCuboidShape(2.9999000000000002, 3.9999000000000002, 11.9999, 4.0001, 14.0001, 13.0001),
            Block.createCuboidShape(1.9999000000000002, 4.9999, 11.9999, 3.0001000000000007, 13.0001, 13.0001),
            Block.createCuboidShape(4.9999, 1.9999000000000002, 11.9999, 11.0001, 16.0001, 13.0001),
            Block.createCuboidShape(13.9999, 5.9999, 11.9999, 15.0001, 12.0001, 13.0001),
            Block.createCuboidShape(5, 2, 0, 11, 3, 16),
            Block.createCuboidShape(4, 3, 0, 12, 4, 16),
            Block.createCuboidShape(3, 4, 0, 13, 5, 16),
            Block.createCuboidShape(2, 5, 0, 14, 6, 16),
            Block.createCuboidShape(1, 6, 0, 15, 7, 16),
            Block.createCuboidShape(1, 7, 0, 15, 8, 16),
            Block.createCuboidShape(1, 8, 0, 15, 9, 16),
            Block.createCuboidShape(1, 9, 0, 15, 10, 16),
            Block.createCuboidShape(1, 10, 0, 15, 11, 16),
            Block.createCuboidShape(1, 11, 0, 15, 12, 16),
            Block.createCuboidShape(2, 12, 0, 14, 13, 16),
            Block.createCuboidShape(3, 13, 0, 13, 14, 16),
            Block.createCuboidShape(4, 14, 0, 12, 15, 16),
            Block.createCuboidShape(5, 15, 0, 11, 16, 16)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    public static final VoxelShape SHAPE_W = Stream.of(
            Block.createCuboidShape(1, 1, 15, 15, 2, 16),
            Block.createCuboidShape(1, 1, 0, 15, 2, 1),
            Block.createCuboidShape(15, 1, 0, 16, 3, 16),
            Block.createCuboidShape(15, 0, 0, 16, 1, 6),
            Block.createCuboidShape(15, 0.5, 6, 16, 1, 10),
            Block.createCuboidShape(15, 0, 10, 16, 1, 16),
            Block.createCuboidShape(0, 1, 0, 1, 3, 16),
            Block.createCuboidShape(0, 0, 0, 1, 1, 6),
            Block.createCuboidShape(0, 0.5, 6, 1, 1, 10),
            Block.createCuboidShape(0, 0, 10, 1, 1, 16),
            Block.createCuboidShape(-2.5, 4.3, 7.800000000000001, -2, 4.7, 8.2),
            Block.createCuboidShape(-2, 4, 7.5, 0, 5, 8.5),
            Block.createCuboidShape(-1.5, 5.5, 8.2, -1, 5.9, 8.899999999999999),
            Block.createCuboidShape(4.9, 17, 7.4, 6.1, 17.9, 8.6),
            Block.createCuboidShape(-1.5, 3.8, 7.800000000000001, -1, 6.3, 8.2),
            Block.createCuboidShape(5.1, 16, 7.6, 5.9, 17, 8.4),
            Block.createCuboidShape(16, 6, 14, 17, 12, 15),
            Block.createCuboidShape(16, 11, 13, 17, 13, 14),
            Block.createCuboidShape(16, 12, 12, 17, 14, 13),
            Block.createCuboidShape(16, 13, 11, 17, 15, 12),
            Block.createCuboidShape(16, 14, 10, 17, 16, 11),
            Block.createCuboidShape(16, 14, 5, 17, 16, 6),
            Block.createCuboidShape(16, 13, 4, 17, 15, 5),
            Block.createCuboidShape(16, 12, 3, 17, 14, 4),
            Block.createCuboidShape(16, 11, 2, 17, 13, 3),
            Block.createCuboidShape(16, 5, 2, 17, 7, 3),
            Block.createCuboidShape(16, 4, 3, 17, 6, 4),
            Block.createCuboidShape(16, 2, 5, 17, 4, 6),
            Block.createCuboidShape(16, 3, 4, 17, 5, 5),
            Block.createCuboidShape(16, 3, 11, 17, 5, 12),
            Block.createCuboidShape(16, 2, 10, 17, 4, 11),
            Block.createCuboidShape(16, 4, 12, 17, 6, 13),
            Block.createCuboidShape(16, 5, 13, 17, 7, 14),
            Block.createCuboidShape(16, 2, 6, 17, 3, 10),
            Block.createCuboidShape(16, 15, 6, 17, 16, 10),
            Block.createCuboidShape(16, 6, 1, 17, 12, 2),
            Block.createCuboidShape(-1, 6, 14, 0, 12, 15),
            Block.createCuboidShape(-1, 11, 13, 0, 13, 14),
            Block.createCuboidShape(-1, 12, 12, 0, 14, 13),
            Block.createCuboidShape(-1, 13, 11, 0, 15, 12),
            Block.createCuboidShape(-1, 14, 10, 0, 16, 11),
            Block.createCuboidShape(-1, 14, 5, 0, 16, 6),
            Block.createCuboidShape(-1, 13, 4, 0, 15, 5),
            Block.createCuboidShape(-1, 12, 3, 0, 14, 4),
            Block.createCuboidShape(-1, 11, 2, 0, 13, 3),
            Block.createCuboidShape(-1, 5, 2, 0, 7, 3),
            Block.createCuboidShape(-1, 4, 3, 0, 6, 4),
            Block.createCuboidShape(-1, 2, 5, 0, 4, 6),
            Block.createCuboidShape(-1, 3, 4, 0, 5, 5),
            Block.createCuboidShape(-1, 3, 11, 0, 5, 12),
            Block.createCuboidShape(-1, 2, 10, 0, 4, 11),
            Block.createCuboidShape(-1, 4, 12, 0, 6, 13),
            Block.createCuboidShape(-1, 5, 13, 0, 7, 14),
            Block.createCuboidShape(-1, 2, 6, 0, 3, 10),
            Block.createCuboidShape(-1, 15, 6, 0, 16, 10),
            Block.createCuboidShape(-1, 6, 1, 0, 12, 2),
            Block.createCuboidShape(2.9999000000000002, 5.9999, 13.9999, 4.0001, 12.0001, 15.0001),
            Block.createCuboidShape(2.9999000000000002, 4.9999, 1.9999000000000002, 4.0001, 13.0001, 3.0000999999999998),
            Block.createCuboidShape(2.9999000000000002, 3.9999000000000002, 2.9999000000000002, 4.0001, 14.0001, 4.0001),
            Block.createCuboidShape(2.9999000000000002, 2.9999000000000002, 3.9999000000000002, 4.0001, 15.0001, 5.0001),
            Block.createCuboidShape(2.9999000000000002, 2.9999000000000002, 10.9999, 4.0001, 15.0001, 12.0001),
            Block.createCuboidShape(2.9999000000000002, 3.9999000000000002, 11.9999, 4.0001, 14.0001, 13.0001),
            Block.createCuboidShape(2.9999000000000002, 4.9999, 12.9999, 4.0001, 13.0001, 14.0001),
            Block.createCuboidShape(2.9999000000000002, 1.9999000000000002, 4.9999, 4.0001, 16.0001, 11.0001),
            Block.createCuboidShape(2.9999000000000002, 5.9999, 0.9999000000000002, 4.0001, 12.0001, 2.0000999999999998),
            Block.createCuboidShape(11.9999, 5.9999, 13.9999, 13.0001, 12.0001, 15.0001),
            Block.createCuboidShape(11.9999, 4.9999, 1.9999000000000002, 13.0001, 13.0001, 3.0000999999999998),
            Block.createCuboidShape(11.9999, 3.9999000000000002, 2.9999000000000002, 13.0001, 14.0001, 4.0001),
            Block.createCuboidShape(11.9999, 2.9999000000000002, 3.9999000000000002, 13.0001, 15.0001, 5.0001),
            Block.createCuboidShape(11.9999, 2.9999000000000002, 10.9999, 13.0001, 15.0001, 12.0001),
            Block.createCuboidShape(11.9999, 3.9999000000000002, 11.9999, 13.0001, 14.0001, 13.0001),
            Block.createCuboidShape(11.9999, 4.9999, 12.9999, 13.0001, 13.0001, 14.0001),
            Block.createCuboidShape(11.9999, 1.9999000000000002, 4.9999, 13.0001, 16.0001, 11.0001),
            Block.createCuboidShape(11.9999, 5.9999, 0.9999000000000002, 13.0001, 12.0001, 2.0000999999999998),
            Block.createCuboidShape(0, 2, 5, 16, 3, 11),
            Block.createCuboidShape(0, 3, 4, 16, 4, 12),
            Block.createCuboidShape(0, 4, 3, 16, 5, 13),
            Block.createCuboidShape(0, 5, 2, 16, 6, 14),
            Block.createCuboidShape(0, 6, 1, 16, 7, 15),
            Block.createCuboidShape(0, 7, 1, 16, 8, 15),
            Block.createCuboidShape(0, 8, 1, 16, 9, 15),
            Block.createCuboidShape(0, 9, 1, 16, 10, 15),
            Block.createCuboidShape(0, 10, 1, 16, 11, 15),
            Block.createCuboidShape(0, 11, 1, 16, 12, 15),
            Block.createCuboidShape(0, 12, 2, 16, 13, 14),
            Block.createCuboidShape(0, 13, 3, 16, 14, 13),
            Block.createCuboidShape(0, 14, 4, 16, 15, 12),
            Block.createCuboidShape(0, 15, 5, 16, 16, 11)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    public static final VoxelShape SHAPE_S = Stream.of(
            Block.createCuboidShape(15, 1, 1, 16, 2, 15),
            Block.createCuboidShape(0, 1, 1, 1, 2, 15),
            Block.createCuboidShape(0, 1, 0, 16, 3, 1),
            Block.createCuboidShape(0, 0, 0, 6, 1, 1),
            Block.createCuboidShape(6, 0.5, 0, 10, 1, 1),
            Block.createCuboidShape(10, 0, 0, 16, 1, 1),
            Block.createCuboidShape(0, 1, 15, 16, 3, 16),
            Block.createCuboidShape(0, 0, 15, 6, 1, 16),
            Block.createCuboidShape(6, 0.5, 15, 10, 1, 16),
            Block.createCuboidShape(10, 0, 15, 16, 1, 16),
            Block.createCuboidShape(7.800000000000001, 4.3, 18, 8.2, 4.7, 18.5),
            Block.createCuboidShape(7.5, 4, 16, 8.5, 5, 18),
            Block.createCuboidShape(8.2, 5.5, 17, 8.899999999999999, 5.9, 17.5),
            Block.createCuboidShape(7.4, 17, 9.9, 8.6, 17.9, 11.1),
            Block.createCuboidShape(7.800000000000001, 3.8, 17, 8.2, 6.3, 17.5),
            Block.createCuboidShape(7.6, 16, 10.1, 8.4, 17, 10.9),
            Block.createCuboidShape(14, 6, -1, 15, 12, 0),
            Block.createCuboidShape(13, 11, -1, 14, 13, 0),
            Block.createCuboidShape(12, 12, -1, 13, 14, 0),
            Block.createCuboidShape(11, 13, -1, 12, 15, 0),
            Block.createCuboidShape(10, 14, -1, 11, 16, 0),
            Block.createCuboidShape(5, 14, -1, 6, 16, 0),
            Block.createCuboidShape(4, 13, -1, 5, 15, 0),
            Block.createCuboidShape(3, 12, -1, 4, 14, 0),
            Block.createCuboidShape(2, 11, -1, 3, 13, 0),
            Block.createCuboidShape(2, 5, -1, 3, 7, 0),
            Block.createCuboidShape(3, 4, -1, 4, 6, 0),
            Block.createCuboidShape(5, 2, -1, 6, 4, 0),
            Block.createCuboidShape(4, 3, -1, 5, 5, 0),
            Block.createCuboidShape(11, 3, -1, 12, 5, 0),
            Block.createCuboidShape(10, 2, -1, 11, 4, 0),
            Block.createCuboidShape(12, 4, -1, 13, 6, 0),
            Block.createCuboidShape(13, 5, -1, 14, 7, 0),
            Block.createCuboidShape(6, 2, -1, 10, 3, 0),
            Block.createCuboidShape(6, 15, -1, 10, 16, 0),
            Block.createCuboidShape(1, 6, -1, 2, 12, 0),
            Block.createCuboidShape(14, 6, 16, 15, 12, 17),
            Block.createCuboidShape(13, 11, 16, 14, 13, 17),
            Block.createCuboidShape(12, 12, 16, 13, 14, 17),
            Block.createCuboidShape(11, 13, 16, 12, 15, 17),
            Block.createCuboidShape(10, 14, 16, 11, 16, 17),
            Block.createCuboidShape(5, 14, 16, 6, 16, 17),
            Block.createCuboidShape(4, 13, 16, 5, 15, 17),
            Block.createCuboidShape(3, 12, 16, 4, 14, 17),
            Block.createCuboidShape(2, 11, 16, 3, 13, 17),
            Block.createCuboidShape(2, 5, 16, 3, 7, 17),
            Block.createCuboidShape(3, 4, 16, 4, 6, 17),
            Block.createCuboidShape(5, 2, 16, 6, 4, 17),
            Block.createCuboidShape(4, 3, 16, 5, 5, 17),
            Block.createCuboidShape(11, 3, 16, 12, 5, 17),
            Block.createCuboidShape(10, 2, 16, 11, 4, 17),
            Block.createCuboidShape(12, 4, 16, 13, 6, 17),
            Block.createCuboidShape(13, 5, 16, 14, 7, 17),
            Block.createCuboidShape(6, 2, 16, 10, 3, 17),
            Block.createCuboidShape(6, 15, 16, 10, 16, 17),
            Block.createCuboidShape(1, 6, 16, 2, 12, 17),
            Block.createCuboidShape(13.9999, 5.9999, 11.9999, 15.0001, 12.0001, 13.0001),
            Block.createCuboidShape(1.9999000000000002, 4.9999, 11.9999, 3.0000999999999998, 13.0001, 13.0001),
            Block.createCuboidShape(2.9999000000000002, 3.9999000000000002, 11.9999, 4.0001, 14.0001, 13.0001),
            Block.createCuboidShape(3.9999000000000002, 2.9999000000000002, 11.9999, 5.0001, 15.0001, 13.0001),
            Block.createCuboidShape(10.9999, 2.9999000000000002, 11.9999, 12.0001, 15.0001, 13.0001),
            Block.createCuboidShape(11.9999, 3.9999000000000002, 11.9999, 13.0001, 14.0001, 13.0001),
            Block.createCuboidShape(12.9999, 4.9999, 11.9999, 14.0001, 13.0001, 13.0001),
            Block.createCuboidShape(4.9999, 1.9999000000000002, 11.9999, 11.0001, 16.0001, 13.0001),
            Block.createCuboidShape(0.9999000000000002, 5.9999, 11.9999, 2.0000999999999998, 12.0001, 13.0001),
            Block.createCuboidShape(13.9999, 5.9999, 2.9999000000000002, 15.0001, 12.0001, 4.0001),
            Block.createCuboidShape(1.9999000000000002, 4.9999, 2.9999000000000002, 3.0000999999999998, 13.0001, 4.0001),
            Block.createCuboidShape(2.9999000000000002, 3.9999000000000002, 2.9999000000000002, 4.0001, 14.0001, 4.0001),
            Block.createCuboidShape(3.9999000000000002, 2.9999000000000002, 2.9999000000000002, 5.0001, 15.0001, 4.0001),
            Block.createCuboidShape(10.9999, 2.9999000000000002, 2.9999000000000002, 12.0001, 15.0001, 4.0001),
            Block.createCuboidShape(11.9999, 3.9999000000000002, 2.9999000000000002, 13.0001, 14.0001, 4.0001),
            Block.createCuboidShape(12.9999, 4.9999, 2.9999000000000002, 14.0001, 13.0001, 4.0001),
            Block.createCuboidShape(4.9999, 1.9999000000000002, 2.9999000000000002, 11.0001, 16.0001, 4.0001),
            Block.createCuboidShape(0.9999000000000002, 5.9999, 2.9999000000000002, 2.0000999999999998, 12.0001, 4.0001),
            Block.createCuboidShape(5, 2, 0, 11, 3, 16),
            Block.createCuboidShape(4, 3, 0, 12, 4, 16),
            Block.createCuboidShape(3, 4, 0, 13, 5, 16),
            Block.createCuboidShape(2, 5, 0, 14, 6, 16),
            Block.createCuboidShape(1, 6, 0, 15, 7, 16),
            Block.createCuboidShape(1, 7, 0, 15, 8, 16),
            Block.createCuboidShape(1, 8, 0, 15, 9, 16),
            Block.createCuboidShape(1, 9, 0, 15, 10, 16),
            Block.createCuboidShape(1, 10, 0, 15, 11, 16),
            Block.createCuboidShape(1, 11, 0, 15, 12, 16),
            Block.createCuboidShape(2, 12, 0, 14, 13, 16),
            Block.createCuboidShape(3, 13, 0, 13, 14, 16),
            Block.createCuboidShape(4, 14, 0, 12, 15, 16),
            Block.createCuboidShape(5, 15, 0, 11, 16, 16)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    public static final VoxelShape SHAPE_E = Stream.of(
                Block.createCuboidShape(1, 1, 0, 15, 2, 1),
                Block.createCuboidShape(1, 1, 15, 15, 2, 16),
                Block.createCuboidShape(0, 1, 0, 1, 3, 16),
                Block.createCuboidShape(0, 0, 10, 1, 1, 16),
                Block.createCuboidShape(0, 0.5, 6, 1, 1, 10),
                Block.createCuboidShape(0, 0, 0, 1, 1, 6),
                Block.createCuboidShape(15, 1, 0, 16, 3, 16),
                Block.createCuboidShape(15, 0, 10, 16, 1, 16),
                Block.createCuboidShape(15, 0.5, 6, 16, 1, 10),
                Block.createCuboidShape(15, 0, 0, 16, 1, 6),
                Block.createCuboidShape(18, 4.3, 7.800000000000001, 18.5, 4.7, 8.2),
                Block.createCuboidShape(16, 4, 7.5, 18, 5, 8.5),
                Block.createCuboidShape(17, 5.5, 7.100000000000001, 17.5, 5.9, 7.800000000000001),
                Block.createCuboidShape(9.900000000000002, 17, 7.4, 11.100000000000001, 17.9, 8.6),
                Block.createCuboidShape(17, 3.8, 7.800000000000001, 17.5, 6.3, 8.2),
                Block.createCuboidShape(10.100000000000001, 16, 7.6, 10.900000000000002, 17, 8.4),
                Block.createCuboidShape(-1, 6, 1, 0, 12, 2),
                Block.createCuboidShape(-1, 11, 2, 0, 13, 3),
                Block.createCuboidShape(-1, 12, 3, 0, 14, 4),
                Block.createCuboidShape(-1, 13, 4, 0, 15, 5),
                Block.createCuboidShape(-1, 14, 5, 0, 16, 6),
                Block.createCuboidShape(-1, 14, 10, 0, 16, 11),
                Block.createCuboidShape(-1, 13, 11, 0, 15, 12),
                Block.createCuboidShape(-1, 12, 12, 0, 14, 13),
                Block.createCuboidShape(-1, 11, 13, 0, 13, 14),
                Block.createCuboidShape(-1, 5, 13, 0, 7, 14),
                Block.createCuboidShape(-1, 4, 12, 0, 6, 13),
                Block.createCuboidShape(-1, 2, 10, 0, 4, 11),
                Block.createCuboidShape(-1, 3, 11, 0, 5, 12),
                Block.createCuboidShape(-1, 3, 4, 0, 5, 5),
                Block.createCuboidShape(-1, 2, 5, 0, 4, 6),
                Block.createCuboidShape(-1, 4, 3, 0, 6, 4),
                Block.createCuboidShape(-1, 5, 2, 0, 7, 3),
                Block.createCuboidShape(-1, 2, 6, 0, 3, 10),
                Block.createCuboidShape(-1, 15, 6, 0, 16, 10),
                Block.createCuboidShape(-1, 6, 14, 0, 12, 15),
                Block.createCuboidShape(16, 6, 1, 17, 12, 2),
                Block.createCuboidShape(16, 11, 2, 17, 13, 3),
                Block.createCuboidShape(16, 12, 3, 17, 14, 4),
                Block.createCuboidShape(16, 13, 4, 17, 15, 5),
                Block.createCuboidShape(16, 14, 5, 17, 16, 6),
                Block.createCuboidShape(16, 14, 10, 17, 16, 11),
                Block.createCuboidShape(16, 13, 11, 17, 15, 12),
                Block.createCuboidShape(16, 12, 12, 17, 14, 13),
                Block.createCuboidShape(16, 11, 13, 17, 13, 14),
                Block.createCuboidShape(16, 5, 13, 17, 7, 14),
                Block.createCuboidShape(16, 4, 12, 17, 6, 13),
                Block.createCuboidShape(16, 2, 10, 17, 4, 11),
                Block.createCuboidShape(16, 3, 11, 17, 5, 12),
                Block.createCuboidShape(16, 3, 4, 17, 5, 5),
                Block.createCuboidShape(16, 2, 5, 17, 4, 6),
                Block.createCuboidShape(16, 4, 3, 17, 6, 4),
                Block.createCuboidShape(16, 5, 2, 17, 7, 3),
                Block.createCuboidShape(16, 2, 6, 17, 3, 10),
                Block.createCuboidShape(16, 15, 6, 17, 16, 10),
                Block.createCuboidShape(16, 6, 14, 17, 12, 15),
                Block.createCuboidShape(11.9999, 5.9999, 0.9999000000000002, 13.0001, 12.0001, 2.0000999999999998),
                Block.createCuboidShape(11.9999, 4.9999, 12.9999, 13.0001, 13.0001, 14.0001),
                Block.createCuboidShape(11.9999, 3.9999000000000002, 11.9999, 13.0001, 14.0001, 13.0001),
                Block.createCuboidShape(11.9999, 2.9999000000000002, 10.9999, 13.0001, 15.0001, 12.0001),
                Block.createCuboidShape(11.9999, 2.9999000000000002, 3.9999000000000002, 13.0001, 15.0001, 5.0001),
                Block.createCuboidShape(11.9999, 3.9999000000000002, 2.9999000000000002, 13.0001, 14.0001, 4.0001),
                Block.createCuboidShape(11.9999, 4.9999, 1.9999000000000002, 13.0001, 13.0001, 3.0000999999999998),
                Block.createCuboidShape(11.9999, 1.9999000000000002, 4.9999, 13.0001, 16.0001, 11.0001),
                Block.createCuboidShape(11.9999, 5.9999, 13.9999, 13.0001, 12.0001, 15.0001),
                Block.createCuboidShape(2.9999000000000002, 5.9999, 0.9999000000000002, 4.0001, 12.0001, 2.0000999999999998),
                Block.createCuboidShape(2.9999000000000002, 4.9999, 12.9999, 4.0001, 13.0001, 14.0001),
                Block.createCuboidShape(2.9999000000000002, 3.9999000000000002, 11.9999, 4.0001, 14.0001, 13.0001),
                Block.createCuboidShape(2.9999000000000002, 2.9999000000000002, 10.9999, 4.0001, 15.0001, 12.0001),
                Block.createCuboidShape(2.9999000000000002, 2.9999000000000002, 3.9999000000000002, 4.0001, 15.0001, 5.0001),
                Block.createCuboidShape(2.9999000000000002, 3.9999000000000002, 2.9999000000000002, 4.0001, 14.0001, 4.0001),
                Block.createCuboidShape(2.9999000000000002, 4.9999, 1.9999000000000002, 4.0001, 13.0001, 3.0000999999999998),
                Block.createCuboidShape(2.9999000000000002, 1.9999000000000002, 4.9999, 4.0001, 16.0001, 11.0001),
                Block.createCuboidShape(2.9999000000000002, 5.9999, 13.9999, 4.0001, 12.0001, 15.0001),
                Block.createCuboidShape(0, 2, 5, 16, 3, 11),
                Block.createCuboidShape(0, 3, 4, 16, 4, 12),
                Block.createCuboidShape(0, 4, 3, 16, 5, 13),
                Block.createCuboidShape(0, 5, 2, 16, 6, 14),
                Block.createCuboidShape(0, 6, 1, 16, 7, 15),
                Block.createCuboidShape(0, 7, 1, 16, 8, 15),
                Block.createCuboidShape(0, 8, 1, 16, 9, 15),
                Block.createCuboidShape(0, 9, 1, 16, 10, 15),
                Block.createCuboidShape(0, 10, 1, 16, 11, 15),
                Block.createCuboidShape(0, 11, 1, 16, 12, 15),
                Block.createCuboidShape(0, 12, 2, 16, 13, 14),
                Block.createCuboidShape(0, 13, 3, 16, 14, 13),
                Block.createCuboidShape(0, 14, 4, 16, 15, 12),
                Block.createCuboidShape(0, 15, 5, 16, 16, 11)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch(state.get(FACING)) {
            case NORTH:
                return SHAPE_N;
            case SOUTH:
                return SHAPE_S;
            case WEST:
                return SHAPE_W;
            case EAST:
                return SHAPE_E;
            default:
                return SHAPE_N;
        }
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATER_LEVEL, LIQUID_TYPE_ENUM_PROPERTY, CORKED);
    }


    /* BLOCK ENTITY */
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) return;
        if (!state.get(CORKED)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof OakBarrelBlockEntity) {
                ItemScatterer.spawn(world, pos, (OakBarrelBlockEntity)blockEntity);
                world.updateComparators(pos, this);
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        BlockEntity blockEntity = builder.getNullable(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof OakBarrelBlockEntity oakBarrelBlockEntity) {
            builder = builder.putDrop(CONTENTS, (context, consumer) -> {
                for (int i = 0; i < oakBarrelBlockEntity.size(); ++i) {
                    consumer.accept(oakBarrelBlockEntity.getStack(i));
                }
            });
        }
        return super.getDroppedStacks(state, builder);
    }

    //    @Override
//    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
//        BlockEntity blockEntity = world.getBlockEntity(pos);
//        if (blockEntity instanceof OakBarrelBlockEntity) {
//            OakBarrelBlockEntity oakBarrelBlockEntity = (OakBarrelBlockEntity) blockEntity;
//            if (!world.isClient) {
//                player.sendMessage(Text.of(state.get(CORKED).toString()), false);
//                if (state.get(CORKED)) {
//                    NbtCompound nbtCompound;
//                    ItemStack itemStack = new ItemStack(this);
//                    nbtCompound = new NbtCompound();
//                    nbtCompound.putInt("cork", state.get(CORKED) ? 1 : 0);
//                    nbtCompound.putInt("level", state.get(WATER_LEVEL));
//                    nbtCompound.putString("liquid", state.get(LIQUID_TYPE_ENUM_PROPERTY).toString());
//                    BlockItem.setBlockEntityNbt(itemStack, ModBlockEntities.OAK_BARREL, nbtCompound);
//                    ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
//                    itemEntity.setToDefaultPickupDelay();
//                    world.spawnEntity(itemEntity);
//                } else {
//                    Block.dropStack(world, pos, new ItemStack(this));
//                }
//            }
//        }
//        super.onBreak(world, pos, state, player);
//    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient && hand == Hand.MAIN_HAND) {
            ItemStack stack = player.getStackInHand(hand);
            if (!state.get(CORKED)) {
                if (stack.isOf(Items.WATER_BUCKET)) {
                    if (state.get(LIQUID_TYPE_ENUM_PROPERTY).equals(LiquidType.WATER)) {
                        world.setBlockState(pos, state.with(WATER_LEVEL, state.get(WATER_LEVEL) + 3)
                                .with(CORKED, state.get(CORKED))
                                .with(LIQUID_TYPE_ENUM_PROPERTY, state.get(LIQUID_TYPE_ENUM_PROPERTY))
                                .with(FACING, state.get(FACING)));
                        player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
                        world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    } else if (state.get(LIQUID_TYPE_ENUM_PROPERTY).equals(LiquidType.NOTHING)) {
                        world.setBlockState(pos, state.with(LIQUID_TYPE_ENUM_PROPERTY, LiquidType.WATER)
                                .with(WATER_LEVEL, state.get(WATER_LEVEL) + 3)
                                .with(CORKED, state.get(CORKED))
                                .with(FACING, state.get(FACING)));
                        player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
                        world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    }
                    return ActionResult.SUCCESS;
                }
            }
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
            return ActionResult.CONSUME;
        }

        return ActionResult.success(world.isClient);
    }

    public void getDrink(World world, BlockPos blockPos, BlockState state) {
        LiquidType st;
        int i = state.get(WATER_LEVEL) - 1;
        if (i == 0) {
            st = LiquidType.NOTHING;
        } else {
            st = state.get(LIQUID_TYPE_ENUM_PROPERTY);
        }
        world.setBlockState(blockPos, state.with(LIQUID_TYPE_ENUM_PROPERTY, st)
                .with(WATER_LEVEL, i)
                .with(FACING, state.get(FACING))
                .with(CORKED, false));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new OakBarrelBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.OAK_BARREL, OakBarrelBlockEntity::tick);
    }
}
