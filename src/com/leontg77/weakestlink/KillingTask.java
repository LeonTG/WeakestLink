package com.leontg77.weakestlink;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.base.Optional;
import com.google.common.collect.Ordering;

/**
 * Killing task class.
 * 
 * @author LeonTG77
 */
public class KillingTask extends BukkitRunnable {
	public static final Optional<GameMode> SPECTATOR_GAMEMODE_OPTIONAL;

	static {
		GameMode spectatorGameMode = null;
		
		try {
			spectatorGameMode = GameMode.valueOf("SPECTATOR");
		} catch (Exception ignored) {}
		
		SPECTATOR_GAMEMODE_OPTIONAL = Optional.fromNullable(spectatorGameMode);
	}
	
	private final Main plugin;
	
	/**
	 * Killing task class constructor.
	 * 
	 * @param plugin The main class.
	 */
	public KillingTask(Main plugin) {
		this.plugin = plugin;
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

	@Override
	public void run() {
		List<Player> list = new ArrayList<Player>();
		
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (SPECTATOR_GAMEMODE_OPTIONAL.isPresent() && online.getGameMode() == SPECTATOR_GAMEMODE_OPTIONAL.get()) {
				continue;
			}
			
			list.add(online);
		}
		
		Player toKill = BY_HEALTH.min(list);

		if (isAllAtSameHealth() || toKill == null) {
			plugin.broadcast(Main.PREFIX + "You were lucky, there were no one to kill.");
			return;
		}

		plugin.broadcast(Main.PREFIX + toKill.getName() + " was on the lowest health and got perished.");
		
		// the damaging is so they get the damage sound when taking the damage to die.
		toKill.damage(0);
		toKill.setHealth(0);
	}
	
	/**
	 * Check if all online players are at the same health.
	 * <p>
	 * Doesn't count players in gamemode 3.
	 * 
	 * @return True if they are, false otherwise.
	 */
	private boolean isAllAtSameHealth() {
		boolean isSameHealth = true;
		Player last = null;
		
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (SPECTATOR_GAMEMODE_OPTIONAL.isPresent() && online.getGameMode() == SPECTATOR_GAMEMODE_OPTIONAL.get()) {
				continue;
			}
			
			if (last != null && last.getHealth() != online.getHealth()) {
				isSameHealth = false;
				break;
			}
			
			last = online;
		}
		
		return isSameHealth;
	}
}