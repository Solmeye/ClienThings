package fr.solmey.clienthings.mixin.pose;

import net.minecraft.client.network.ClientPlayNetworkHandler;

import fr.solmey.clienthings.config.JsonConfig;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityPose;
import java.util.List;
import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    private static final int POSE_INDEX = 6;

    @ModifyVariable(
        method = "onEntityTrackerUpdate(Lnet/minecraft/network/packet/s2c/play/EntityTrackerUpdateS2CPacket;)V",
        at = @At("HEAD"),
        argsOnly = true
    )
    private EntityTrackerUpdateS2CPacket modifierPacket(EntityTrackerUpdateS2CPacket originalPacket) {
      if (JsonConfig.config.pose.enabled && JsonConfig.shouldWork(JsonConfig.config.pose.servers)) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        
        if(player != null) {
          Entity entity = player.getWorld().getEntityById(originalPacket.id());
          if(player == entity) {
            List<DataTracker.SerializedEntry<?>> newEntries = new ArrayList<>();
            for (DataTracker.SerializedEntry<?> entry : originalPacket.trackedValues()) {
              if (entry.id() == POSE_INDEX) {

                EntityPose entityPose = player.getPose();

                TrackedDataHandler<EntityPose> handler = (TrackedDataHandler<EntityPose>) entry.handler();
                DataTracker.SerializedEntry<EntityPose> newEntry = new DataTracker.SerializedEntry<>(
                    entry.id(),
                    handler,
                    entityPose
                );

                newEntries.add(newEntry);
              }
              else {
                newEntries.add(entry);
              }
            }

            EntityTrackerUpdateS2CPacket copyPacket = new EntityTrackerUpdateS2CPacket(
              originalPacket.id(),
              new ArrayList<>(newEntries)
            );
            return copyPacket;
          }
        }
      }
      return originalPacket;
    }
}