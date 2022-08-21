package net.bobr.brewingmod.item.custom;

import net.bobr.brewingmod.block.ModBlocks;
import net.bobr.brewingmod.block.custom.OakBarrelBlock;
import net.bobr.brewingmod.block.enums.LiquidType;
import net.bobr.brewingmod.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Mug extends Item {
    public Mug(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos;
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(blockPos = context.getBlockPos());
        if (!blockState.isOf(ModBlocks.OAK_BARREL) || blockState.get(OakBarrelBlock.CORKED) || blockState.get(OakBarrelBlock.LIQUID_TYPE_ENUM_PROPERTY).equals(LiquidType.NOTHING) || blockState.get(OakBarrelBlock.LIQUID_TYPE_ENUM_PROPERTY).equals(LiquidType.WATER)) {
            return ActionResult.PASS;
        }
        ItemStack itemStack = context.getStack();
        PlayerEntity player = context.getPlayer();
        if (!world.isClient) {
            player.setStackInHand(context.getHand(), ItemUsage.exchangeStack(itemStack, player, new ItemStack(switch (blockState.get(OakBarrelBlock.LIQUID_TYPE_ENUM_PROPERTY)) {
                case MEAD -> ModItems.MEAD;
                case BEER -> ModItems.BEER;
                default -> null;
            })));
            ((OakBarrelBlock)ModBlocks.OAK_BARREL).getDrink(world, blockPos, blockState);
        }
        return ActionResult.success(world.isClient);
    }
}
