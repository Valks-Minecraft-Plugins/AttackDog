package me.valk.attackdog.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.valk.attackdog.AttackDog;
import me.valk.attackdog.configs.ConfigItem;

public class GUI {
	public static Inventory wolfGUI(UUID id) {
		ConfigItem configItem = new ConfigItem(AttackDog.guiCM);
		YamlConfiguration guiConfig = AttackDog.guiConfig;
		YamlConfiguration mainConfig = AttackDog.mainConfig;
		YamlConfiguration wolfsConfig = AttackDog.wolfsConfig;
		
		Inventory inv = Bukkit.createInventory(null, 9, "Wolf");
		
		ItemStack updateName = configItem.get("gui.buttons.update_name");
		ItemStack updateHealth = configItem.get("gui.buttons.update_health");
		ItemStack updateDamage = configItem.get("gui.buttons.update_damage");
		ItemStack updateSpeed = configItem.get("gui.buttons.update_speed");
		
		double costHealthMultiplier = mainConfig.getDouble("wolf.health.cost_lvl_multiplier");
		double costDamageMultiplier = mainConfig.getDouble("wolf.damage.cost_lvl_multiplier");
		double costSpeedMultiplier = mainConfig.getDouble("wolf.speed.cost_lvl_multiplier");
		
		double costBaseHealth = mainConfig.getDouble("wolf.health.cost");
		double costBaseDamage = mainConfig.getDouble("wolf.damage.cost");
		double costBaseSpeed = mainConfig.getDouble("wolf.speed.cost");
		
		int wolfHealthLvl = 0;
		int wolfDamageLvl = 0;
		int wolfSpeedLvl = 0;
		
		ConfigurationSection configSection = wolfsConfig.getConfigurationSection("wolfs");
		for (String element : configSection.getKeys(false)) {
			if (wolfsConfig.getString("wolfs." + element + ".uuid").equals(id.toString())) {
				wolfHealthLvl = wolfsConfig.getInt("wolfs." + element + ".level.health");
				wolfDamageLvl = wolfsConfig.getInt("wolfs." + element + ".level.damage");
				wolfSpeedLvl = wolfsConfig.getInt("wolfs." + element + ".level.speed");
			}
		}
		
		double costHealth = costBaseHealth * (wolfHealthLvl + 1) * costHealthMultiplier * costHealthMultiplier;
		double costDamage = costBaseDamage * (wolfDamageLvl + 1) * costDamageMultiplier * costDamageMultiplier;
		double costSpeed = costBaseSpeed * (wolfSpeedLvl + 1) * costSpeedMultiplier * costSpeedMultiplier;

		replacePlaceholderAmount(updateHealth, String.valueOf(costHealth), String.valueOf(wolfHealthLvl));
		replacePlaceholderAmount(updateDamage, String.valueOf(costDamage), String.valueOf(wolfDamageLvl));
		replacePlaceholderAmount(updateSpeed, String.valueOf(costSpeed), String.valueOf(wolfSpeedLvl));
		
		inv.setItem(guiConfig.getInt("gui.buttons.update_name.slot"), updateName);
		inv.setItem(guiConfig.getInt("gui.buttons.update_health.slot"), updateHealth);
		inv.setItem(guiConfig.getInt("gui.buttons.update_damage.slot"), updateDamage);
		inv.setItem(guiConfig.getInt("gui.buttons.update_speed.slot"), updateSpeed);
		return inv;
	}
	
	private static void replacePlaceholderAmount(ItemStack item, String amount, String level) {
		ItemMeta im = item.getItemMeta();
		List<String> list = new ArrayList<String>();
		List<String> lore = im.getLore();
		for (String line : lore) {
			String r1 = line.replaceAll("%amount%", amount);
			String r2 = r1.replaceAll("%level%", level);
			list.add(r2);
		}
		im.setLore(list);
		item.setItemMeta(im);
	}
}
