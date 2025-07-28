package fr.solmey.clienthings.util;

import fr.solmey.clienthings.mixin.elytras.EntityAccessor;
import fr.solmey.clienthings.util.Swap;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;
import net.minecraft.item.ItemStack;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.MinecraftClient;

public class Actions {

    private static long[] ticks = new long[256];
    private static byte[] actions = new byte[256];

    public static final byte NOTHING = 0;
    public static final byte STOP_FLYING = 1;
    public static final byte SWAP_ITEM_WITH_OFFHAND = 2;

    public static void add(long _tick, byte _Action) {
        int cursor = 0;
        for(int i = 0; i < 256 ; i++)
            if(ticks[i] == 0)
                cursor = i;

        ticks[cursor] = _tick;
        actions[cursor] = _Action;
        executePending();
    }

    public static void executePending() {
        /*int ping = 50;
        MinecraftClient client = MinecraftClient.getInstance();
		if (client.getNetworkHandler() != null && client.player != null) {
			PlayerListEntry playerEntry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
			if (playerEntry != null)
                ping = playerEntry.getLatency();
		}*/

        for(int i = 0; i < 256 ; i++) {
            if(ticks[i] != 0 && ticks[i] <= MinecraftClient.getInstance().world.getTime()) {
                switch(actions[i]) {
                    case STOP_FLYING:
                        ClientPlayerEntity player = MinecraftClient.getInstance().player;
                        ((EntityAccessor)player).invokeSetFlag(Elytras.GLIDING_FLAG_INDEX, false);
                        break;
                    case SWAP_ITEM_WITH_OFFHAND:
                        MinecraftClient client = MinecraftClient.getInstance();
                        PlayerEntity playerEntity = MinecraftClient.getInstance().player;
                        if(Swap.mainHand == playerEntity.getMainHandStack() && Swap.offHand == playerEntity.getOffHandStack()) {
                            ItemStack itemStack = playerEntity.getStackInHand(Hand.OFF_HAND);
                            playerEntity.setStackInHand(Hand.OFF_HAND, playerEntity.getStackInHand(Hand.MAIN_HAND));
                            playerEntity.setStackInHand(Hand.MAIN_HAND, itemStack);
                            playerEntity.clearActiveItem();
                        }
                        break;
                    default:
                        ;
                }
                ticks[i] = 0;
                actions[i] = NOTHING;
            }
        }
    }

    public static void clear() {
        for(int i = 0; i < 256 ; i++) {
            ticks[i] = 0;
            actions[i] = NOTHING;
        }
    }
}