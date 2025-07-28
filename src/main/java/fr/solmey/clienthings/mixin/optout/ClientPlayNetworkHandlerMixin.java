package fr.solmey.clienthings.mixin.optout;

import net.minecraft.client.network.ClientPlayNetworkHandler;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.ClienThings;

import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.UnknownCustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

  @Inject(method = "onGameJoin", at = @At("HEAD"), cancellable = true)
  private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo info) {

    if (JsonConfig.config.optout) {
      ClientPlayNetworkHandler networkHandler = (ClientPlayNetworkHandler)(Object) this;
      Identifier id = Identifier.of(ClienThings.MOD_ID, "optout");
      UnknownCustomPayload payload = new UnknownCustomPayload(id);
      networkHandler.sendPacket(new CustomPayloadC2SPacket(payload));
    }
  }
}