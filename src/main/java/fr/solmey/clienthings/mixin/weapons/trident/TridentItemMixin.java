package fr.solmey.clienthings.mixin.weapons.trident;

import net.minecraft.item.TridentItem;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Sounds;
import fr.solmey.clienthings.util.Entities;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.entity.LivingEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.random.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
public class TridentItemMixin {
	@Inject(at = @At("HEAD"), method = "onStoppedUsing")
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> info) {
		if (JsonConfig.config.weapons.enabled && JsonConfig.config.weapons.trident.enabled && JsonConfig.shouldWorkOnThisServer(JsonConfig.config.weapons.trident.servers) && JsonConfig.shouldWork(JsonConfig.config.weapons.servers)) {
			TridentItem tridentItem = (TridentItem) (Object) this;
			
			PlayerEntity playerEntity;
			playerEntity = (PlayerEntity)user;

			int i = tridentItem.getMaxUseTime(stack, user) - remainingUseTicks;
			if (user instanceof PlayerEntity && i >= 10 ) {
				playerEntity = (PlayerEntity)user;

				float f = EnchantmentHelper.getTridentSpinAttackStrength(stack, (LivingEntity)playerEntity);
				if ((f <= 0.0F || playerEntity.isTouchingWaterOrRain()) && !stack.willBreakNextUse()) {

					RegistryEntry<SoundEvent> registryEntry = EnchantmentHelper.getEffect(stack, EnchantmentEffectComponentTypes.TRIDENT_SOUND).orElse(SoundEvents.ITEM_TRIDENT_THROW);
					Random threadSafeRandom = Random.createThreadSafe();
					ClientWorld clientWorld = ((ClientWorld)world);

					//stack.damage(1, playerEntity);
					if (f == 0.0F) {
						ItemStack itemStack = stack.splitUnlessCreative(1, (LivingEntity)playerEntity);

						TridentEntity tridentEntity = new TridentEntity(((ClientWorld)world), (LivingEntity)playerEntity, itemStack);
						tridentEntity.setVelocity((Entity)playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2.5F, 1.0F);
						TridentEntity initialTridentEntity = new TridentEntity(((ClientWorld)world), (LivingEntity)playerEntity, itemStack);
						initialTridentEntity.setVelocity((Entity)playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2.5F, 1.0F);
						clientWorld.addEntity(tridentEntity);
						
						Entities.set(System.currentTimeMillis(), tridentEntity, initialTridentEntity, Entities.FAKE);


						if (playerEntity.isInCreativeMode()) {
							tridentEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
						}
						
						clientWorld.playSoundFromEntity(playerEntity, tridentEntity, Registries.SOUND_EVENT.getEntry((SoundEvent)registryEntry.value()), SoundCategory.PLAYERS, 1.0F, 1.0F, threadSafeRandom.nextLong());
						Sounds.set(System.currentTimeMillis(), playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), registryEntry.value(), Sounds.WEAPONS);
					}
					else if(f > 0) {
						clientWorld.playSoundFromEntity(playerEntity, playerEntity, Registries.SOUND_EVENT.getEntry((SoundEvent)registryEntry.value()), SoundCategory.PLAYERS, 1.0F, 1.0F, threadSafeRandom.nextLong());
						Sounds.set(System.currentTimeMillis(), playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), registryEntry.value(), Sounds.WEAPONS);
					}
				}
			}
		}
	}
}