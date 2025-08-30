## What's ClienThings ?
ClienThings is a project to predict more things client-side.
For example, if you eat food, the animation only ends when the server indicates it will.
This mod fixes that and therefore compensates for the ping.
You can use ClienThings on your server to find out if a player is using the same mod.

Currently this mod compensates for :
- Consumables
- End Crystal
- Elytras
- Firework rocket
- Cooldowns
- Entity pose
- Weapons
- Minecart
- Swap
- Wind charge

In development :
- Jukebox

Coming Soon™ :
- Cactus damage (yes)
- Fishing Rod
- Lead
- Boat item
- Snowball
- Bottle o' Enchanting
- Spawn Eggs
- Egg
- Armor Stand
- Note block
- Item frame
- Dyes
- Interfaces
- Void damage
- Game mode change
- Potion Effects
- Tchat
- Sounds
- Crafting
- Pick Block
- Scaffolding
- Knockback
- Fall damage
- Interactions with blocks
- Interactions with items
- Interactions with entities

<details>
<summary>Explanations of the predictions</summary>

> Consumables

Allows consumption to be completed without delay

> Cooldowns

Removes server-imposed cooldowns that are late (on enderpearls, chorus fruit, etc.)

> End Crystal

Allows crystals to be spawned and destroyed immediately.
Since some servers don't have server-side compensation software, this also allows real crystals to be automatically destroyed upon reception if they were previously destroyed in their client-side state only.

> Elytras

Allows you to stop flying without delay

> Firework rocket

Allows you to use a firework rocket without delay when flying in elytra

> Minecarts

Allows minecarts to be spawned without delay (if experimental setting is enabled you can ride it without delay)

> Entity pose

Removes server-imposed poses (flying, sneaking...)

> Swap

Predicts the swap of items between the main and secondary hand (only works if experimental setting is enabled)

> Weapons

Throw the tridents without delay
Finish loading the crossbow without delay

> Wind Charge

Predicts weapon behavior (crossbow & trident)

Launches wind-charges without delay and makes them destroy themselves at maximum height + 30 (as in vanilla)


</details>



<details>
<summary>Configuration (explanations)</summary>
  
- enabled : Enables or disables the option
- optout : Enables or disables the opt-out
- debug : Enables or disables debug mode
- experimental : Enables or disables experimental options
- servers : Allows you to select which server type the given option should work on. CUSTOM / MODDED / PLUGIN / VANILLA
- autoDestroy : Automatically destroys a crystal when received if a client-side crystal located in the exact same location was previously destroyed
- bypassRequiredAiming : Ignores certain conditions such as range, item usage (shield/consumable) or aiming when using autoDestroy
- maxDistance : Maximum distance in blocks between server-side and client-side actions to bind (prevents unwanted server actions from being considered true by the client)
</details>


<details>
<summary>Configuration (default)</summary>
  
```
{
  "enabled": true,
  "optout": true,
  "debug": false,
  "experimental": false,
  "defaultPing": 75,
  "consumables": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    },
    "maxDistance": 2.0
  },
  "cooldowns": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    }
  },
  "crystals": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    },
    "autoDestroy": {
      "enabled": true,
      "servers": {
        "CUSTOM": false,
        "MODDED": true,
        "PLUGIN": true,
        "VANILLA": true
      },
      "bypassRequiredAiming": {
        "enabled": true,
        "servers": {
          "CUSTOM": false,
          "MODDED": true,
          "PLUGIN": false,
          "VANILLA": true
        }
      }
    }
  },
  "elytras": {
    "enabled": true,
    "servers": {
      "CUSTOM": false,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    }
  },
  "firework": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    },
    "maxDistance": 3.0
  },
  "jukebox": {
    "enabled": false,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    }
  },
  "minecart": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    }
  },
  "pose": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    }
  },
  "swap": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    }
  },
  "weapons": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    },
    "maxDistance": 3.0,
    "crossbow": {
      "enabled": true,
      "servers": {
        "CUSTOM": true,
        "MODDED": true,
        "PLUGIN": true,
        "VANILLA": true
      }
    },
    "trident": {
      "enabled": true,
      "servers": {
        "CUSTOM": true,
        "MODDED": true,
        "PLUGIN": true,
        "VANILLA": true
      }
    }
  },
  "windcharge": {
    "enabled": true,
    "servers": {
      "CUSTOM": true,
      "MODDED": true,
      "PLUGIN": true,
      "VANILLA": true
    },
    "maxDistance": 3.0
  }
}
```

</details>

## How it works ?
Instead of waiting for an indication from the server, the client will simulate it at the right time, without delay.

## Disclaimer
This mod does NOT give any advantages, it is not a cheat. It only compensates for the ping.
This mod may be detected as a cheat by some anti-cheats. Use with caution on public servers.

Versions for 1.21-1.21.1 are deprecated.

Allowed on servers such as:
- `mcpvp.club`
- `fadedmc.net`
- `turtled.net`

Banned on
- `pvphub.me`
- `MCTiers (mace)`

## Compatibility
**ClienThings** should work on any client and server.
