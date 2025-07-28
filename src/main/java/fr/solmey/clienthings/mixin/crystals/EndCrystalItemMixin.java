package fr.solmey.clienthings.mixin.crystals;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;

import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.client.world.ClientWorld;

import net.minecraft.util.ActionResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalItem.class)
public abstract class EndCrystalItemMixin {
    @Inject(method = "useOnBlock", at = @At("TAIL"), cancellable = true)
    private ActionResult useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
        if (JsonConfig.config.crystals.enabled && JsonConfig.shouldWork(JsonConfig.config.crystals.servers)) {
            World world = context.getWorld();
            BlockPos blockPos = context.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);
            if (!blockState.isOf(Blocks.OBSIDIAN) && !blockState.isOf(Blocks.BEDROCK)) {
                //return ActionResult.FAIL;
            } else {
                BlockPos blockPos2 = blockPos.up();
                if (!world.isAir(blockPos2)) {
                    //return ActionResult.FAIL;
                } else {
                    double d = blockPos2.getX();
                    double e = blockPos2.getY();
                    double f = blockPos2.getZ();
                    List<Entity> list = world.getOtherEntities(null, new Box(d, e, f, d + 1.0, e + 2.0, f + 1.0));
                    if (!list.isEmpty()) {
                        //return ActionResult.FAIL;
                    } else {
                        EndCrystalEntity endCrystalEntity = new EndCrystalEntity(world, d + 0.5, e, f + 0.5);
                        endCrystalEntity.setShowBottom(false);
                        EndCrystalEntity initialEndCrystalEntity = new EndCrystalEntity(world, d + 0.5, e, f + 0.5);
                        endCrystalEntity.setShowBottom(false);

                        ((ClientWorld)world).addEntity(endCrystalEntity); // Instead of spawnEntity because spawnEntity is only in ServerWorld, not in ClientWorld yk
                        Entities.set(System.currentTimeMillis(), endCrystalEntity, initialEndCrystalEntity, Entities.FAKE);
                        //world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos2);
                        

                        //context.getStack().decrement(1); //Maybe for an another optimisation ;)


                        //return ActionResult.SUCCESS;
                    }
                }
            }
        }
        return info.getReturnValue();
    }
}