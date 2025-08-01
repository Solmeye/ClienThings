package fr.solmey.clienthings.mixin.elytras;

import net.minecraft.client.MinecraftClient;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Elytras;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
class MinecraftClientMixin {

  @Inject(method = "onDisconnected", at = @At("HEAD"), cancellable = true)
  private void onDisconnected(CallbackInfo info) {
    if(JsonConfig.config.elytras.enabled && JsonConfig.shouldWork(JsonConfig.config.elytras.servers)) {
      Elytras.bypass = true;
      Elytras.bypass2 = true;
    }
  }
}