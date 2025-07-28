package fr.solmey.clienthings.mixin.consumables;

import net.minecraft.client.network.ClientPlayerEntity;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Consumables;
import fr.solmey.clienthings.util.Sounds;

import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.Consumable;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.consume.TeleportRandomlyConsumeEffect;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.item.consume.PlaySoundConsumeEffect;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.MathHelper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(at = @At("TAIL"), method = "consumeItem")
	public void consumeItem(CallbackInfo info) {
		Consumables.set(System.currentTimeMillis());
	}

    @Redirect(
        method = "tickItemStackUsage",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/World;isClient:Z"
        )
    )
    private boolean tickItemStackUsage(World world) {
        if (JsonConfig.config.consumables.enabled && JsonConfig.shouldWork(JsonConfig.config.consumables.servers))
            return false;
        else {
            return world.isClient;
        }
    }

    @Redirect(
        method = "clearActiveItem",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/World;isClient:Z"
        )
    )
    private boolean clearActiveItem(World world) {
        if (JsonConfig.config.consumables.enabled && JsonConfig.shouldWork(JsonConfig.config.consumables.servers))
            return false;
        else {
            return world.isClient;
        }
    }
}