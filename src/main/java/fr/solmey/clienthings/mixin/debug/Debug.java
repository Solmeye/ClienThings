package fr.solmey.clienthings.mixin.debug;

import net.minecraft.client.network.ClientPlayerEntity;

import fr.solmey.clienthings.config.JsonConfig;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class Debug {
	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info) {
		if (JsonConfig.config.debug) {
            //MinecraftClient minecraftClient = MinecraftClient.getInstance();
			//ClientPlayNetworkHandler networkHandler = minecraftClient.getNetworkHandler();
            //System.out.println(networkHandler.getBrand());
		}
	}
}