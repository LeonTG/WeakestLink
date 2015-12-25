package com.leontg77.weakestlink;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Ordering;

public class KillingTask extends BukkitRunnable {
	
	private final Ordering<Player> BY_HEALTH = new Ordering<Player>() {
        @Override
        public int compare(Player p1, Player p2) {
            return Double.compare(p1.getHealth(), p2.getHealth());
        }
    };

	@Override
	public void run() {
		Player toKill = BY_HEALTH.min(Bukkit.getOnlinePlayers());
		
		// if to kill is null, stop.
		// not that it should be null...
		if (toKill == null) {
			return;
		}

		Bukkit.broadcastMessage(Main.PREFIX + toKill.getName() + " was on the lowest health and got perished.");
		
		// set their health to 0 and damage them so they hear the damage sound.
		toKill.setHealth(0);
		toKill.damage(0);
	}
}
