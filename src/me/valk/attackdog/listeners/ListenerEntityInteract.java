package me.valk.attackdog.listeners;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.valk.attackdog.AttackDog;
import me.valk.attackdog.gui.GUI;

public class ListenerEntityInteract implements Listener {
	@EventHandler
	private void entityInteract(PlayerInteractEntityEvent e) {
		if (e.getHand() != EquipmentSlot.HAND)
			return;
		Material type = e.getPlayer().getEquipment().getItemInMainHand().getType();

		/*
		 * Cancel if the player wants to feed their wolf.
		 */
		switch (type) {
		case RAW_CHICKEN:
		case COOKED_CHICKEN:
		case PORK:
		case GRILLED_PORK:
		case RAW_BEEF:
		case COOKED_BEEF:
		case ROTTEN_FLESH:
		case MUTTON:
		case COOKED_MUTTON:
		case RABBIT:
		case COOKED_RABBIT:
			return;
		default:
			break;
		}
		
		/*
		 * Cancel if the player is crouching in case the player wants to make their wolf sit.
		 */
		if (e.getPlayer().isSneaking()) {
			return;
		}

		if (e.getRightClicked() instanceof Wolf) {
			YamlConfiguration wolfsConfig = AttackDog.wolfsConfig;
			ConfigurationSection configSection = wolfsConfig.getConfigurationSection("wolfs");
			for (String element : configSection.getKeys(false)) {
				if (wolfsConfig.getString("wolfs." + element + ".uuid")
						.equals(e.getRightClicked().getUniqueId().toString())) {
					UUID id = UUID.fromString(wolfsConfig.getString("wolfs." + element + ".uuid"));
					e.getPlayer().openInventory(GUI.wolfGUI(id));
				}
			}
		}
	}
}
