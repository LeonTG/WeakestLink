package com.leontg77.weakestlink.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.leontg77.weakestlink.KillingTask;
import com.leontg77.weakestlink.Main;

/**
 * WeakestLink command.
 * <p> 
 * Command used to enable or disable the scenario.
 * 
 * @author LeonTG77
 */
public class WeakestLinkCommand implements CommandExecutor {
	private static final String PERMISSION = "weakestlink.manage";
	
	private boolean enabled = false;
	
	private KillingTask task = new KillingTask();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// check if the have permission, if not send them a message and return.
		if (!sender.hasPermission(PERMISSION)) {
			sender.sendMessage(Main.PREFIX + ChatColor.RED + "You don't have access to this.");
			return true;
		}
		
		// check if they typed anything else than the command itself, if not send usage and return.
		if (args.length == 0) {
			sender.sendMessage(Main.PREFIX + "Usage: /weakestlink <enable|disable>");
			return true;
		}
		
		// check if they typed /weakestlink enable, if so do the command.
		if (args[0].equalsIgnoreCase("enable")) {
			// check if the scenario is enabled, if not tell them so and return.
			if (enabled) {
				sender.sendMessage(Main.PREFIX + "WeakestLink is already enabled.");
				return true;
			}
			
			// send them a message and set enabled to be true
			sender.sendMessage(Main.PREFIX + "WeakestLink has been enabled.");
			enabled = true;
			
			// start the task.
			task.runTaskTimer(Main.plugin, 12000, 12000);
			return true;
		}

		// check if they typed /weakestlink enable, if so do the command.
		if (args[0].equalsIgnoreCase("disable")) {
			// check if the scenario wasn't enabled, if not tell them so and return.
			if (!enabled) {
				sender.sendMessage(Main.PREFIX + "WeakestLink is not enabled.");
				return true;
			}

			// send them a message and set enabled to be false
			sender.sendMessage(Main.PREFIX + "WeakestLink has been disabled.");
			enabled = false;
			
			// cancel the task.
			task.cancel();
			return true;
		}
		
		// they didn't type enable or disable, send usage.
		sender.sendMessage(Main.PREFIX + "Usage: /weakestlink <enable|disable>");
		return true;
	}
}