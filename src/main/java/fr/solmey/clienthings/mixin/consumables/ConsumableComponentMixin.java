package fr.solmey.clienthings.mixin.consumables;

import net.minecraft.component.type.ConsumableComponent;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Consumables;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.item.consume.TeleportRandomlyConsumeEffect;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ConsumableComponent.class)
public class ConsumableComponentMixin {
    @Redirect(
        method = "finishConsumption",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/World;isClient:Z"
        )
    )
    private boolean finishConsumption(World world) {
        if (JsonConfig.config.consumables.enabled && JsonConfig.shouldWork(JsonConfig.config.consumables.servers))
            return false;
        else {
            return world.isClient;
        }
    }
}