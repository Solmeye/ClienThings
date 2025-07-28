package fr.solmey.clienthings.mixin.debug;

import net.minecraft.client.network.ClientPlayerEntity;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Elytras;
import fr.solmey.clienthings.mixin.elytras.EntityAccessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.Packet;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
	/*@Inject(at = @At("HEAD"), method = "tick")
	public void tick(CallbackInfo info) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		ClientPlayNetworkHandler networkHandler = minecraftClient.getNetworkHandler();
		ClientPlayerEntity playerEntity = MinecraftClient.getInstance().player;
		networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.Full(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), playerEntity.getYaw(), playerEntity.getPitch(), false, false));
	}*/
}