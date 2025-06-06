## What's ClienThings ?
**ClienThings** is a project to simulate more things client-side.
For example, if you eat food, the animation only ends when the server indicates it will.
This mod fixes that and therefore compensates for the ping.

Currently this mod compensates for :
- Consumables (90%)
- Elytras (95%)

Coming Soon™ :
- Cooldowns
- Fall damage
- Interfaces
- End Crystal Placement (Use [Marlow Crystal Optimizer](https://modrinth.com/mod/marlow-crystal-optimizer) in the meantime)
- Knockback (in some case)
- Game mode change
- Sneaking state
- Firework rocket
- Potion Effects
- Tchat
- Crafting
- Weapons (Bow/Crossbow, etc.)
- Note block
- Jukebox
- Swap Items In Hands
- Pick Block
- Scaffolding
- Splash Potion (maybe)
- Wind charge (maybe)
- Enderpearl (maybe)


<details>
<summary>Configuration (default)</summary>
  
```
#consumables: Enables or disables consumable prediction
#consumables_MaxDistance: Maximum distance (in blocks) between the client and the server for the end-of-consumption sound(s) to be ignored.
#consumables_MaxTime: Maximum time (in ms) the server has to play the end-of-consumption sound(s) to be ignored.
#debugMode: Debug mode (for developers)
#elytras: Enables or disables elytras prediction

consumables=true
consumables_MaxDistance=0.2
consumables_MaxTime=3200
debugMode=false
elytras=true

```

</details>

## How it works ?
Instead of waiting for an indication from the server, the client will simulate it at the right time, without delay.

## Disclaimer
This mod may be detected as a cheat by some anti-cheats. Use with caution on public servers.

## Complementary
If you use this mod, you might be interested by [No Bugs](https://modrinth.com/project/no-bugs)

## Compatibility
**ClienThings** should work on any client.
