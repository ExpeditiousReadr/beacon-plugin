package main;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Beacon;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import commands.BeaconLister;
import listeners.BlockListener;
import listeners.ChunkListener;
import listeners.PlayerListener;
	
/*
 * BEACONS V1
 * 
 * OVERVIEW:
 * beacons give mining fatigue to any player in a specified radius unless that player is on the whitelist for that beacon
 * 
 * if you have any bugs or suggestions let me know!
 * 
 * 
 */
public class Main extends JavaPlugin {
	
	private static Main plugin;
	private List<Location> beacons;
	private NamespacedKey beaconWhitelist = new NamespacedKey(this, "beacon_whitelist");
	
	
	@Override
	public void onEnable() {
		
		plugin = this;
		
		initListeners(this.getServer().getPluginManager());
		
		beacons = Lists.newArrayList();
		
		loadLoadedBeacons();
		
		this.getCommand("blist").setExecutor(new BeaconLister());
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public void initListeners(PluginManager p) {
		p.registerEvents(new BlockListener(), this);
		p.registerEvents(new ChunkListener(), this);
		p.registerEvents(new PlayerListener(), this);
	}
	
	private void loadLoadedBeacons() {
		Bukkit.getWorlds().forEach(world -> {
			Arrays.asList(world.getLoadedChunks()).forEach(chunk -> {
				Arrays.asList(chunk.getTileEntities()).forEach(TileEntity -> {
					if(TileEntity instanceof Beacon && !getBeacons().contains(TileEntity.getLocation())) {
						getBeacons().add(TileEntity.getLocation());
					}
				});
			});
		});
	}
	
	public static Main getPlugin() {
		return plugin;
	}
	
	public List<Location> getBeacons() {
		return beacons;
	}
	
	public NamespacedKey getBeaconWhitelist() {
		return beaconWhitelist;
	}
}
