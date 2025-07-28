package fr.solmey.clienthings.mixin.debug;

import net.minecraft.client.network.ClientPlayNetworkHandler;

import fr.solmey.clienthings.config.JsonConfig;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
class ClientPlayNetworkHandlerMixinGameJoin {

  /*@Inject(method = "onGameJoin", at = @At("HEAD"), cancellable = true)
  private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo info) {
    if(JsonConfig.config.enabled) {
      MinecraftClient minecraftClient = MinecraftClient.getInstance();
			ClientPlayNetworkHandler networkHandler = (ClientPlayNetworkHandler) (Object) this;
      ServerType.saveType(networkHandler.getBrand());
    }
  }*/
}