package fr.solmey.clienthings.mixin.consumables;

import fr.solmey.clienthings.config.JsonConfig;

import net.minecraft.item.consume.ApplyEffectsConsumeEffect;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.LivingEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ApplyEffectsConsumeEffect.class)
public class ApplyEffectsConsumeEffectMixin {
    @Shadow private float probability;

    @Inject(method = "onConsume", at = @At("HEAD"), cancellable = true)
    private void onConsume(World world, ItemStack stack, LivingEntity user, CallbackInfoReturnable<Boolean> info) {
        if (JsonConfig.config.consumables.enabled && JsonConfig.shouldWork(JsonConfig.config.consumables.servers))
            if (this.probability != 1.0F)
                info.cancel();
    }
}