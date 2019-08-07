package me.valk.attackdog.listeners;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.valk.attackdog.AttackDog;
import me.valk.attackdog.utils.Utils;

public class ListenerPlayerJoin implements Listener {
	@EventHandler
	private void playerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		YamlConfiguration wolfsConfig = AttackDog.wolfsConfig;
		YamlConfiguration mainConfig = AttackDog.mainConfig;
		ConfigurationSection configSection = wolfsConfig.getConfigurationSection("wolfs");

		for (String element : configSection.getKeys(false)) {
			if (wolfsConfig.getString("wolfs." + element + ".owner.uuid").equals(p.getUniqueId().toString())) {
				if (mainConfig.getBoolean("wolf.teleport_on_join")) {
					Utils.teleportWolfToOwner(p);
				}
			}
		}
	}
}
