package fr.solmey.clienthings.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;

import fr.solmey.clienthings.config.AutoDestroy;
import fr.solmey.clienthings.config.BypassRequiredAiming;
import fr.solmey.clienthings.config.Config;
import fr.solmey.clienthings.config.Consumables;
import fr.solmey.clienthings.config.Cooldowns;
import fr.solmey.clienthings.config.Crossbow;
import fr.solmey.clienthings.config.Crystals;
import fr.solmey.clienthings.config.Elytras;
import fr.solmey.clienthings.config.Firework;
import fr.solmey.clienthings.config.Pose;
import fr.solmey.clienthings.config.Servers;
import fr.solmey.clienthings.config.Trident;
import fr.solmey.clienthings.config.Weapons;

import net.minecraft.util.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;




import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonConfig {

    private static final String filePath = "config/clienthings.json";
    public static final File configFile = new File(filePath);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();;
    public static Config config = new Config();


    /*
    VANILLA
    Vanilla        vanilla

    MODDED
    Fabric        fabric
    Quilt        quilt
    Neoforge    neoforge
    Forge        forge

    PLUGIN
    Paper        Paper
    Spigot        Spigot
    Purpur        Purpur
    */
    //public final int ALL = 6;                // All servers softwares (vanilla / plugins / modded)
    //public final int CUSTOM = 6;
    //public final int CUSTOM = 6;
    //public final int CUSTOM_MODDED = 6;
    //public final int CUSTOM_PLUGIN = 6;
    //public final int CUSTOM_VANILLA = 6;
    //public final int CUSTOM = 6;
    //public final int PLUGIN_MODDED = 5;      // Plugins & Moddeds softwares
    //public final int VANILLA_MODDED = 4;     // Vanilla & Moddeds softwares
    //public final int VANILLA_PLUGIN = 3;     // Vanilla & Plugins softwares

    public static final int CUSTOM = 3;
    public static final int MODDED = 2;             // Moddeds softwares
    public static final int PLUGIN = 1;             // Plugins softwares
    public static final int VANILLA = 0;            // Vanilla softwares

    public static int type = VANILLA;   // Actual server type



    public static void loadConfig() {
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                config = gson.fromJson(reader, Config.class);
            } catch (IOException e) {
                return;
            }
        }
        /**
         * Enabled : Enables or disables consumable the given optimization 
         * Optout : Enables or disables the opt-out 
         * Debug : Enables or disables debugging
         * Servers : Indicates on which software the option should work
         * maxTime : Maximum time in milliseconds before the server optimization/action can no longer be synchronized with the client (avoids double effects)
         * maxDistance : Maximum distance in blocks between server-side and client-side action to be linked (avoids double effects)
         * Experimental : Enable or disable experimental options
         * bypassRequiredAiming : Bypass conditions like reach, use of a item (shield / consumable) or aiming before the crystal
         * autoDestroy : Automatically destroy the crystal when received when a cliend-side crystal at the exact same place was previously destroyed
         */


        if(config == null) config = new Config();
        if(config.enabled == null) config.enabled = true;
        //if(config.globalservers == null) config.globalservers = "ALL"; //ALL / MODDED / PLUGIN / VANILLA
        if(config.optout == null) config.optout = true;
        if(config.debug == null) config.debug = false;
        if(config.experimental == null) config.experimental = false;

        if(config.consumables == null) config.consumables = new Consumables();
        if(config.consumables.enabled == null) config.consumables.enabled = true;
        if(config.consumables.servers == null) config.consumables.servers = new Servers();
        if(config.consumables.servers.CUSTOM == null) config.consumables.servers.CUSTOM = true;
        if(config.consumables.servers.MODDED == null) config.consumables.servers.MODDED = true;
        if(config.consumables.servers.PLUGIN == null) config.consumables.servers.PLUGIN = true;
        if(config.consumables.servers.VANILLA == null) config.consumables.servers.VANILLA = true;
        if(config.consumables.maxDistance == null) config.consumables.maxDistance = 2.0;
        if(config.consumables.maxTime == null) config.consumables.maxTime = 6400;

        if(config.cooldowns == null) config.cooldowns = new Cooldowns();
        if(config.cooldowns.enabled == null) config.cooldowns.enabled = true;
        if(config.cooldowns.servers == null) config.cooldowns.servers = new Servers();
        if(config.cooldowns.servers.CUSTOM == null) config.cooldowns.servers.CUSTOM = true;
        if(config.cooldowns.servers.MODDED == null) config.cooldowns.servers.MODDED = true;
        if(config.cooldowns.servers.PLUGIN == null) config.cooldowns.servers.PLUGIN = true;
        if(config.cooldowns.servers.VANILLA == null) config.cooldowns.servers.VANILLA = true;

        if(config.crystals == null) config.crystals = new Crystals();
        if(config.crystals.enabled == null) config.crystals.enabled = true;
        if(config.crystals.servers == null) config.crystals.servers = new Servers();
        if(config.crystals.servers.CUSTOM == null) config.crystals.servers.CUSTOM = true;
        if(config.crystals.servers.MODDED == null) config.crystals.servers.MODDED = true;
        if(config.crystals.servers.PLUGIN == null) config.crystals.servers.PLUGIN = true;
        if(config.crystals.servers.VANILLA == null) config.crystals.servers.VANILLA = true;
        if(config.crystals.maxTime == null) config.crystals.maxTime = 800;
        if(config.crystals.autoDestroy == null) config.crystals.autoDestroy = new AutoDestroy();
        if(config.crystals.autoDestroy.enabled == null) config.crystals.autoDestroy.enabled = true;
        if(config.crystals.autoDestroy.servers == null) config.crystals.autoDestroy.servers = new Servers();
        if(config.crystals.autoDestroy.servers.CUSTOM == null) config.crystals.autoDestroy.servers.CUSTOM = false;
        if(config.crystals.autoDestroy.servers.MODDED == null) config.crystals.autoDestroy.servers.MODDED = true;
        if(config.crystals.autoDestroy.servers.PLUGIN == null) config.crystals.autoDestroy.servers.PLUGIN = true;
        if(config.crystals.autoDestroy.servers.VANILLA == null) config.crystals.autoDestroy.servers.VANILLA = false;
        if(config.crystals.autoDestroy.bypassRequiredAiming == null) config.crystals.autoDestroy.bypassRequiredAiming = new BypassRequiredAiming();
        if(config.crystals.autoDestroy.bypassRequiredAiming.enabled == null) config.crystals.autoDestroy.bypassRequiredAiming.enabled = true;
        if(config.crystals.autoDestroy.bypassRequiredAiming.servers == null) config.crystals.autoDestroy.bypassRequiredAiming.servers = new Servers();
        if(config.crystals.autoDestroy.bypassRequiredAiming.servers.CUSTOM == null) config.crystals.autoDestroy.bypassRequiredAiming.servers.CUSTOM = false;
        if(config.crystals.autoDestroy.bypassRequiredAiming.servers.MODDED == null) config.crystals.autoDestroy.bypassRequiredAiming.servers.MODDED = true;
        if(config.crystals.autoDestroy.bypassRequiredAiming.servers.PLUGIN == null) config.crystals.autoDestroy.bypassRequiredAiming.servers.PLUGIN = false;
        if(config.crystals.autoDestroy.bypassRequiredAiming.servers.VANILLA == null) config.crystals.autoDestroy.bypassRequiredAiming.servers.VANILLA = true;

        if(config.elytras == null) config.elytras = new Elytras();
        if(config.elytras.enabled == null) config.elytras.enabled = true;
        if(config.elytras.servers == null) config.elytras.servers = new Servers();
        if(config.elytras.servers.CUSTOM == null) config.elytras.servers.CUSTOM = false;
        if(config.elytras.servers.MODDED == null) config.elytras.servers.MODDED = true;
        if(config.elytras.servers.PLUGIN == null) config.elytras.servers.PLUGIN = true;
        if(config.elytras.servers.VANILLA == null) config.elytras.servers.VANILLA = true;

        if(config.firework == null) config.firework = new Firework();
        if(config.firework.enabled == null) config.firework.enabled = true;
        if(config.firework.servers == null) config.firework.servers = new Servers();
        if(config.firework.servers.CUSTOM == null) config.firework.servers.CUSTOM = true;
        if(config.firework.servers.MODDED == null) config.firework.servers.MODDED = true;
        if(config.firework.servers.PLUGIN == null) config.firework.servers.PLUGIN = true;
        if(config.firework.servers.VANILLA == null) config.firework.servers.VANILLA = true;
        if(config.firework.maxDistance == null) config.firework.maxDistance = 3.0;
        if(config.firework.maxTime == null) config.firework.maxTime = 3200;

        if(config.minecart == null) config.minecart = new Minecart();
        if(config.minecart.enabled == null) config.minecart.enabled = true;
        if(config.minecart.servers == null) config.minecart.servers = new Servers();
        if(config.minecart.servers.CUSTOM == null) config.minecart.servers.CUSTOM = true;
        if(config.minecart.servers.MODDED == null) config.minecart.servers.MODDED = true;
        if(config.minecart.servers.PLUGIN == null) config.minecart.servers.PLUGIN = true;
        if(config.minecart.servers.VANILLA == null) config.minecart.servers.VANILLA = true;
        if(config.minecart.maxTime == null) config.minecart.maxTime = 1200;

        if(config.pose == null) config.pose = new Pose();
        if(config.pose.enabled == null) config.pose.enabled = true;
        if(config.pose.servers == null) config.pose.servers = new Servers();
        if(config.pose.servers.CUSTOM == null) config.pose.servers.CUSTOM = true;
        if(config.pose.servers.MODDED == null) config.pose.servers.MODDED = true;
        if(config.pose.servers.PLUGIN == null) config.pose.servers.PLUGIN = true;
        if(config.pose.servers.VANILLA == null) config.pose.servers.VANILLA = true;

        if(config.swap == null) config.swap = new Swap();
        if(config.swap.enabled == null) config.swap.enabled = true;
        if(config.swap.servers == null) config.swap.servers = new Servers();
        if(config.swap.servers.CUSTOM == null) config.swap.servers.CUSTOM = true;
        if(config.swap.servers.MODDED == null) config.swap.servers.MODDED = true;
        if(config.swap.servers.PLUGIN == null) config.swap.servers.PLUGIN = true;
        if(config.swap.servers.VANILLA == null) config.swap.servers.VANILLA = true;

        if(config.weapons == null) config.weapons = new Weapons();
        if(config.weapons.enabled == null) config.weapons.enabled = true;
        if(config.weapons.servers == null) config.weapons.servers = new Servers();
        if(config.weapons.servers.CUSTOM == null) config.weapons.servers.CUSTOM = true;
        if(config.weapons.servers.MODDED == null) config.weapons.servers.MODDED = true;
        if(config.weapons.servers.PLUGIN == null) config.weapons.servers.PLUGIN = true;
        if(config.weapons.servers.VANILLA == null) config.weapons.servers.VANILLA = true;
        if(config.weapons.maxDistance == null) config.weapons.maxDistance = 48.0;
        if(config.weapons.maxTime == null) config.weapons.maxTime = 4800;
        if(config.weapons.trident == null) config.weapons.trident = new Trident();
        if(config.weapons.trident.enabled == null) config.weapons.trident.enabled = true;
        if(config.weapons.trident.servers == null) config.weapons.trident.servers = new Servers();
        if(config.weapons.trident.servers.CUSTOM == null) config.weapons.trident.servers.CUSTOM = true;
        if(config.weapons.trident.servers.MODDED == null) config.weapons.trident.servers.MODDED = true;
        if(config.weapons.trident.servers.PLUGIN == null) config.weapons.trident.servers.PLUGIN = true;
        if(config.weapons.trident.servers.VANILLA == null) config.weapons.trident.servers.VANILLA = true;
        if(config.weapons.crossbow == null) config.weapons.crossbow = new Crossbow();
        if(config.weapons.crossbow.enabled == null) config.weapons.crossbow.enabled = true;
        if(config.weapons.crossbow.servers == null) config.weapons.crossbow.servers = new Servers();
        if(config.weapons.crossbow.servers.CUSTOM == null) config.weapons.crossbow.servers.CUSTOM = true;
        if(config.weapons.crossbow.servers.MODDED == null) config.weapons.crossbow.servers.MODDED = true;
        if(config.weapons.crossbow.servers.PLUGIN == null) config.weapons.crossbow.servers.PLUGIN = true;
        if(config.weapons.crossbow.servers.VANILLA == null) config.weapons.crossbow.servers.VANILLA = true;
        
        saveConfig();
    }

    public static void openConfig() {
        if (!JsonConfig.configFile.exists()) {
            JsonConfig.saveConfig();
        }
        if (JsonConfig.configFile.exists()) {
            Util.getOperatingSystem().open(JsonConfig.configFile.toPath().toUri());
        }
    }

    public static void saveConfig() {
        configFile.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveType(String arg) {
        switch(arg) {
            case "vanilla":
                type = VANILLA;
                break;

            case "fabric":
                type = MODDED;
                break;
            case "quilt":
                type = MODDED;
                break;
            case "neoforge":
                type = MODDED;
                break;
            case "forge":
                type = MODDED;
                break;

            case "Paper":
                type = PLUGIN;
                break;
            case "Spigot":
                type = PLUGIN;
                break;
            case "Purpur":
                type = PLUGIN;
                break;

            default:
                type = CUSTOM;
        }
    }

    public static boolean shouldWorkOnThisServer(Servers serversOption) {
        boolean bl = false;
        if(serversOption.CUSTOM && type == CUSTOM) bl = true;
        if(serversOption.MODDED && type == MODDED) bl = true;
        if(serversOption.PLUGIN && type == PLUGIN) bl = true;
        if(serversOption.VANILLA && type == VANILLA) bl = true;
        return bl;
    }

    public static boolean shouldWork(Servers serversOption) {
        boolean bl = false;

        if(JsonConfig.config.enabled)
            bl = shouldWorkOnThisServer(serversOption);
        if(MinecraftClient.getInstance().isInSingleplayer())
            bl = false;
        return bl;
    }
}