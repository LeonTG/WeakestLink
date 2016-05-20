/** 
 * Project: WeakestLink
 * Class: com.leontg77.weakestlink.KillingTask
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

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Killing task class.
 * 
 * @author LeonTG77
 */
public class KillingTask extends BukkitRunnable {
    private final Main plugin;
	
	/**
	 * Killing task class constructor.
	 * 
	 * @param plugin The main class.
	 */
	public KillingTask(Main plugin) {
		this.plugin = plugin;
	}
    
	@Override
	public void run() {
		Player toKill = plugin.getLowestPlayer();

		if (toKill == null) {
			plugin.broadcast(Main.PREFIX + "You were lucky, there were no one to kill.");
			return;
		}

		plugin.broadcast(Main.PREFIX + toKill.getName() + " was on the lowest health and got perished.");
		
		// the damaging is so they get the damage sound when taking the damage to die.
		toKill.damage(0);
		toKill.setHealth(0);
	}
}