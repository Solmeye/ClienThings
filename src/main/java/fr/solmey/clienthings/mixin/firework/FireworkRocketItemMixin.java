package fr.solmey.clienthings.mixin.firework;

import net.minecraft.item.FireworkRocketItem;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;

import fr.solmey.clienthings.util.Sounds;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.util.Hand;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;

@Mixin(FireworkRocketItem.class)
class FireworkRocketItemMixin {

  @Inject(method = "use", at = @At("HEAD"), cancellable = true)
  private ActionResult use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> info) {
    if(JsonConfig.config.firework.enabled && JsonConfig.shouldWork(JsonConfig.config.firework.servers)) {
      if (user.isGliding()) {
        ItemStack itemStack = user.getStackInHand(hand);
        ClientWorld clientWorld = (ClientWorld) user.getWorld();
        ProjectileEntity firework = new FireworkRocketEntity(world, itemStack, user);
        ProjectileEntity initialFirework = new FireworkRocketEntity(world, itemStack, user);
        FireworkRocketEntity fireworkEntity = (FireworkRocketEntity) firework;

        clientWorld.addEntity(firework);
        Entities.set(System.currentTimeMillis(), firework, initialFirework, Entities.FAKE); //For cancel the EntitySpawnS2CPacket
                
        fireworkEntity.getWorld().playSound(user, fireworkEntity.getX(), fireworkEntity.getY(), fireworkEntity.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
        Sounds.set(System.currentTimeMillis(), fireworkEntity.getX(), fireworkEntity.getY(), fireworkEntity.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, Sounds.FIREWORK);

        itemStack.decrementUnlessCreative(1, user);

        return ActionResult.SUCCESS;
		  }
      else {
			  return ActionResult.PASS;
		  }
    }
    return info.getReturnValue();
  }
}