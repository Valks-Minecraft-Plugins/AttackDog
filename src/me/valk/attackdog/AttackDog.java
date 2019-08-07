package me.valk.attackdog;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.valk.attackdog.commands.CmdAttackDog;
import me.valk.attackdog.configs.ConfigItem;
import me.valk.attackdog.configs.ConfigManager;
import me.valk.attackdog.listeners.ListenerChat;
import me.valk.attackdog.listeners.ListenerEntityDeath;
import me.valk.attackdog.listeners.ListenerEntityInteract;
import me.valk.attackdog.listeners.ListenerGUI;
import me.valk.attackdog.listeners.ListenerPlayerJoin;
import me.valk.attackdog.utils.ItemModule;
import net.milkbowl.vault.economy.Economy;

public class AttackDog extends JavaPlugin {
	public static Economy economy = null;
	public static File pluginFolder;
	
	public static ConfigManager mainCM;
	public static YamlConfiguration mainConfig;
	
	public static ConfigManager messagesCM;
	public static YamlConfiguration messagesConfig;
	
	public static ConfigManager guiCM;
	public static YamlConfiguration guiConfig;
	
	public static ConfigManager wolfsCM;
	public static YamlConfiguration wolfsConfig;
	
	@Override
	public void onEnable() {
		pluginFolder = getDataFolder();
		
		initConfigs();
		registerListeners(getServer().getPluginManager());
		registerCommands();
		setupEconomy();
	}
	
	private void registerListeners(PluginManager pm) {
		pm.registerEvents(new ListenerEntityInteract(), this);
		pm.registerEvents(new ListenerGUI(), this);
		pm.registerEvents(new ListenerChat(), this);
		pm.registerEvents(new ListenerPlayerJoin(), this);
		pm.registerEvents(new ListenerEntityDeath(), this);
	}
	
	private void registerCommands() {
		getCommand("attackdog").setExecutor(new CmdAttackDog());
	}
	
	private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
	
	public static Economy getEconomy() {
        return economy;
    }
	
	private void initConfigs() {
		mainCM = new ConfigManager("config");
		mainConfig = mainCM.getConfig();
		
		messagesCM = new ConfigManager("messages");
		messagesConfig = messagesCM.getConfig();
		
		guiCM = new ConfigManager("gui");
		guiConfig = guiCM.getConfig();
		
		wolfsCM = new ConfigManager("wolfs");
		wolfsConfig = wolfsCM.getConfig();
		
		initMainConfig();
		initMessagesConfig();
		initGUIConfig();
		initWolfsConfig();
	}
	
	private void initMainConfig() {
		defaultSet(mainConfig, "wolf.teleport_on_join", true);
		defaultSet(mainConfig, "wolf.tamed_on_spawn", true);
		defaultSet(mainConfig, "wolf.max_name_length", 20);
		defaultSet(mainConfig, "wolf.one_word_name", true);
		
		defaultSet(mainConfig, "wolf.summon.cooldown", 60);
		
		defaultSet(mainConfig, "wolf.health.cost", 100);
		defaultSet(mainConfig, "wolf.health.cost_lvl_multiplier", 1.2);
		defaultSet(mainConfig, "wolf.health.health_lvl_multiplier", 1.2);
		defaultSet(mainConfig, "wolf.health.initial_amount", 10);
		defaultSet(mainConfig, "wolf.health.max_level", 5);
		
		defaultSet(mainConfig, "wolf.damage.cost", 250);
		defaultSet(mainConfig, "wolf.damage.cost_lvl_multiplier", 1.2);
		defaultSet(mainConfig, "wolf.damage.damage_lvl_multiplier", 1.2);
		defaultSet(mainConfig, "wolf.damage.initial_amount", 2.0);
		defaultSet(mainConfig, "wolf.damage.max_level", 5);
		
		defaultSet(mainConfig, "wolf.speed.cost", 125);
		defaultSet(mainConfig, "wolf.speed.cost_lvl_multiplier", 1.2);
		defaultSet(mainConfig, "wolf.speed.speed_lvl_multiplier", 1.2);
		defaultSet(mainConfig, "wolf.speed.initial_amount", 0.2);
		defaultSet(mainConfig, "wolf.speed.max_level", 5);
		
		defaultSet(mainConfig, "wolf.armor.cost", 125);
		defaultSet(mainConfig, "wolf.armor.cost_lvl_multiplier", 1.2);
		defaultSet(mainConfig, "wolf.armor.armor_lvl_multiplier", 1.2);
		defaultSet(mainConfig, "wolf.armor.initial_amount", 3);
		defaultSet(mainConfig, "wolf.armor.max_level", 5);
		mainCM.saveConfig();
	}
	
	private void initMessagesConfig() {
		defaultSet(messagesConfig, "messages.error.no_args", "&cPlease specify valid arguments.");
		defaultSet(messagesConfig, "messages.error.no_perms", "&cYou do not have permission to do that.");
		defaultSet(messagesConfig, "messages.error.cooldown", "&cYou're on a cooldown, please wait!");
		defaultSet(messagesConfig, "messages.error.not_enough_money", "&cYou need $%cost% more to do that!");
		defaultSet(messagesConfig, "messages.error.name_too_long", "&cName can't be longer than %maxnamelength% characters! Type EXIT to leave this mode.");
		defaultSet(messagesConfig, "messages.error.max_level", "&cYou reached the max level for this upgrade!");
		
		defaultSet(messagesConfig, "messages.message.exited_input_mode", "&7Exited input mode.");
		defaultSet(messagesConfig, "messages.message.summoned", "&2Summoned a wolf!");
		defaultSet(messagesConfig, "messages.message.update_name.input", "&7Input a new name for your wolf.");
		defaultSet(messagesConfig, "messages.message.update_name.output", "&7Your wolfs name was set from &f%oldname% &7to &f%newname%.");
		defaultSet(messagesConfig, "messages.message.update_health.output", "&7Upgraded your wolfs health to level &f%newhealthlevel% &7for &f$%cost%&7. Your balance is now at &f$%playerbalance%&7. (&f%oldhealth% &7-> &f%newhealth%&7)");
		defaultSet(messagesConfig, "messages.message.update_damage.output", "&7Upgraded your wolfs damage to level &f%newdamagelevel% &7for &f$%cost%&7. Your balance is now at &f$%playerbalance%&7. (&f%olddamage% &7-> &f%newdamage%&7)");
		defaultSet(messagesConfig, "messages.message.update_speed.output", "&7Upgraded your wolfs speed to level &f%newspeedlevel% &7for &f$%cost%&7. Your balance is now at &f$%playerbalance%&7. (&f%oldspeed% &7-> &f%newspeed%&7)");
		defaultSet(messagesConfig, "messages.message.update_armor.output", "&7Upgraded your wolfs armor to level &f%newarmorlevel% &7for &f$%cost%&7. Your balance is now at &f$%playerbalance%&7. (&f%oldarmor% &7-> &f%newarmor%&7)");
		
		messagesCM.saveConfig();
	}
	
	private void initWolfsConfig() {
		if (!wolfsConfig.isSet("wolfs.1")) {
			wolfsConfig.set("wolfs.1.uuid", "Template");
			wolfsConfig.set("wolfs.1.name", "Example Wolf");
			wolfsConfig.set("wolfs.1.level.health", 0);
			wolfsConfig.set("wolfs.1.level.damage", 0);
			wolfsConfig.set("wolfs.1.level.speed", 0);
			wolfsConfig.set("wolfs.1.level.armor", 0);
			wolfsConfig.set("wolfs.1.stats.tamed", true);
			wolfsConfig.set("wolfs.1.stats.curHealth", 0);
			wolfsConfig.set("wolfs.1.stats.curDamage", 0);
			wolfsConfig.set("wolfs.1.stats.curSpeed", 0);
			wolfsConfig.set("wolfs.1.stats.curArmor", 0);
			wolfsConfig.set("wolfs.1.owner.uuid", "Preset");
			wolfsConfig.set("wolfs.1.owner.name", "valkyrienyanko");
		}
		wolfsCM.saveConfig();
	}
	
	private void initGUIConfig() {
		ConfigItem configItem = new ConfigItem(guiCM);
		defaultSet(guiConfig, "gui.rows", 1);
		
		if (!guiConfig.isSet("gui.buttons.update_name")) {
			ItemStack item = ItemModule.item("&fName", "&7Change Name", Material.NAME_TAG);
			ItemMeta im = item.getItemMeta();
			im.addEnchant(Enchantment.MENDING, 1, false);
			item.setItemMeta(im);
			configItem.set("gui.buttons.update_name", item);
			guiConfig.set("gui.buttons.update_name.slot", 0);
		}
		
		if (!guiConfig.isSet("gui.buttons.update_health")) {
			configItem.set("gui.buttons.update_health", ItemModule.item("&fHealth", "&7Upgrade Health\n&7Cost: &f$%amount%\n&7Level: &f%level%", Material.APPLE));
			guiConfig.set("gui.buttons.update_health.slot", 1);
		}
		
		if (!guiConfig.isSet("gui.buttons.update_damage")) {
			ItemStack item = ItemModule.item("&fDamage", "&7Upgrade Damage\n&7Cost: &f$%amount%\n&7Level: &f%level%", Material.WOOD_SWORD);
			ItemMeta im = item.getItemMeta();
			im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			item.setItemMeta(im);
			configItem.set("gui.buttons.update_damage", item);
			guiConfig.set("gui.buttons.update_damage.slot", 2);
		}
		
		if (!guiConfig.isSet("gui.buttons.update_speed")) {
			configItem.set("gui.buttons.update_speed", ItemModule.item("&fSpeed", "&7Upgrade Speed\n&7Cost: &f$%amount%\n&7Level: &f%level%", Material.FEATHER));
			guiConfig.set("gui.buttons.update_speed.slot", 3);
		}
		
		if (!guiConfig.isSet("gui.buttons.update_armor")) {
			ItemStack item = ItemModule.item("&fArmor", "&7Upgrade Armor\n&7Cost: &f$%amount%\n&7Level: &f%level%", Material.IRON_CHESTPLATE);
			ItemMeta im = item.getItemMeta();
			im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			item.setItemMeta(im);
			configItem.set("gui.buttons.update_armor", item);
			guiConfig.set("gui.buttons.update_armor.slot", 4);
		}
		guiCM.saveConfig();
	}
	
	private void defaultSet(YamlConfiguration config, String path, Object value) {
		if (!config.isSet(path)) {
			config.set(path, value);
		}
	}
}
