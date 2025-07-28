package fr.solmey.clienthings.mixin.consumables;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Sounds;

import net.minecraft.item.consume.PlaySoundConsumeEffect;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.registry.entry.RegistryEntry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlaySoundConsumeEffect.class)
public class PlaySoundConsumeEffectMixin {
    @Shadow
    @Final
    private RegistryEntry<SoundEvent> sound;

    @Inject(method = "onConsume", at = @At("HEAD"))
    private void onConsume(World world, ItemStack stack, LivingEntity user, CallbackInfoReturnable<Boolean> info) {
        if (JsonConfig.config.consumables.enabled && JsonConfig.shouldWork(JsonConfig.config.consumables.servers)) {
            world.playSound(user, user.getBlockPos(), (SoundEvent)this.sound.value(), user.getSoundCategory(), 1.0F, 1.0F);
            Sounds.set(System.currentTimeMillis(), user.getBlockPos().getX(), user.getBlockPos().getY(), user.getBlockPos().getZ(), (SoundEvent)this.sound.value(), Sounds.CONSUMABLES);
        }
    }
}