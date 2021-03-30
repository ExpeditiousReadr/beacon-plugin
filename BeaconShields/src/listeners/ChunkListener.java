package listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Beacon;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.persistence.PersistentDataType;

import main.Main;

public class ChunkListener implements Listener {
	
	/*
	 * Have not implemented listeners for if the world is loaded/unloaded. however reloading the plugin will rescan the world
	 */
	
	Main p = Main.getPlugin();
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent e) {
		for(BlockState state : e.getChunk().getTileEntities()) {
			if(state instanceof Beacon && !p.getBeacons().contains(state.getLocation())) {
				p.getBeacons().add(state.getLocation());
				TileState beaconState = (TileState) state;
				if(!(beaconState.getPersistentDataContainer().has(p.getBeaconWhitelist(), PersistentDataType.BYTE_ARRAY))) {
					beaconState.getPersistentDataContainer().set(p.getBeaconWhitelist(), PersistentDataType.BYTE_ARRAY, new byte[0]);
					beaconState.update();
					Bukkit.broadcastMessage("added key to beacon");
				}
				Bukkit.broadcastMessage("Loaded beacon");
				return;
			}
		}
	}
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent e) {
		for(BlockState state : e.getChunk().getTileEntities()) {
			if(state instanceof Beacon && p.getBeacons().contains(state.getLocation())) {
				p.getBeacons().remove(state.getLocation());
				Bukkit.broadcastMessage("unloaded beacon");
				return;
			}
		}
	}

}
