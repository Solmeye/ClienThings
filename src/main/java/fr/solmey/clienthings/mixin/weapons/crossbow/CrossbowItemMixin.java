package fr.solmey.clienthings.mixin.weapons.crossbow;

import fr.solmey.clienthings.config.JsonConfig;

import net.minecraft.item.CrossbowItem;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.world.World;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundCategory;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.jetbrains.annotations.Nullable;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
    @Redirect(
        method = "usageTick",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/World;isClient:Z"
        )
    )
    private boolean usageTick(World world) {
        if (JsonConfig.config.weapons.enabled && JsonConfig.config.weapons.crossbow.enabled && JsonConfig.shouldWorkOnThisServer(JsonConfig.config.weapons.crossbow.servers) && JsonConfig.shouldWork(JsonConfig.config.weapons.servers))
            return false;
        else
            return world.isClient;
    }
}