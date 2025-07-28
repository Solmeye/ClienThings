package fr.solmey.clienthings.mixin.elytras;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Elytras;
import fr.solmey.clienthings.util.Actions;
import fr.solmey.clienthings.mixin.elytras.LivingEntityAccessor;

import net.minecraft.entity.LivingEntity;
import net.minecraft.client.MinecraftClient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(at = @At("TAIL"), method = "tickGliding")
    private void tickGliding(CallbackInfo info) {
        if (JsonConfig.config.elytras.enabled && JsonConfig.shouldWork(JsonConfig.config.elytras.servers) && !Elytras.bypass && !Elytras.bypass2) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;
            if(!((LivingEntityAccessor)livingEntity).invokeCanGlide())
                Actions.add(MinecraftClient.getInstance().world.getTime() + 1, Actions.STOP_FLYING);
        }
    }

    /*@Redirect(
        method = "tickGliding",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/World;isClient:Z"
        )
    )
    private boolean tickGliding(World world) {
        if (JsonConfig.config.elytras.enabled && JsonConfig.shouldWork(JsonConfig.config.elytras.servers))
            return false;
        else
            return world.isClient;
    }*/
}