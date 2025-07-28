package fr.solmey.clienthings.mixin.cooldowns;

import net.minecraft.client.network.ClientPlayNetworkHandler;

import fr.solmey.clienthings.config.JsonConfig;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.CooldownUpdateS2CPacket;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@Inject(method = "onCooldownUpdate", at = @At("HEAD"), cancellable = true)
  public void onCooldownUpdate(CooldownUpdateS2CPacket packet, CallbackInfo info) {
    if (JsonConfig.config.cooldowns.enabled && JsonConfig.shouldWork(JsonConfig.config.cooldowns.servers)) {
      MinecraftClient client = MinecraftClient.getInstance();
        if(client != null && client.player != null) {
        ItemCooldownManager cooldownManager = client.player.getItemCooldownManager();
        Item item = Registries.ITEM.get(packet.cooldownGroup());
        ItemStack stack = new ItemStack(item);

        if (cooldownManager != null && cooldownManager.isCoolingDown(stack) && packet.cooldown() != 0)
          info.cancel();
      }
    }
  }

 	/* @Inject(method = "onEntityAttributes", at = @At("HEAD"), cancellable = true)
  public void onEntityAttributes(EntityAttributesS2CPacket packet, CallbackInfo info) {
    if (JsonConfig.config.cooldowns.enabled && JsonConfig.shouldWork(JsonConfig.config.cooldowns.servers))
        info.cancel();
  } */
}