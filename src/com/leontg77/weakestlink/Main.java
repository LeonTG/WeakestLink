package com.leontg77.weakestlink;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.leontg77.weakestlink.commands.WeakestLinkCommand;

/**
 * Main class of the plugin.
 * 
 * @author LeonTG77
 */
public class Main extends JavaPlugin {
	public static final String PREFIX = "§6Weakest Link §8» §f";

	@Override
	public void onDisable() {
		PluginDescriptionFile file = getDescription();
		getLogger().info(file.getName() + " is now disabled.");
	}
	
	@Override
	public void onEnable() {
		PluginDescriptionFile file = getDescription();
		getLogger().info(file.getName() + " v" + file.getVersion() + " is now enabled.");
		getLogger().info("Plugin is made by LeonTG77.");
		
		KillingTask task = new KillingTask(this);
		WeakestLinkCommand cmd = new WeakestLinkCommand(this, task);
		
		// register command.
		getCommand("weakestlink").setExecutor(cmd);
		getCommand("weakestlink").setTabCompleter(cmd);
	}
	
	/**
	 * Send the given message to all online players.
	 * 
	 * @param message The message to send.
	 */
	public void broadcast(String message) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			online.sendMessage(message);
		}
		
		Bukkit.getLogger().info(message);
	}
}