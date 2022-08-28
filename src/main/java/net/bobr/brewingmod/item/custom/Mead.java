package net.bobr.brewingmod.item.custom;

import net.bobr.brewingmod.item.ModItems;
import net.bobr.brewingmod.networking.ModPackets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class Mead extends AliasedBlockItem {
    private final float alc = 0.6f;

    public Mead(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 40;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        super.finishUsing(stack, world, user);
        ItemStack itemStack;
        itemStack = new ItemStack(ModItems.MEAD);
        itemStack.damage(stack.getDamage() + 1, user, p -> p.sendToolBreakStatus(user.getActiveHand()));
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        if (!world.isClient) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeFloat(this.alc);
            ClientPlayNetworking.send(ModPackets.DRUNK_ALCOHOL, buf);
        }
        if (itemStack.isEmpty()) {
            return new ItemStack(ModItems.MUG);
        }
        return itemStack;
    }

    @Override
    public SoundEvent getDrinkSound() {
        return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
    }

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
    }
}
