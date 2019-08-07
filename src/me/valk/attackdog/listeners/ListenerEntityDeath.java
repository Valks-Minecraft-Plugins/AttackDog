package me.valk.attackdog.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import me.valk.attackdog.AttackDog;
import me.valk.attackdog.utils.Utils;

public class ListenerEntityDeath implements Listener {
	@EventHandler
	private void entityDeath(EntityDeathEvent e) {
		if (e.getEntity() instanceof Wolf) {
			YamlConfiguration wolfsConfig = AttackDog.wolfsConfig;
			ConfigurationSection configSection = wolfsConfig.getConfigurationSection("wolfs");
			
			for (String element : configSection.getKeys(false)) {
				if (wolfsConfig.getString("wolfs." + element + ".uuid").equals(e.getEntity().getUniqueId().toString())) {
					UUID playerUUID = UUID.fromString(wolfsConfig.getString("wolfs." + element + ".owner.uuid"));
					Player owner = (Player) Bukkit.getEntity(playerUUID);
					Utils.unregisterWolf(owner);
				}
			}
		}
	}
}
