package com.Geisteskranken.FinchyBlockTracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.BlockDestroyHook;
import net.canarymod.hook.player.BlockPlaceHook;
import net.canarymod.plugin.PluginListener;

public class FinchyEventHandler implements PluginListener {

	public static final ExecutorService SQLQueue = Executors
			.newFixedThreadPool(2);

	@HookHandler
	public boolean onBlockBreak(BlockDestroyHook evt) {
		if (FinchyBlockTracker.Track == true) {
			Player p = evt.getPlayer();
			String Player = p.getName();

			Block b = evt.getBlock();
			Short BlockType = b.getType().getId();

			String Block = BlockType.toString();

			int X = b.getX();
			int Y = b.getY();
			int Z = b.getZ();

			SQLQueue.execute(new Runnable() {
				public void run() {
					FinchyBlockTrackerSQL.insertBlockBreak(Player, X, Y, Z,
							getTime(), Block);
				}
			});
			return true;
		}
		return true;
	}

	@HookHandler
	public boolean OnPlace(BlockPlaceHook evt) {
		if (FinchyBlockTracker.Track == true) {
			Player p = evt.getPlayer();
			String Player = p.getName();

			Block b = evt.getBlockPlaced();
			Short BlockType = b.getType().getId();

			String Block = BlockType.toString();

			int X = b.getX();
			int Y = b.getY();
			int Z = b.getZ();

			SQLQueue.execute(new Runnable() {
				public void run() {
					FinchyBlockTrackerSQL.insertBlockPlace(Player, X, Y, Z,
							getTime(), Block);
				}
			});
			return true;
		}
		return true;

	}

	public String getTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

}
