package fr.solmey.clienthings.mixin.fall;

import fr.solmey.clienthings.config.JsonConfig;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import net.minecraft.client.world.ClientWorld;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	/*@Inject(at = @At("RETURN"), method = "handleFallDamage", cancellable = true)
	public boolean handleFallDamage(double fallDistance, float damagePerDistance, DamageSource damageSource, CallbackInfoReturnable<Boolean> info) {
		
		if(JsonConfig.config.falldamage && JsonConfig.shouldWork(JsonConfig.config.falldamage.servers)) {
			LivingEntity player = (LivingEntity)(Object)this;
			int i = ((LivingEntityInvoker) player).invokeComputeFallDamage(fallDistance, damagePerDistance);
			if(JsonConfig.config.debug)
				System.out.println("FALL :" + i);

			World world = player.getWorld();
				//ClientWorld clientWorld = (ServerWorld) world;
				//Damages.damage(world, damageSource, i);


			//player.damage(player.getWorld(), damageSource, i);
		}
		return info.getReturnValue();
	}*/
}

/*
@Mixin(LivingEntity.class)
interface LivingEntityInvoker {

    @Invoker("computeFallDamage")
    int invokeComputeFallDamage(double fallDistance, float damageMultiplier);
}*/