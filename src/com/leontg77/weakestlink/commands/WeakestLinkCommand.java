/**
 * Project: WeakestLink
 * Class: com.leontg77.weakestlink.commands.WeakestLinkCommand
 *
 * The MIT License (MIT)
 * 
 * Copyright (c) 2016 Leon Vaktskjold <leontg77@gmail.com>.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.leontg77.weakestlink.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.leontg77.weakestlink.KillingTask;
import com.leontg77.weakestlink.Main;

/**
 * WeakestLink command class.
 * 
 * @author LeonTG77
 */
public class WeakestLinkCommand implements CommandExecutor, TabCompleter {
    private static final String PERMISSION = "weakestlink.manage";

    private final KillingTask task;
    private final Main plugin;
    
    /**
     * WeakestLink command class constructor.
     * 
     * @param plugin The main class.
     * @param task The killing runnable task.
     */
    public WeakestLinkCommand(Main plugin, KillingTask task) {
        this.plugin = plugin;
        this.task = task;
    }
    
    private boolean enabled = false;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Main.PREFIX + "Usage: /weakestlink <info|enable|disable>");
            return true;
        }
        
        if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(Main.PREFIX + "Scenario creator: §a/u/???");
            sender.sendMessage(Main.PREFIX + "Plugin creator: §aLeonTG77");
            sender.sendMessage(Main.PREFIX + "Version: §a" + plugin.getDescription().getVersion());
            sender.sendMessage(Main.PREFIX + "Description:");
            sender.sendMessage("§8» §f" + plugin.getDescription().getDescription());
            return true;
        }
        
        if (args[0].equalsIgnoreCase("info")) {
            Player lowest = plugin.getLowestPlayer();
            
            if (lowest == null) {
                sender.sendMessage(Main.PREFIX + "Everyone is at the same health.");
                return true;
            }
            
            sender.sendMessage(Main.PREFIX + "The lowest player is: §a" + lowest.getName());
            return true;
        }
        
        if (args[0].equalsIgnoreCase("enable")) {
            if (!sender.hasPermission(PERMISSION)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }
            
            if (enabled) {
                sender.sendMessage(Main.PREFIX + "Weakest Link is already enabled.");
                return true;
            }
            
            plugin.broadcast(Main.PREFIX + "Weakest Link has been enabled.");
            enabled = true;
            
            task.runTaskTimer(plugin, 12000, 12000);
            return true;
        }

        if (args[0].equalsIgnoreCase("disable")) {
            if (!sender.hasPermission(PERMISSION)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }
            
            if (!enabled) {
                sender.sendMessage(Main.PREFIX + "Weakest Link is not enabled.");
                return true;
            }

            plugin.broadcast(Main.PREFIX + "Weakest Link has been disabled.");
            enabled = false;
            
            task.cancel();
            return true;
        }
        
        sender.sendMessage(Main.PREFIX + "Usage: /weakestlink <info|enable|disable>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> toReturn = new ArrayList<String>();
        List<String> list = new ArrayList<String>();
        
        if (args.length != 1) {
            return toReturn;
        }
        
        list.add("info");
        
        if (sender.hasPermission(PERMISSION)) {
            list.add("enable");
            list.add("disable");
        }

        // make sure to only tab complete what starts with what they 
        // typed or everything if they didn't type anything
        for (String str : list) {
            if (args[args.length - 1].isEmpty() || str.startsWith(args[args.length - 1].toLowerCase())) {
                toReturn.add(str);
            }
        }
        
        return toReturn;
    }
}