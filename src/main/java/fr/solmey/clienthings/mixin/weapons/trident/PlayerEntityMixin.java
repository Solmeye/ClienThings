package fr.solmey.clienthings.mixin.weapons.trident;

import fr.solmey.clienthings.config.JsonConfig;

import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import  net.minecraft.item.ItemStack;

import net.minecraft.item.CrossbowItem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Redirect(
        method = "useRiptide",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/World;isClient:Z"
        )
    )
    private boolean useRiptide(World world) {
        if (JsonConfig.config.weapons.enabled && JsonConfig.config.weapons.trident.enabled && JsonConfig.shouldWorkOnThisServer(JsonConfig.config.weapons.trident.servers) && JsonConfig.shouldWork(JsonConfig.config.weapons.servers))
            return false;
        else
            return world.isClient;
    }
}