package me.valk.attackdog.configs;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import me.valk.attackdog.AttackDog;

public class ConfigManager {
	private File file;
	private YamlConfiguration config;

	public ConfigManager(String name) {
		file = new File(AttackDog.pluginFolder, name + ".yml");
		config = YamlConfiguration.loadConfiguration(file);
	}

	public void saveConfig() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(file);
	}

	public YamlConfiguration getConfig() {
		return config;
	}
}
