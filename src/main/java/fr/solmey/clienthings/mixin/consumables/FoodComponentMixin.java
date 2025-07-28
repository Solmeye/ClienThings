package fr.solmey.clienthings.mixin.consumables;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Sounds;

import net.minecraft.component.type.FoodComponent;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoodComponent.class)
public class FoodComponentMixin {
    @Inject(method = "onConsume", at = @At("HEAD"))
    private void onConsume(World world, LivingEntity user, ItemStack stack, ConsumableComponent consumable, CallbackInfo info) {
        // https://bugs.mojang.com/browse/MC-188359 btw
		// https://report.bugs.mojang.com/servicedesk/customer/portal/2/MC-188359
        if (JsonConfig.config.consumables.enabled && JsonConfig.shouldWork(JsonConfig.config.consumables.servers)) {
            Random random = user.getRandom();
            world.playSound((PlayerEntity)user, user.getX(), user.getY(), user.getZ(), (SoundEvent)consumable.sound().value(), SoundCategory.NEUTRAL, 1.0F, random.nextTriangular(1.0F, 0.4F));
            Sounds.set(System.currentTimeMillis(), user.getX(), user.getY(), user.getZ(), (SoundEvent)consumable.sound().value(), Sounds.CONSUMABLES);

            //playerEntity.getHungerManager().eat(this);
            world.playSound((PlayerEntity)user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, MathHelper.nextBetween(random, 0.9F, 1.0F));
            Sounds.set(System.currentTimeMillis(), user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_BURP, Sounds.CONSUMABLES);
        }
    }
}