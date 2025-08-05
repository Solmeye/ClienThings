package fr.solmey.clienthings.util;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.mixin.crystals.ClientPlayNetworkHandlerAccessor;
import fr.solmey.clienthings.mixin.crystals.MinecraftClientAccessor;
import fr.solmey.clienthings.mixin.crystals.EntityAccessor;
import fr.solmey.clienthings.mixin.firework.FireworkRocketEntityAccessor;

import net.minecraft.entity.projectile.AbstractWindChargeEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
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
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
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
    public static final byte NOT_SYNCHRONISE = 4;       //Pls dont sync the position so quick :(
    public static final byte UNKNOWN = 127;     // IDK

    public static final int CONSUMABLES = 0;
    public static final int FIREWORK = 1;
    public static final int WEAPONS = 2;
    public static final int WINDCHARGE = 3;

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
                        if(type[i] != NOT_SYNCHRONISE)
                            remove(entities[i], Entity.RemovalReason.DISCARDED);
                        else
                            remove(entities[i], null);
                    }
                }
                else if (entities[i].getType() == EntityType.FIREWORK_ROCKET) {
                    if(System.currentTimeMillis() - timestamps[i] >= JsonConfig.config.firework.maxTime && timestamps[i] != 0) {
                        if(type[i] != NOT_SYNCHRONISE)
                            remove(entities[i], Entity.RemovalReason.DISCARDED);
                        else
                            remove(entities[i], null);
                    }
                }
                else if (entities[i].getType() == EntityType.TRIDENT) {
                    if(System.currentTimeMillis() - timestamps[i] >= JsonConfig.config.weapons.maxTime && timestamps[i] != 0) {
                        if(type[i] != NOT_SYNCHRONISE)
                            remove(entities[i], Entity.RemovalReason.DISCARDED);
                        else
                            remove(entities[i], null);
                    }
                }
                else if (entities[i] instanceof AbstractMinecartEntity) {
                    if(System.currentTimeMillis() - timestamps[i] >= JsonConfig.config.minecart.maxTime && timestamps[i] != 0) {
                        if(type[i] != NOT_SYNCHRONISE)
                            remove(entities[i], Entity.RemovalReason.DISCARDED);
                        else
                            remove(entities[i], null);
                    }
                }
                else if (entities[i].getType() == EntityType.WIND_CHARGE) {
                    if(System.currentTimeMillis() - timestamps[i] >= JsonConfig.config.windcharge.maxTime && timestamps[i] != 0) {
                        if(type[i] != NOT_SYNCHRONISE)
                            remove(entities[i], Entity.RemovalReason.DISCARDED);
                        else
                            remove(entities[i], null);
                    }
                }
                else {
                    if(System.currentTimeMillis() - timestamps[i] >= 5000 && timestamps[i] != 0)
                        if(type[i] != NOT_SYNCHRONISE)
                            remove(entities[i], Entity.RemovalReason.DISCARDED);
                        else
                            remove(entities[i], null);
                }
            }
        }
    }

    public static void remove(Entity entity, Entity.RemovalReason reason) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        ClientWorld clientWorld = (ClientWorld) player.getWorld();

        if(reason != null)
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

    public static boolean has(Entity entity, byte _type) {
        for (int i = 0; i < entities.length; i++)
            if(entities[i] == entity && type[i] == _type)
                return true;
        return false;
    }

    public static byte getType(Entity entity) {
        for (int i = 0; i < entities.length; i++)
            if(entities[i] == entity)
                return (type[i]);
        return UNKNOWN;
    }

    public static boolean needToCancel(EntityS2CPacket packet) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if(minecraftClient != null) {
            ClientPlayerEntity player = minecraftClient.player;
            if(player != null) {
                ClientWorld clientWorld = (ClientWorld) player.getWorld();
                if(clientWorld != null) {
                    Entity entity = packet.getEntity(clientWorld);
                    if(entity != null && !entity.isLogicalSideForUpdatingMovement() && packet.isPositionChanged() && has(entity, NOT_SYNCHRONISE)) {
                        Vec3d vec3d = new Vec3d(packet.getDeltaX(), packet.getDeltaY(), packet.getDeltaZ());

                        double maxDistance;
                        if(entity.getType() == EntityType.END_CRYSTAL)
                            maxDistance = 0;
                        else if (entity.getType() == EntityType.FIREWORK_ROCKET)
                            maxDistance = JsonConfig.config.firework.maxDistance;
                        else if (entity.getType() == EntityType.TRIDENT)
                            maxDistance = JsonConfig.config.weapons.maxDistance;
                        else if (entity instanceof AbstractMinecartEntity)
                            maxDistance = 0;
                        else if (entity.getType() == EntityType.WIND_CHARGE)
                            maxDistance = JsonConfig.config.windcharge.maxDistance;
                        else
                            maxDistance = 0;

                        if(vec3d.length() <= maxDistance)
                            return true;
                        else
                            remove(entity, null);
                    }
                }
            }
        }
        return false;
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
                if(JsonConfig.config.debug) {
                    System.out.println("DEBUG util.Entities.java at 199");
                    System.out.println("Type : " + type[i]);
                    System.out.println("TIMESTAMP : " + System.currentTimeMillis());
                    System.out.println("PACKET X : " + packet.getX());
                    System.out.println("PACKET Y : " + packet.getY());
                    System.out.println("PACKET Z : " + packet.getZ());
                    System.out.println("PACKET getEntityType : " + packet.getEntityType());
                    System.out.println("PACKET getHeadYaw : " + packet.getHeadYaw());
                    System.out.println("LOCAL Timestamp : " + timestamps[i]);
                    System.out.println("LOCAL X : " + initialEntities[i].getX());
                    System.out.println("LOCAL Y : " + initialEntities[i].getY());
                    System.out.println("LOCAL Z : " + initialEntities[i].getZ());
                    System.out.println("LOCAL getType : " + initialEntities[i].getType());
                    System.out.println("LOCAL getHeadYaw : " + initialEntities[i].getHeadYaw());
                    System.out.println();
                }

                boolean bl1 = packet.getPitch() == initialEntities[i].getPitch();
                boolean bl2 = packet.getYaw() == initialEntities[i].getYaw();
                boolean bl3 = bl1 && bl2;

                if(packet.getEntityType() == initialEntities[i].getType()
                && initialEntities[i].getX() == packet.getX()
                && initialEntities[i].getY() == packet.getY()
                && initialEntities[i].getZ() == packet.getZ()
                && ((packet.getEntityType() == EntityType.TRIDENT) || (packet.getEntityType() == EntityType.WIND_CHARGE)) ? true : bl3
                && packet.getHeadYaw() == initialEntities[i].getHeadYaw()
                && type[i] != NOT_SYNCHRONISE
                && timestamps[i] <= timestamps[cursor] && timestamps[i] != 0) {
                    if(JsonConfig.config.debug) {
                        System.out.println("DEBUG util.Entities.java at 229");
                        System.out.println("Cancel needed");
                        System.out.println();
                    }
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

            if(type[cursor] == FAKE) {
                clientWorld.addEntity(newEntity);

                while (newEntity.age++ < entities[cursor].age && entities[cursor] != null) {
                    if(JsonConfig.config.debug) {
                        System.out.println("DEBUG util.Entities.java at 257");
                        System.out.println("Age : " +     newEntity.age);
                        System.out.println("Position : " +  newEntity.getPos());
                        System.out.println("Velocity : " +  newEntity.getVelocity());
                        System.out.println();
                    }
                    if(newEntity.getType() == EntityType.END_CRYSTAL)
                        ((EndCrystalEntity)newEntity).endCrystalAge = ((EndCrystalEntity)entities[cursor]).endCrystalAge;
                    else if (newEntity.getType() == EntityType.FIREWORK_ROCKET)
                        ((FireworkRocketEntityAccessor)newEntity).setLife(((FireworkRocketEntityAccessor)entities[cursor]).getLife());
                    else if (newEntity.getType() == EntityType.TRIDENT)
                        ((TridentEntity)newEntity).tick();
                    else if (newEntity instanceof AbstractMinecartEntity)
                        ;
                    else if (newEntity.getType() == EntityType.WIND_CHARGE)
                        ((AbstractWindChargeEntity)newEntity).tick();
                    else
                        newEntity.tick();
                }

                remove(entities[cursor], Entity.RemovalReason.DISCARDED);

                if(newEntity.getType() == EntityType.END_CRYSTAL)
                    ;
                else if (newEntity.getType() == EntityType.FIREWORK_ROCKET)
                    ;
                else if (newEntity.getType() == EntityType.TRIDENT)
                    set(System.currentTimeMillis(), newEntity, newEntity, Entities.NOT_SYNCHRONISE);
                else if (newEntity instanceof AbstractMinecartEntity)
                    ;
                else if (newEntity.getType() == EntityType.WIND_CHARGE)
                    set(System.currentTimeMillis(), newEntity, newEntity, Entities.NOT_SYNCHRONISE);
                else
                    ;
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