# CakeWarsGamePlugin
The main plugin for the CakeWarsGame. CakeWars is our Proof of Concept gamemode.


## config.yml

```yaml
shop:
  directory1:
    name: Weapons
    material: IRON_SWORD
    slot: 0
    items:
    - name: Wooden Sword
      material: WOODEN_SWORD
      amount: 1
      slot: 0
    lore:
    - Buy weapons here
```

## exomap.yml

```yaml
teams:
  team1:
    spawnLocation:
      x: 23.5
      y: 72
      z: 118.5
    cakeLocation:
      x: 23.5
      y: 71
      z: 115.5
    primaryShopLocation:
      x: 30.5
      y: 71
      z: 115.5
    maxPlayers: 2
lobby:
  spawn:
    x: -4.5
    y: 125
    z: 3
spawners:
  spawnerTypes:
    iron:
      material: IRON_INGOT
      interval: 40
  spawners:
    spawner1:
      type: iron
      location:
        x: 22.5
        y: 72
        z: 117.5
flavorIds:
- default
- fast
```