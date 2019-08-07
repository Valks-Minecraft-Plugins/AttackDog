# AttackDog
Fully configurable wolf management plugin. Change your wolfs name or level up your wolfs health, damage and speed.

### Requirements
- [Vault](https://www.spigotmc.org/resources/vault.34315/).
- Any Economy Plugin (E.g. EssentialsX, EasyShops, AuctionHouse, RealEstate).
- Minecraft Client + Server on 1.12.2

### Permissions
```yml
attackdog.summon
```

### Configs
#### config.yml
```yml
wolf:
  teleport_on_join: true
  # Should the wolf teleport when its owner joins the server?
  
  tamed_on_spawn: true
  # Should the wolf be tamed on first summon?
  
  max_name_length: 20
  # The max length of your wolfs name.
  
  one_word_name: true
  # Should the wolfs name be limited to only one word?
  
  summon:
    cooldown: 60
    # The cooldown for how long before you can attempt to summon another wolf if necessary.
  health:
    cost: 100
    # The base cost of the wolfs health upgrade.
    
    cost_lvl_multiplier: 1.2
    # The cost level multiplier for the wolfs health upgrade.
    
    health_lvl_multiplier: 1.2
    # Determines how much health is applied every upgrade.
    
    initial_amount: 10
    # The initial amount of health the wolf should be summoned with.
    
    max_level: 10
    # The max number of health upgrades.
  damage:
    cost: 250
    # The base cost of the wolfs damage upgrade.
    
    cost_lvl_multiplier: 1.2
    # The cost level multiplier for the wolfs damage upgrade.
    
    health_lvl_multiplier: 1.2
    # Determines how much damage is applied every upgrade.
    
    initial_amount: 2.0
    # The initial amount of damage the wolf should be summoned with.
    
    max_level: 10
    # The max number of damage upgrades.
  speed:
    cost: 125
    # The base cost of the wolfs speed upgrade.
    
    cost_lvl_multiplier: 1.2
    # The cost level multiplier for the wolfs speed upgrade.
    
    health_lvl_multiplier: 1.2
    # Determines how much speed is applied every upgrade.
    
    initial_amount: 0.2
    # The initial amount of speed the wolf should be summoned with.
    
    max_level: 10
    # The max number of speed upgrades.
```

#### gui.yml
```yml
# Materials https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html
# Color Codes https://www.planetminecraft.com/blog/bukkit-color-codes/
# Item Flags https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/inventory/ItemFlag.html
# Enchantments https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/enchantments/Enchantment.html
gui:
  buttons:
    update_name:
      material: NAME_TAG
      name: §f§fName
      lore:
      - §7Change Name
      slot: 0
    update_health:
      material: APPLE
      name: §f§fHealth
      lore:
      - §7Upgrade Health
      - '§7Cost: §f$%amount%'
      - '§7Level: §f%level%'
      slot: 1
    update_damage:
      material: WOOD_SWORD
      name: §f§fDamage
      lore:
      - §7Upgrade Damage
      - '§7Cost: §f$%amount%'
      - '§7Level: §f%level%'
      flags:
      - HIDE_ATTRIBUTES
      slot: 2
    update_speed:
      material: FEATHER
      name: §f§fSpeed
      lore:
      - §7Upgrade Speed
      - '§7Cost: §f$%amount%'
      - '§7Level: §f%level%'
      slot: 3
```

#### messages.yml
```yml
messages:
  error:
    no_args: '&cPlease specify valid arguments.'
    no_perms: '&cYou do not have permission to do that.'
    cooldown: '&cYou''re on a cooldown, please wait!'
    
    not_enough_money: '&cYou need $%cost% more to do that!'
    # %cost% -> The money the player lacks for the purchase.
    
    name_too_long: '&cName can''t be longer than %maxnamelength% characters! Type
      EXIT to leave this mode.'
    # %maxnamelength% -> The maximum number of characters the name can be set to.
    
    max_level: '&cYou reached the max level for this upgrade!'
  message:
    exited_input_mode: '&7Exited input mode.'
    summoned: '&2Summoned a wolf!'
    update_name:
      input: '&7Input a new name for your wolf.'
      output: '&7Your wolfs name was set from &f%oldname% &7to &f%newname%.'
      # %oldname% -> The old name of the wolf.
      # %newname% -> The new name of the wolf.
      
    update_health:
      output: '&7Upgraded your wolfs health to level &f%newhealthlevel% &7for &f$%cost%&7.
        Your balance is now at &f$%playerbalance%&7. (&f%oldhealth% &7-> &f%newhealth%&7)'
      # %oldhealthlevel% -> The level the health use to be at.
      # %newhealthlevel% -> The level the health is now at.
      # %oldhealth% -> The previous wolfs health.
      # %newhealth% -> The new wolfs health.
      # %cost% -> How much the upgrade costed the player.
      # %playerbalance% -> The players balance after they purchased the upgrade.
      
    update_damage:
      output: '&7Upgraded your wolfs damage to level &f%newdamagelevel% &7for &f$%cost%&7.
        Your balance is now at &f$%playerbalance%&7. (&f%olddamage% &7-> &f%newdamage%&7)'
      # %olddamagelevel% -> The level the damage use to be at.
      # %newdamagelevel% -> The level the damage is now at.
      # %oldhealth% -> The previous wolfs damage.
      # %newhealth% -> The new wolfs damage.
      # %cost% -> How much the upgrade costed the player.
      # %playerbalance% -> The players balance after they purchased the upgrade.
      
    update_speed:
      output: '&7Upgraded your wolfs speed to level &f%newspeedlevel% &7for &f$%cost%&7.
        Your balance is now at &f$%playerbalance%&7. (&f%oldspeed% &7-> &f%newspeed%&7)'
      # %oldspeedlevel% -> The level the speed use to be at.
      # %newspeedlevel% -> The level the speed is now at.
      # %oldspeed% -> The previous wolfs speed.
      # %newspeed% -> The new wolfs speed.
      # %cost% -> How much the upgrade costed the player.
      # %playerbalance% -> The players balance after they purchased the upgrade.
```

#### wolfs.yml
```yml
wolfs:
  # Example wolf preset, do not remove or config may become corrupt if list empty.
  '1':
    uuid: Template
    name: Example Wolf
    level:
      health: 0
      damage: 0
      speed: 0
    stats:
      tamed: true
      curHealth: 0
      curDamage: 0
      curSpeed: 0
    owner:
      uuid: Preset
      name: valkyrienyanko
  '2':
    uuid: 0a48dbd7-a80e-4126-aa93-1e9ef6053515
    name: Wolf
    level:
      health: 0
      damage: 0
      speed: 0
    stats:
      curHealth: 0
      curDamage: 0
      curSpeed: 0
    owner:
      uuid: 35ea50ff-3927-4fcf-b65e-01932c67b864
      name: valkyrienyanko
```
