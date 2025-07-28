package fr.solmey.clienthings.util;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.mixin.crystals.ClientPlayNetworkHandlerAccessor;
import fr.solmey.clienthings.mixin.crystals.MinecraftClientAccessor;
import fr.solmey.clienthings.mixin.crystals.EntityAccessor;
import fr.solmey.clienthings.mixin.firework.FireworkRocketEntityAccessor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class Entities {
    public static float attackCooldownProgress = 0.0F;
    private static long[] timestamps = new long[256];
    private static Entity[] entities = new Entity[256];
    private static Entity[] initialEntities = new Entity[256]; //DOESNT SHOULD BE ADDED CLIENT SIDE
    private static byte[] type = new byte[256];

    public static final byte FAKE = 0;          //Only client-side
    public static final byte TO_DESTROY = 1;    //To destroy as soon as possible
    public static final byte TO_CREATE = 2;     //To create as soon as possible
    public static final byte INITIAL = 3;       //The initial entity
    public static final byte UNKNOWN = 127;     // IDK

    public static final int CONSUMABLES = 0;
    public static final int FIREWORK = 1;
    public static final int WEAPONS = 2;

    public static void set(long _timestamp, Entity _Entity, Entity _initialEntity, byte _type) {
        int cursor = 0;
        clear();
        for(int i = 0; i < 256 ; i++)
            if(timestamps[i] == 0)
                cursor = i;

        timestamps[cursor] = _timestamp;
        entities[cursor] = _Entity;
        initialEntities[cursor] = _initialEntity;
        type[cursor] = _type;
    }

    public static void clear() {
        for(int i = 0; i < 256 ; i++) {
            if(entities[i] != null) {
                if(entities[i].getType() == EntityType.END_CRYSTAL) {
                    if((System.currentTimeMillis() - timestamps[i] >= JsonConfig.config.crystals.maxTime && timestamps[i] != 0)) {
                        remove(entities[i], Entity.RemovalReason.DISCARDED);
                    }
                }
                else if (entities[i].getType() == EntityType.FIREWORK_ROCKET) {
                    if(System.currentTimeMillis() - timestamps[i] >= JsonConfig.config.firework.maxTime && timestamps[i] != 0)
                        remove(entities[i], Entity.RemovalReason.DISCARDED);
                }
                else if (entities[i].getType() == EntityType.TRIDENT) {
                    if(System.currentTimeMillis() - timestamps[i] >= JsonConfig.config.weapons.maxTime && timestamps[i] != 0)
                        remove(entities[i], Entity.RemovalReason.DISCARDED);
                }
                else if (entities[i] instanceof AbstractMinecartEntity) {
                    if(System.currentTimeMillis() - timestamps[i] >= JsonConfig.config.minecart.maxTime && timestamps[i] != 0)
                        remove(entities[i], Entity.RemovalReason.DISCARDED);
                }
                else {
                    if(System.currentTimeMillis() - timestamps[i] >= 5000 && timestamps[i] != 0)
                        remove(entities[i], Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    public static void remove(Entity entity, Entity.RemovalReason reason) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        ClientWorld clientWorld = (ClientWorld) player.getWorld();

        clientWorld.removeEntity(entity.getId(), reason);

        for (int i = 0; i < entities.length; i++) {
            if(entities[i] == entity) {
                timestamps[i] = 0;
                entities[i] = null;
                initialEntities[i] = null;
                type[i] = UNKNOWN;
            }
        }
    }

    public static byte getType(Entity entity) {
        for (int i = 0; i < entities.length; i++)
            if(entities[i] == entity)
                return (type[i]);
        return UNKNOWN;
    }

    public static boolean needToCancel(EntitySpawnS2CPacket packet) {
        clear();
        int cursor = 0;
        boolean needed = false;
        int cursorFake = 0;
        int cursorToDestroy = 0;
        double distance = 0;

        for(int i = 0; i < 256 ; i++)
            if(timestamps[i] >= timestamps[cursor])
                cursor = i;
        for(int i = 0; i < 256 ; i++) {
            if(initialEntities[i] != null) {
                if(packet.getEntityType() == initialEntities[i].getType()
                && initialEntities[i].getX() == packet.getX()
                && initialEntities[i].getY() == packet.getY()
                && initialEntities[i].getZ() == packet.getZ()
                && packet.getEntityType() == EntityType.TRIDENT ? true : packet.getYaw() == initialEntities[i].getYaw()
                && packet.getEntityType() == EntityType.TRIDENT ? true : packet.getPitch() == initialEntities[i].getPitch()
                && packet.getHeadYaw() == initialEntities[i].getHeadYaw()
                && timestamps[i] <= timestamps[cursor] && timestamps[i] != 0) {
                    cursor = i;
                    needed = true;

                    if(type[i] == FAKE)
                        cursorFake = cursor;
                    else if(type[i] == TO_DESTROY)
                        cursorToDestroy = cursor;
                }
            }
        }
        if(needed == true) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
			ClientPlayNetworkHandler networkHandler = minecraftClient.getNetworkHandler();
            ClientPlayerEntity player = minecraftClient.player;
        	ClientWorld clientWorld = (ClientWorld) player.getWorld();

            Entity newEntity = ((ClientPlayNetworkHandlerAccessor) networkHandler).invokeCreateEntity(packet);
            newEntity.onSpawnPacket(packet);
            if(newEntity instanceof EndCrystalEntity && entities[cursor] instanceof EndCrystalEntity)
                ((EndCrystalEntity)newEntity).endCrystalAge = ((EndCrystalEntity)entities[cursor]).endCrystalAge;
            if(newEntity instanceof FireworkRocketEntity && entities[cursor] instanceof FireworkRocketEntity) {
                ((FireworkRocketEntityAccessor)newEntity).setLife(((FireworkRocketEntityAccessor)entities[cursor]).getLife());
            }

            if(type[cursor] == FAKE) { //getType()
                clientWorld.addEntity(newEntity);
                remove(entities[cursor], Entity.RemovalReason.DISCARDED);
            }
            else if (type[cursor] == TO_DESTROY) {
                clientWorld.addEntity(newEntity);
                Entity[] removedFAKES = new Entity[256];

                for (int i = 0; i < entities.length; i++) {
                    if(getType(entities[i]) == FAKE && entities[i] != null) {
                        removedFAKES[i] = entities[i];
                        entities[i].discard();
                    }
                    else {
                        removedFAKES[i] = null;
                    }
                }
                //JsonConfig.config.crystals.enabled && JsonConfig.shouldWork(JsonConfig.config.crystals.servers)
                minecraftClient.gameRenderer.updateCrosshairTarget(1.0F);
                if(JsonConfig.config.crystals.autoDestroy.enabled && JsonConfig.shouldWorkOnThisServer(JsonConfig.config.crystals.autoDestroy.servers)) {
                    boolean bypass = JsonConfig.config.crystals.autoDestroy.bypassRequiredAiming.enabled && JsonConfig.shouldWorkOnThisServer(JsonConfig.config.crystals.autoDestroy.bypassRequiredAiming.servers);
                    if(!player.isSpectator()) {
                        if (!player.isUsingItem() || bypass) {
                            if ((((MinecraftClientAccessor)minecraftClient).getAttackCooldown() <= 0 && minecraftClient.crosshairTarget != null && !minecraftClient.player.isRiding()) || bypass) {
                                ItemStack itemStack = minecraftClient.player.getStackInHand(Hand.MAIN_HAND);
                                if (itemStack.isItemEnabled(minecraftClient.world.getEnabledFeatures())) {
                                    if ((minecraftClient.crosshairTarget.getType() == HitResult.Type.ENTITY && ((EntityHitResult)minecraftClient.crosshairTarget).getEntity() == newEntity) || bypass) {
                                        // Vanilla server-side check
                                        Box box = newEntity.getBoundingBox();
                                        if(clientWorld.getWorldBorder().contains(newEntity.getBlockPos()) && player.canInteractWithEntityIn(box, 3.0D)) {
                                            minecraftClient.interactionManager.attackEntity(minecraftClient.player, newEntity);
                                            player.swingHand(Hand.MAIN_HAND);

                                            if(newEntity.isRemoved()) {
                                                remove(newEntity, Entity.RemovalReason.KILLED);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if(newEntity.isRemoved()) {
                    remove(entities[cursor], Entity.RemovalReason.KILLED);
                }
                else {
                    remove(entities[cursor], Entity.RemovalReason.DISCARDED);
                    //((EntityAccessor)newEntity).setRemovalReason(null);
                    //clientWorld.addEntity(newEntity);
                }

                for (int i = 0; i < removedFAKES.length; i++) {
                    if(removedFAKES[i] != null) {
                        ((EntityAccessor)removedFAKES[i]).setRemovalReason(null);
                        clientWorld.addEntity(removedFAKES[i]);
                    }
                }

                if(!newEntity.isRemoved() && cursorFake != 0)
                    remove(entities[cursorFake], Entity.RemovalReason.DISCARDED);
            }
        }

        return needed;
    }
}