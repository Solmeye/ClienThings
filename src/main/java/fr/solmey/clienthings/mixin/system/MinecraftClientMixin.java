package fr.solmey.clienthings.mixin.system;

import net.minecraft.client.MinecraftClient;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Actions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
class MinecraftClientMixin {

  @Inject(method = "onDisconnected", at = @At("HEAD"), cancellable = true)
  private void onDisconnected(CallbackInfo info) {
    Actions.clear();
  }
}