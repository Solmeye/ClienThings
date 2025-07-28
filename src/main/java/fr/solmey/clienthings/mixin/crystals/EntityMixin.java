package fr.solmey.clienthings.mixin.crystals;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;

import fr.solmey.clienthings.mixin.crystals.EntityAccessor;
import net.minecraft.util.Identifier;
import java.util.Optional;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.commons.lang3.mutable.MutableFloat;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import com.google.common.collect.Multimap;
import net.minecraft.registry.RegistryKey;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.ActionResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "sidedDamage", at = @At("TAIL"), cancellable = true)
	public final boolean sidedDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		if(JsonConfig.config.crystals.enabled && JsonConfig.shouldWork(JsonConfig.config.crystals.servers)) {
			Entity entity = (Entity) (Object) this;
			if(entity instanceof EndCrystalEntity) {
				//ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
            	ClientPlayerEntity player = MinecraftClient.getInstance().player;

				if (!((EntityAccessor) entity).invokeIsAlwaysInvulnerableTo(source) && !entity.isRemoved()) {
					if(entity.isAttackable()) {
						if(!entity.handleAttack(player)) {
							ItemStack itemStack = player.getWeaponStack();

							//WEAKNESS
							//https://minecraft.wiki/w/Weakness
							StatusEffectInstance weakness = player.getStatusEffect(StatusEffects.WEAKNESS);
							float weaknessAttack = weakness == null ? 0.0F : 4.0F * (weakness.getAmplifier() + 1);


							//STRENGHT
							//https://minecraft.wiki/w/Strength
							StatusEffectInstance strength = player.getStatusEffect(StatusEffects.STRENGTH);
							float strengthAttack = strength == null ? 0.0F : 3.0F * (strength.getAmplifier() + 1);


							//ATTACK DAMAGE
							float[] attackDamage = new float[1];
							attackDamage[0] = 0.0F;
							itemStack.applyAttributeModifiers(EquipmentSlot.MAINHAND, (attribute, modifier) -> {
								if (EntityAttributes.ATTACK_DAMAGE.equals(attribute))
									attackDamage[0] += modifier.value(); 
							});
							

							//SHARPNESS DAMAGE
							// https://minecraft.fandom.com/wiki/Sharpness
							// https://bugs.mojang.com/browse/MC/issues/MC-92734
							// https://bugs.mojang.com/browse/MC/issues/MC-295727
							float sharpnessBonus = 0;
							int sharpnessLevel = 0;
							Optional<RegistryKey<Enchantment>> sharpnessOptional = Optional.of(Enchantments.SHARPNESS);
							for (RegistryEntry<Enchantment> enchantment : itemStack.getEnchantments().getEnchantments()) {
								if (enchantment.getKey().equals(sharpnessOptional)) {
									sharpnessLevel = itemStack.getEnchantments().getLevel(enchantment);
								}
							}
							if(sharpnessLevel != 0)
								sharpnessBonus = (sharpnessLevel - 1) * 0.5F + 1;


							float f = player.isUsingRiptide() ? ((LivingEntityAccessor) player).getRiptideAttackDamage() : (float)player.getAttributeValue(EntityAttributes.ATTACK_DAMAGE); //base damage
							f += attackDamage[0] + strengthAttack - weaknessAttack;
							float g = sharpnessBonus;
							float h = Entities.attackCooldownProgress;
							f *= 0.2F + h * h * 0.8F;
							g *= h;

							if(f > 0.0F || g > 0.0F) {
								//Entities.remove(entity, Entity.RemovalReason.KILLED);  NOT HERE BECAUSE .REMOVE WILL REMOVE THE TYPE
								if (Entities.getType(entity) == Entities.FAKE) {
									Entities.remove(entity, Entity.RemovalReason.KILLED);

									Entities.set(System.currentTimeMillis(), entity, entity, Entities.TO_DESTROY);
								}
								else {
									Entities.remove(entity, Entity.RemovalReason.KILLED);
								}
							}
						}
					}
				}
			}
		}
		return info.getReturnValue();
	}
}

@Mixin(LivingEntity.class)
interface LivingEntityAccessor {
    @Accessor("riptideAttackDamage")
    float getRiptideAttackDamage();
}