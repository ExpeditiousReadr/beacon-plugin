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
	 * takes a player as an argument
	 * checks for tier four beacon
	 * current beacon gets whitelist checked against player
	 * location calculation
	 * 		gets distance from beacon on the x and z axis
	 * 		if values are both lower than range (within square radius) returns true
	 * 
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
			
			//hardcoding the range for now
			if(x <= 10 && z <= 10) {
				return true;
			}			
		}	
		return false;
	}
}
