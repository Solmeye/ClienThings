package fr.solmey.clienthings.mixin.minecarts;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;

import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.world.World;
import net.minecraft.util.ActionResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecartEntity.class)
public class MinecartEntityMixin {
    @Redirect(
        method = "interact",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/World;isClient:Z"
        )
    )
    private boolean interact(World world) {
        if (JsonConfig.config.minecart.enabled && JsonConfig.config.experimental && JsonConfig.shouldWork(JsonConfig.config.minecart.servers))
            return false;
        else {
            return world.isClient;
        }
    }
}