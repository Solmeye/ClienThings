package fr.solmey.clienthings.mixin.minecarts;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;

import net.minecraft.item.MinecartItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ItemStack;


import java.util.List;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.client.world.ClientWorld;

import net.minecraft.util.ActionResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecartItem.class)
public abstract class MinecartItemMixin {
    @Shadow
    @Final
    private EntityType<? extends AbstractMinecartEntity> type;

    @Inject(method = "useOnBlock", at = @At("TAIL"), cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
        if (JsonConfig.config.minecart.enabled && JsonConfig.shouldWork(JsonConfig.config.minecart.servers)) {
            if(info.getReturnValue() == ActionResult.SUCCESS) {
                ClientWorld clientWorld = (ClientWorld) context.getWorld();
                World world = context.getWorld();
                BlockPos blockPos = context.getBlockPos();
                BlockState blockState = world.getBlockState(blockPos);
                ItemStack itemStack = context.getStack();
                RailShape railShape = (blockState.getBlock() instanceof AbstractRailBlock) ? (RailShape)blockState.get(((AbstractRailBlock)blockState.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
                double d = 0.0D;
                if (railShape.isAscending()) {
                    d = 0.5D;
                }
                Vec3d vec3d = new Vec3d(blockPos.getX() + 0.5D, blockPos.getY() + 0.0625D + d, blockPos.getZ() + 0.5D);
                AbstractMinecartEntity abstractMinecartEntity = AbstractMinecartEntity.create(world, vec3d.x, vec3d.y, vec3d.z, this.type, SpawnReason.DISPENSER, itemStack, context.getPlayer());
                AbstractMinecartEntity initialAbstractMinecartEntity = AbstractMinecartEntity.create(world, vec3d.x, vec3d.y, vec3d.z, this.type, SpawnReason.DISPENSER, itemStack, context.getPlayer());
                
                clientWorld.addEntity((Entity)abstractMinecartEntity);
                Entities.set(System.currentTimeMillis(), abstractMinecartEntity, initialAbstractMinecartEntity, Entities.FAKE);
            }
        }
    }
}