package com.leontg77.weakestlink;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.leontg77.weakestlink.cmds.WeakestLinkCommand;

/**
 * Main class of the plugin.
 * 
 * @author LeonTG77
 */
public class Main extends JavaPlugin {
	public static final String PREFIX = "PREFIX HERE!!! ";
	public static Main plugin;

	@Override
	public void onDisable() {
		// print a message to the console saying it has been disabled.
		PluginDescriptionFile file = getDescription();
		getLogger().info(file.getName() + " has been disabled.");
		
		// set the plugin field to null.
		plugin = null;
	}
	
	@Override
	public void onEnable() {
		// print a message to the console saying it has been enabled.
		PluginDescriptionFile file = getDescription();
		getLogger().info(file.getName() + " v" + file.getVersion() + " has been enabled.");
		getLogger().info("Plugin is made by LeonTG77.");
		
		// register the /vs command.
		getCommand("weakestlink").setExecutor(new WeakestLinkCommand());
		
		// set the plugin field to this class.
		plugin = this;
	}
}