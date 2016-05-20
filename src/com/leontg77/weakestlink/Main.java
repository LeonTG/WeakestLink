/**
 * Project: WeakestLink
 * Class: com.leontg77.weakestlink.Main
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

package com.leontg77.weakestlink;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import com.leontg77.weakestlink.commands.WeakestLinkCommand;

/**
 * Main class of the plugin.
 * 
 * @author LeonTG77
 */
public class Main extends JavaPlugin {
    private static final Optional<GameMode> SPECTATOR_GAMEMODE_OPTIONAL;

    static {
        GameMode spectatorGameMode = null;
        
        try {
            spectatorGameMode = GameMode.valueOf("SPECTATOR");
        } catch (Exception ignored) {}
        
        SPECTATOR_GAMEMODE_OPTIONAL = Optional.fromNullable(spectatorGameMode);
    }
    
	public static final String PREFIX = "§aWeakest Link §8» §f";

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
    
    /**
     * Ordering of everyones health.
     */
    private static final Ordering<Player> BY_HEALTH = new Ordering<Player>() {
        @Override
        public int compare(Player p1, Player p2) {
            return Double.compare(p1.getHealth(), p2.getHealth());
        }
    };
    
    /**
     * Spectator predicate.
     */
    private static final Predicate<Player> IS_SPECTATOR = new Predicate<Player>() {
        @Override
        public boolean apply(Player player) {
            return SPECTATOR_GAMEMODE_OPTIONAL.isPresent() ? player.getGameMode() == SPECTATOR_GAMEMODE_OPTIONAL.get() : player.getGameMode() == GameMode.CREATIVE;
        }
    };
    
    private static final Predicate<Player> NOT_SPECTATOR = Predicates.not(IS_SPECTATOR);
	    
    /**
     * Get the lowest online player by their health.
     * 
     * @return The lowest player.
     */
	public Player getLowestPlayer() {
	    Player lowest = BY_HEALTH.min(Iterables.filter(Bukkit.getOnlinePlayers(), NOT_SPECTATOR));

        if (isAllAtSameHealth() || lowest == null) {
            return null;
        }
        
        return lowest;
	}
    
    /**
     * Check if all online players are at the same health.
     * <p>
     * Doesn't count players in gamemode 3 (or gm 1 in 1.7).
     * 
     * @return True if they are, false otherwise.
     */
    private boolean isAllAtSameHealth() {
        boolean isSameHealth = true;
        Player last = null;
        
        for (Player online : Iterables.filter(Bukkit.getOnlinePlayers(), NOT_SPECTATOR)) {
            if (last != null && last.getHealth() != online.getHealth()) {
                isSameHealth = false;
                break;
            }
            
            last = online;
        }
        
        return isSameHealth;
    }
}