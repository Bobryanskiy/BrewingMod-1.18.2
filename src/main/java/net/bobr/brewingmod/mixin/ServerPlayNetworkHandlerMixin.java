package net.bobr.brewingmod.mixin;

import net.bobr.brewingmod.block.custom.OakBarrelBlock;
import net.bobr.brewingmod.block.entity.OakBarrelBlockEntity;
import net.bobr.brewingmod.util.IPlayerLastClickedPosGetterSetter;
import net.minecraft.network.packet.c2s.play.CraftRequestC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin{
    @Shadow
    ServerPlayerEntity player;

    @Inject(method = "onCraftRequest(Lnet/minecraft/network/packet/c2s/play/CraftRequestC2SPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getRecipeManager()Lnet/minecraft/recipe/RecipeManager;"), cancellable = true)
    public void onCraftRequestHook (CraftRequestC2SPacket packet, CallbackInfo ci) {
        BlockPos pos = ((IPlayerLastClickedPosGetterSetter) player).getLastClickedPos();
        if (player.world.getBlockEntity(pos) instanceof OakBarrelBlockEntity) {
            if (player.world.getBlockState(pos).get(OakBarrelBlock.CORKED)) {
                ci.cancel();
            }
        }
    }
}
