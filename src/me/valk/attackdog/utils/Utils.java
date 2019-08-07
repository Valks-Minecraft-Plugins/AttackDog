package me.valk.attackdog.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import me.valk.attackdog.AttackDog;

public class Utils {
	/**
	 * Registers and spawns the wolf.
	 * 
	 * @param p
	 */
	@SuppressWarnings("deprecation")
	public static void registerWolf(Player p) {
		YamlConfiguration mainConfig = AttackDog.mainConfig;
		YamlConfiguration wolfsConfig = AttackDog.wolfsConfig;
		ConfigurationSection configSection = wolfsConfig.getConfigurationSection("wolfs");

		Wolf wolf = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
		wolf.setMaxHealth(mainConfig.getDouble("wolf.health.initial_amount"));
		wolf.setHealth(wolf.getMaxHealth());
		wolf.setCustomNameVisible(true);
		if (mainConfig.getBoolean("wolf.tamed_on_spawn")) {
			wolf.setOwner(p);
			wolf.setTamed(true);
		}
		wolf.setAdult();
		
		AttributeInstance attributeSpeed = wolf.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
		AttributeInstance attributeAttack = wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
		AttributeInstance attributeArmor = wolf.getAttribute(Attribute.GENERIC_ARMOR);
		
		double initialSpeed = mainConfig.getDouble("wolf.speed.initial_amount");
		double initialDamage = mainConfig.getDouble("wolf.damage.initial_amount");
		double initialArmor = mainConfig.getDouble("wolf.armor.initial_amount");
		
		attributeSpeed.setBaseValue(initialSpeed);
		attributeAttack.setBaseValue(initialDamage);
		attributeArmor.setBaseValue(initialArmor);

		int slot = 1;
		if (wolfsConfig.isConfigurationSection("wolfs")) {
			for (String element : configSection.getKeys(false)) {
				if (element != null) {
					slot++;
				}
			}
		}

		wolfsConfig.set("wolfs." + slot + ".uuid", wolf.getUniqueId().toString());
		wolfsConfig.set("wolfs." + slot + ".name", wolf.getName());
		wolfsConfig.set("wolfs." + slot + ".level.health", 0);
		wolfsConfig.set("wolfs." + slot + ".level.damage", 0);
		wolfsConfig.set("wolfs." + slot + ".level.speed", 0);
		wolfsConfig.set("wolfs." + slot + ".stats.curHealth", 0);
		wolfsConfig.set("wolfs." + slot + ".stats.curDamage", 0);
		wolfsConfig.set("wolfs." + slot + ".stats.curSpeed", 0);
		wolfsConfig.set("wolfs." + slot + ".owner.uuid", p.getUniqueId().toString());
		wolfsConfig.set("wolfs." + slot + ".owner.name", p.getName());
		AttackDog.wolfsCM.saveConfig();
	}

	/**
	 * Unregisters the wolf from the config.
	 * 
	 * @param p
	 */
	public static void unregisterWolf(Player p) {
		YamlConfiguration wolfsConfig = AttackDog.wolfsConfig;
		ConfigurationSection configSection = wolfsConfig.getConfigurationSection("wolfs");

		for (String element : configSection.getKeys(false)) {
			if (wolfsConfig.getString("wolfs." + element + ".owner.uuid").equals(p.getUniqueId().toString())) {
				wolfsConfig.set("wolfs." + element, null);
			}
		}
	}

	/**
	 * Spawns the wolf from the config.
	 * 
	 * @param p
	 */
	@SuppressWarnings("deprecation")
	public static void spawnWolf(Player p) {
		YamlConfiguration wolfsConfig = AttackDog.wolfsConfig;
		ConfigurationSection configSection = wolfsConfig.getConfigurationSection("wolfs");

		for (String element : configSection.getKeys(false)) {
			if (wolfsConfig.getString("wolfs." + element + ".owner.uuid").equals(p.getUniqueId().toString())) {
				Wolf wolf = (Wolf) p.getWorld().spawnEntity(p.getLocation(), EntityType.WOLF);
				double curHealth = wolfsConfig.getDouble("wolfs." + element + ".stats.curHealth");
				double curDamage = wolfsConfig.getDouble("wolfs." + element + ".stats.curDamage");
				double curSpeed = wolfsConfig.getDouble("wolfs." + element + ".stats.curSpeed");
				wolf.setMaxHealth(curHealth);
				wolf.setHealth(curHealth);
				wolf.setCustomNameVisible(true);
				if (wolfsConfig.getBoolean("wolfs." + element + ".stats.tamed")) {
					wolf.setOwner(p);
					wolf.setTamed(true);
				}
				wolf.setAdult();
				
				AttributeInstance attributeSpeed = wolf.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
				AttributeInstance attributeAttack = wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
				attributeSpeed.setBaseValue(curSpeed);
				attributeAttack.setBaseValue(curDamage);
			}
		}
	}

	/**
	 * Despawns the wolf.
	 * 
	 * @param p
	 */
	public static void despawnWolf(Player p) {
		YamlConfiguration wolfsConfig = AttackDog.wolfsConfig;
		ConfigurationSection configSection = wolfsConfig.getConfigurationSection("wolfs");

		for (String element : configSection.getKeys(false)) {
			if (wolfsConfig.getString("wolfs." + element + ".owner.uuid").equals(p.getUniqueId().toString())) {
				UUID wolfID = UUID.fromString(wolfsConfig.getString("wolfs." + element + ".uuid"));
				Wolf wolf = (Wolf) Bukkit.getEntity(wolfID);
				wolf.remove();
			}
		}
	}

	/**
	 * Teleport wolf to its owner.
	 * 
	 * @param p
	 */
	public static void teleportWolfToOwner(Player p) {
		YamlConfiguration wolfsConfig = AttackDog.wolfsConfig;
		ConfigurationSection configSection = wolfsConfig.getConfigurationSection("wolfs");

		for (String element : configSection.getKeys(false)) {
			if (wolfsConfig.getString("wolfs." + element + ".owner.uuid").equals(p.getUniqueId().toString())) {
				UUID wolfID = UUID.fromString(wolfsConfig.getString("wolfs." + element + ".uuid"));
				Wolf wolf = (Wolf) Bukkit.getEntity(wolfID);
				wolf.teleport(p);
			}
		}
	}
}
