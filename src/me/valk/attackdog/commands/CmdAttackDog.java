package me.valk.attackdog.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import me.valk.attackdog.AttackDog;
import me.valk.attackdog.utils.Cooldown;
import me.valk.attackdog.utils.TextModule;
import me.valk.attackdog.utils.Utils;

public class CmdAttackDog implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("attackdog")) {
			if (args.length < 1) {
				sender.sendMessage(TextModule.color(AttackDog.messagesConfig.getString("messages.error.no_args")));
				return true;
			}
			
			if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("attackdog.reload")) {
				sender.sendMessage(TextModule.color("&7Reloaded all configs."));
				AttackDog.guiCM.saveConfig();
				AttackDog.mainCM.saveConfig();
				AttackDog.messagesCM.saveConfig();
				AttackDog.wolfsCM.saveConfig();
				return true;
			}
			
			if (args[0].equalsIgnoreCase("author")) {
				sender.sendMessage(TextModule.color("&7Plugin created by &fvalkyrienyanko."));
				return true;
			}
			
			if (args[0].equalsIgnoreCase("version")) {
				sender.sendMessage(TextModule.color("&71.0.4-SNAPSHOT"));
				return true;
			}
			
			if (args[0].equalsIgnoreCase("summon")) {
				if (!sender.hasPermission("attackdog.summon")) {
					sender.sendMessage(TextModule.color(AttackDog.messagesConfig.getString("messages.error.no_perms")));
					return true;
				}
				
				Player p = (Player) sender;
				
				if (Cooldown.isInCooldown(p.getUniqueId())) {
					sender.sendMessage(TextModule.color(AttackDog.messagesConfig.getString("messages.error.cooldown")));
					return true;
				} else {
					YamlConfiguration wolfsConfig = AttackDog.wolfsConfig;
					ConfigurationSection configSection = wolfsConfig.getConfigurationSection("wolfs");
					
					for (String element : configSection.getKeys(false)) {
						if (wolfsConfig.getString("wolfs." + element + ".owner.uuid").equals(p.getUniqueId().toString())) {
							p.sendMessage("You may only have one wolf at a time.");
							return true;
						}
					}
					
					new Cooldown(p.getUniqueId(), AttackDog.mainConfig.getInt("wolf.summon.cooldown")).start();
					sender.sendMessage(TextModule.color(AttackDog.messagesConfig.getString("messages.message.summoned")));
					
					Utils.registerWolf(p);
				}
			}
		}
		return true;
	}
}
