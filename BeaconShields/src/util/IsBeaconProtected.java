package util;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import main.Main;

public class IsBeaconProtected {
	
	/*
	 * math and conditional checks, called from PlayerListener.java
	 */
	
	private static Main p;
	
	public IsBeaconProtected() {
		p = Main.getPlugin();
	}
	
	public boolean isProtected(Location location, Player player) {
		
		Iterator<Location> list = p.getBeacons().iterator();
	
		while(list.hasNext()) {
			Location loc = list.next();
			if(loc.getBlock().getType() != Material.BEACON) {
				list.remove();
				continue;
			}
			Beacon b = (Beacon) loc.getBlock().getState();
			if(b.getTier() != 4) {
				continue;
			}
			List<String> whitelist = Serialize.getWhitelist(b.getPersistentDataContainer().get(p.getBeaconWhitelist(), PersistentDataType.BYTE_ARRAY));
			if(whitelist.contains(player.getName())) {
				continue;
			}
		
			double x = Math.abs(loc.getX() - location.getX());
			double z = Math.abs(loc.getZ() - location.getZ());
			
			/*should probably add a configuration setting for the range
			 * range is 37 because 32 block base radius + 5 blocks (which is the maximum distance a player can break blocks from) 
			 * this ensures that you can't just stand 1 block outside a 32 block radius and mine blocks inside the protected area
			 */
			if(x <= 37 && z <= 37) {
				return true;
			}			
		}	
		return false;
	}
}
