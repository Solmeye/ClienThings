package fr.solmey.clienthings.mixin.crystals;


import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("removalReason")
    void setRemovalReason(Entity.RemovalReason removalReason);

    @Invoker("isAlwaysInvulnerableTo")
    boolean invokeIsAlwaysInvulnerableTo(DamageSource source);
}
