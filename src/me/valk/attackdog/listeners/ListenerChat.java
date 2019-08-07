package me.valk.attackdog.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.valk.attackdog.AttackDog;
import me.valk.attackdog.utils.TextModule;

public class ListenerChat implements Listener {
	public static List<UUID> inputName = new ArrayList<UUID>();
	
	@EventHandler
	private void chatEvent(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (inputName.contains(p.getUniqueId())) {
			e.setCancelled(true);
			
			YamlConfiguration wolfsConfig = AttackDog.wolfsConfig;
			YamlConfiguration messagesConfig = AttackDog.messagesConfig;
			YamlConfiguration mainConfig = AttackDog.mainConfig;
			
			if (e.getMessage().equalsIgnoreCase("EXIT")) {
				inputName.remove(p.getUniqueId());
				p.sendMessage(TextModule.color(messagesConfig.getString("messages.message.exited_input_mode")));
				return;
			}
			
			ConfigurationSection configSection = wolfsConfig.getConfigurationSection("wolfs");
			for (String element : configSection.getKeys(false)) {
				if (wolfsConfig.getString("wolfs." + element + ".owner.uuid").equals(p.getUniqueId().toString())) {
					UUID wolfID = UUID.fromString(wolfsConfig.getString("wolfs." + element + ".uuid"));
					Wolf wolf = (Wolf) Bukkit.getEntity(wolfID);
					
					String oldName = wolf.getCustomName();
					String newName = "";
					if (mainConfig.getBoolean("wolf.one_word_name")) {
						newName = e.getMessage().split(" ")[0];
					} else {
						newName = e.getMessage();
					}
					
					if (oldName == null) {
						oldName = wolf.getName();
					}
					
					int maxNameLength = mainConfig.getInt("wolf.max_name_length");
					if (newName.length() > maxNameLength) {
						String output = messagesConfig.getString("messages.error.name_too_long");
						output = output.replaceAll("%maxnamelength%", String.valueOf(maxNameLength));
						p.sendMessage(TextModule.color(output));
						return;
					}
					
					wolf.setCustomName(TextModule.color(newName));
					
					String output = messagesConfig.getString("messages.message.update_name.output");
					
					output = output.replaceAll("%oldname%", oldName);
					output = output.replaceAll("%newname%", newName);
					
					p.sendMessage(TextModule.color(output));
				}
			}
			
			inputName.remove(p.getUniqueId());
		}
	}
}
