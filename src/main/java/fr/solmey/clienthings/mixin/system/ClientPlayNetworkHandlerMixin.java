package fr.solmey.clienthings.mixin.system;

import net.minecraft.client.network.ClientPlayNetworkHandler;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import fr.solmey.clienthings.util.Sounds;
import fr.solmey.clienthings.util.Consumables;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundFromEntityS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.network.packet.s2c.query.PingResultS2CPacket;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@Inject(method = "onEntitySpawn", at = @At("HEAD"), cancellable = true)
  public void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo info) {
    if(Entities.needToCancel(packet))
      info.cancel();
  }

  @Inject(method = "onPlaySound", at = @At("HEAD"), cancellable = true)
  public void onPlaySound(PlaySoundS2CPacket packet, CallbackInfo info) {
    if(Sounds.needToCancel(packet.getSound().value(), packet.getX(), packet.getY(), packet.getZ()))
      info.cancel();
  }

	@Inject(method = "onPlaySoundFromEntity", at = @At("HEAD"), cancellable = true)
  public void onPlaySoundFromEntity(PlaySoundFromEntityS2CPacket packet, CallbackInfo info) {
    ClientPlayerEntity playerEntity = MinecraftClient.getInstance().player;
    if (Sounds.needToCancel(packet.getSound().value(), playerEntity.getX(), playerEntity.getY(), playerEntity.getZ())) {
      info.cancel();
    }
  }

	@Inject(method = "onEntityStatus", at = @At("HEAD"), cancellable = true)
  public void onEntityStatus(EntityStatusS2CPacket packet, CallbackInfo info) {
    if(packet.getStatus() == EntityStatuses.CONSUME_ITEM && Consumables.needToCancel()) {
      info.cancel();
    }
  }

	//@Inject(method = "onPingResult", at = @At("HEAD"), cancellable = true)
  //public void onPingResult(PingResultS2CPacket packet, CallbackInfo info) {
  //}

  /*@Inject(method = "onEntitiesDestroy", at = @At("HEAD"), cancellable = true)
  private void onEntitiesDestroy(EntitiesDestroyS2CPacket packet, CallbackInfo info) {
    info.cancel();
  }

	@Inject(method = "onExplosion", at = @At("HEAD"), cancellable = true)
  private void onExplosion(ExplosionS2CPacket packet, CallbackInfo info) {
    info.cancel();
  } */
}