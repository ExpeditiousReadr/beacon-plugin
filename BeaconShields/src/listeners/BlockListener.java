package listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataType;

import main.Main;

public class BlockListener implements Listener {
	
	public Main p = Main.getPlugin();	

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.getBlock().getType() != Material.BEACON) {
			return;
		}		
		p.getBeacons().remove(e.getBlock().getLocation());
		Bukkit.broadcastMessage("beacon removed");
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(e.getBlock().getType() != Material.BEACON) {
			return;
		}	
		TileState beaconState = (TileState) e.getBlock().getState();
		beaconState.getPersistentDataContainer().set(p.getBeaconWhitelist(), PersistentDataType.BYTE_ARRAY, new byte[0]);
		beaconState.update();
		
		p.getBeacons().add(e.getBlock().getLocation());
		Bukkit.broadcastMessage("beacon added");			
	}		
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

