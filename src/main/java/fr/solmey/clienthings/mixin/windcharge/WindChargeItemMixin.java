package fr.solmey.clienthings.mixin.windcharge;

import net.minecraft.item.WindChargeItem;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import fr.solmey.clienthings.util.Sounds;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WindChargeItem.class)
public class WindChargeItemMixin {
	@Inject(at = @At("HEAD"), method = "use")
	public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> info) {
		if (JsonConfig.config.windcharge.enabled && JsonConfig.shouldWork(JsonConfig.config.windcharge.servers)) {
			ClientWorld clientWorld = (ClientWorld) user.getWorld();

			WindChargeEntity windChargeEntity = new WindChargeEntity(user, world, user.getPos().getX(), user.getEyePos().getY(), user.getPos().getZ());
			windChargeEntity.setVelocity((Entity)user, user.getPitch(), user.getYaw(), 0.0F, WindChargeItem.POWER, 0.0F); //0.0F is divergence
			WindChargeEntity initialwindChargeEntity = new WindChargeEntity(user, world, user.getPos().getX(), user.getEyePos().getY(), user.getPos().getZ());
			initialwindChargeEntity.setVelocity((Entity)user, user.getPitch(), user.getYaw(), 0.0F, WindChargeItem.POWER, 1.0F);
			
			clientWorld.addEntity(windChargeEntity);
			Entities.set(System.currentTimeMillis(), windChargeEntity, initialwindChargeEntity, Entities.FAKE);

			clientWorld.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_WIND_CHARGE_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			Sounds.set(System.currentTimeMillis(), user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_WIND_CHARGE_THROW, Sounds.WINDCHARGE);
		}
	}
}