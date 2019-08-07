package me.valk.attackdog.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.valk.attackdog.AttackDog;
import me.valk.attackdog.utils.TextModule;
import net.milkbowl.vault.economy.Economy;

public class ListenerGUI implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	private void invClick(InventoryClickEvent e) {
		Economy eco = AttackDog.getEconomy();

		YamlConfiguration mainConfig = AttackDog.mainConfig;
		YamlConfiguration guiConfig = AttackDog.guiConfig;
		YamlConfiguration wolfsConfig = AttackDog.wolfsConfig;
		YamlConfiguration messagesConfig = AttackDog.messagesConfig;

		Player p = (Player) e.getWhoClicked();

		if (e.getView().getTitle().equalsIgnoreCase("Wolf")) {
			e.setCancelled(true);

			/*
			 * Name
			 */
			if (e.getSlot() == guiConfig.getInt("gui.buttons.update_name.slot")) {

				ListenerChat.inputName.add(p.getUniqueId());
				p.sendMessage(TextModule.color(messagesConfig.getString("messages.message.update_name.input")));
				e.getView().close();
			}

			/*
			 * Health
			 */
			if (e.getSlot() == guiConfig.getInt("gui.buttons.update_health.slot")) {
				ConfigurationSection configSection = wolfsConfig.getConfigurationSection("wolfs");
				for (String element : configSection.getKeys(false)) {
					if (wolfsConfig.getString("wolfs." + element + ".owner.uuid").equals(p.getUniqueId().toString())) {
						int oldHealthLevel = wolfsConfig.getInt("wolfs." + element + ".level.health");
						int newHealthLevel = oldHealthLevel + 1;

						if (newHealthLevel > mainConfig.getInt("wolf.health.max_level")) {
							e.getView().close();
							p.sendMessage(TextModule.color(messagesConfig.getString("messages.error.max_level")));
							return;
						}

						double costBase = mainConfig.getDouble("wolf.health.cost");
						double costMultiplier = mainConfig.getDouble("wolf.health.cost_lvl_multiplier");
						double cost = costBase * newHealthLevel * costMultiplier * costMultiplier;
						double playerBalance = eco.getBalance(p);
						if (playerBalance < cost) {
							e.getView().close();
							double remaining = cost - playerBalance;
							String output = messagesConfig.getString("messages.error.not_enough_money");
							output = output.replaceAll("%cost%", String.format("%.2f", remaining));
							p.sendMessage(TextModule.color(output));
							return;
						}
						;

						eco.withdrawPlayer(p, cost);

						UUID wolfID = UUID.fromString(wolfsConfig.getString("wolfs." + element + ".uuid"));
						Wolf wolf = (Wolf) Bukkit.getEntity(wolfID);

						double multiplier = mainConfig.getDouble("wolf.health.health_lvl_multiplier");

						double oldHealth = wolf.getMaxHealth();
						double newHealth = oldHealth * multiplier;

						wolf.setMaxHealth(newHealth);
						wolf.setHealth(wolf.getMaxHealth());

						wolfsConfig.set("wolfs." + element + ".level.health", newHealthLevel);
						wolfsConfig.set("wolfs." + element + ".stats.curHealth", wolf.getMaxHealth());
						AttackDog.wolfsCM.saveConfig();

						String output = messagesConfig.getString("messages.message.update_health.output");
						output = output.replaceAll("%oldhealth%", String.format("%.2f", oldHealth));
						output = output.replaceAll("%newhealth%", String.format("%.2f", newHealth));
						output = output.replaceAll("%oldhealthlevel%", String.valueOf(oldHealthLevel));
						output = output.replaceAll("%newhealthlevel%", String.valueOf(newHealthLevel));
						output = output.replaceAll("%cost%", String.format("%.2f", cost));
						output = output.replaceAll("%playerbalance%", String.format("%.2f", playerBalance));

						p.sendMessage(TextModule.color(output));
					}
				}

				e.getView().close();
			}

			/*
			 * Damage
			 */
			if (e.getSlot() == guiConfig.getInt("gui.buttons.update_damage.slot")) {
				ConfigurationSection configSection = wolfsConfig.getConfigurationSection("wolfs");
				for (String element : configSection.getKeys(false)) {
					if (wolfsConfig.getString("wolfs." + element + ".owner.uuid").equals(p.getUniqueId().toString())) {
						int oldDamageLevel = wolfsConfig.getInt("wolfs." + element + ".level.damage");
						int newDamageLevel = oldDamageLevel + 1;

						if (newDamageLevel > mainConfig.getInt("wolf.damage.max_level")) {
							e.getView().close();
							p.sendMessage(TextModule.color(messagesConfig.getString("messages.error.max_level")));
							return;
						}

						double costBase = mainConfig.getDouble("wolf.damage.cost");
						double costMultiplier = mainConfig.getDouble("wolf.damage.cost_lvl_multiplier");
						double cost = costBase * newDamageLevel * costMultiplier * costMultiplier;
						double playerBalance = eco.getBalance(p);
						if (playerBalance < cost) {
							e.getView().close();
							double remaining = cost - playerBalance;
							String output = messagesConfig.getString("messages.error.not_enough_money");
							output = output.replaceAll("%cost%", String.format("%.2f", remaining));
							p.sendMessage(TextModule.color(output));
							return;
						}
						;

						eco.withdrawPlayer(p, cost);

						UUID wolfID = UUID.fromString(wolfsConfig.getString("wolfs." + element + ".uuid"));
						Wolf wolf = (Wolf) Bukkit.getEntity(wolfID);

						AttributeInstance attributeDamage = wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);

						double multiplier = mainConfig.getDouble("wolf.damage.damage_lvl_multiplier");

						double oldDamage = attributeDamage.getBaseValue();
						double newDamage = oldDamage * multiplier;

						attributeDamage.setBaseValue(newDamage);

						wolfsConfig.set("wolfs." + element + ".level.damage", newDamageLevel);
						wolfsConfig.set("wolfs." + element + ".stats.curDamage", newDamage);
						AttackDog.wolfsCM.saveConfig();

						String output = messagesConfig.getString("messages.message.update_damage.output");
						output = output.replaceAll("%olddamage%", String.format("%.2f", oldDamage));
						output = output.replaceAll("%newdamage%", String.format("%.2f", newDamage));
						output = output.replaceAll("%olddamagelevel%", String.valueOf(oldDamageLevel));
						output = output.replaceAll("%newdamagelevel%", String.valueOf(newDamageLevel));
						output = output.replaceAll("%cost%", String.format("%.2f", cost));
						output = output.replaceAll("%playerbalance%", String.format("%.2f", playerBalance));

						p.sendMessage(TextModule.color(output));
					}
				}
				e.getView().close();
			}

			/*
			 * Speed
			 */
			if (e.getSlot() == guiConfig.getInt("gui.buttons.update_speed.slot")) {
				ConfigurationSection configSection = wolfsConfig.getConfigurationSection("wolfs");
				for (String element : configSection.getKeys(false)) {
					if (wolfsConfig.getString("wolfs." + element + ".owner.uuid").equals(p.getUniqueId().toString())) {
						int oldSpeedLevel = wolfsConfig.getInt("wolfs." + element + ".level.speed");
						int newSpeedLevel = oldSpeedLevel + 1;

						if (newSpeedLevel > mainConfig.getInt("wolf.speed.max_level")) {
							e.getView().close();
							p.sendMessage(TextModule.color(messagesConfig.getString("messages.error.max_level")));
							return;
						}

						double costBase = mainConfig.getDouble("wolf.speed.cost");
						double costMultiplier = mainConfig.getDouble("wolf.speed.cost_lvl_multiplier");
						double cost = costBase * newSpeedLevel * costMultiplier * costMultiplier;
						double playerBalance = eco.getBalance(p);
						if (playerBalance < cost) {
							e.getView().close();
							double remaining = cost - playerBalance;
							String output = messagesConfig.getString("messages.error.not_enough_money");
							output = output.replaceAll("%cost%", String.format("%.2f", remaining));
							p.sendMessage(TextModule.color(output));
							return;
						}
						;

						eco.withdrawPlayer(p, cost);

						UUID wolfID = UUID.fromString(wolfsConfig.getString("wolfs." + element + ".uuid"));
						Wolf wolf = (Wolf) Bukkit.getEntity(wolfID);

						AttributeInstance attributeSpeed = wolf.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);

						double multiplier = mainConfig.getDouble("wolf.speed.speed_lvl_multiplier");

						double oldSpeed = attributeSpeed.getValue();
						double newSpeed = oldSpeed * multiplier;

						attributeSpeed.setBaseValue(newSpeed);

						wolfsConfig.set("wolfs." + element + ".level.speed", newSpeedLevel);
						wolfsConfig.set("wolfs." + element + ".stats.curSpeed", newSpeed);
						AttackDog.wolfsCM.saveConfig();

						String output = messagesConfig.getString("messages.message.update_speed.output");
						output = output.replaceAll("%oldspeed%", String.format("%.2f", oldSpeed));
						output = output.replaceAll("%newspeed%", String.format("%.2f", newSpeed));
						output = output.replaceAll("%oldspeedlevel%", String.valueOf(oldSpeedLevel));
						output = output.replaceAll("%newspeedlevel%", String.valueOf(newSpeedLevel));
						output = output.replaceAll("%cost%", String.format("%.2f", cost));
						output = output.replaceAll("%playerbalance%", String.format("%.2f", playerBalance));

						p.sendMessage(TextModule.color(output));
					}
				}
				e.getView().close();
			}

			/*
			 * Armor
			 */
			if (e.getSlot() == guiConfig.getInt("gui.buttons.update_armor.slot")) {
				ConfigurationSection configSection = wolfsConfig.getConfigurationSection("wolfs");
				for (String element : configSection.getKeys(false)) {
					if (wolfsConfig.getString("wolfs." + element + ".owner.uuid").equals(p.getUniqueId().toString())) {
						int oldArmorLevel = wolfsConfig.getInt("wolfs." + element + ".level.armor");
						int newArmorLevel = oldArmorLevel + 1;

						if (newArmorLevel > mainConfig.getInt("wolf.armor.max_level")) {
							e.getView().close();
							p.sendMessage(TextModule.color(messagesConfig.getString("messages.error.max_level")));
							return;
						}

						double costBase = mainConfig.getDouble("wolf.armor.cost");
						double costMultiplier = mainConfig.getDouble("wolf.armor.cost_lvl_multiplier");
						double cost = costBase * newArmorLevel * costMultiplier * costMultiplier;
						double playerBalance = eco.getBalance(p);
						if (playerBalance < cost) {
							e.getView().close();
							double remaining = cost - playerBalance;
							String output = messagesConfig.getString("messages.error.not_enough_money");
							output = output.replaceAll("%cost%", String.format("%.2f", remaining));
							p.sendMessage(TextModule.color(output));
							return;
						}
						;

						eco.withdrawPlayer(p, cost);

						UUID wolfID = UUID.fromString(wolfsConfig.getString("wolfs." + element + ".uuid"));
						Wolf wolf = (Wolf) Bukkit.getEntity(wolfID);

						AttributeInstance attributeArmor = wolf.getAttribute(Attribute.GENERIC_ARMOR);

						double multiplier = mainConfig.getDouble("wolf.armor.armor_lvl_multiplier");

						double oldArmor = attributeArmor.getValue();
						double newArmor = oldArmor * multiplier;
						
						System.out.println(attributeArmor.getBaseValue());

						attributeArmor.setBaseValue(newArmor);

						wolfsConfig.set("wolfs." + element + ".level.armor", newArmorLevel);
						wolfsConfig.set("wolfs." + element + ".stats.curArmor", newArmor);
						AttackDog.wolfsCM.saveConfig();

						String output = messagesConfig.getString("messages.message.update_armor.output");
						output = output.replaceAll("%oldarmor%", String.format("%.2f", oldArmor));
						output = output.replaceAll("%newarmor%", String.format("%.2f", newArmor));
						output = output.replaceAll("%oldarmorlevel%", String.valueOf(oldArmorLevel));
						output = output.replaceAll("%newarmorlevel%", String.valueOf(newArmorLevel));
						output = output.replaceAll("%cost%", String.format("%.2f", cost));
						output = output.replaceAll("%playerbalance%", String.format("%.2f", playerBalance));

						p.sendMessage(TextModule.color(output));
					}
				}
				e.getView().close();
			}
		}
	}
}
