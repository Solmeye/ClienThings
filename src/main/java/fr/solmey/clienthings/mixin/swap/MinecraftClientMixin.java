package fr.solmey.clienthings.mixin.swap;

import net.minecraft.client.MinecraftClient;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Actions;
import fr.solmey.clienthings.util.Swap;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
  @Inject(method = "handleInputEvents()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getNetworkHandler()Lnet/minecraft/client/network/ClientPlayNetworkHandler;", shift = At.Shift.BEFORE)
  )
  public void handleInputEvents(CallbackInfo info) {
    if(JsonConfig.config.swap.enabled && JsonConfig.config.experimental && JsonConfig.shouldWork(JsonConfig.config.swap.servers)) {
      PlayerEntity playerEntity = MinecraftClient.getInstance().player;
      Swap.mainHand = playerEntity.getMainHandStack();
      Swap.offHand = playerEntity.getOffHandStack();
      Actions.add(MinecraftClient.getInstance().world.getTime() + 1, Actions.SWAP_ITEM_WITH_OFFHAND);
    }
  }
}