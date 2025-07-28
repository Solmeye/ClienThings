package fr.solmey.clienthings.mixin.system;

import net.minecraft.client.network.ClientPlayerEntity;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import fr.solmey.clienthings.util.Actions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientPlayerEntity.class, priority = 1)
public class ClientPlayerEntityMixin {
	@Inject(at = @At("HEAD"), method = "tick")
	public void tick(CallbackInfo info) {
		Entities.clear();
		Actions.executePending();
	}
}