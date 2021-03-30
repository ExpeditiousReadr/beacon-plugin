package listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import main.Main;
import util.IsBeaconProtected;
import util.Serialize;

public class PlayerListener implements Listener {

	private Main plugin = Main.getPlugin();
	private IsBeaconProtected checker = new IsBeaconProtected();
	
	
	
	@EventHandler
	public void enterBeaconRadius(PlayerMoveEvent e) {
		if(e.getFrom().getBlock().getX() != e.getTo().getBlock().getX() ||
			e.getFrom().getBlock().getZ() != e.getTo().getBlock().getZ()) {
			Player p = e.getPlayer();
			if(checker.isProtected(e.getTo(), p)) {			
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 3));
			}
			else if(p.hasPotionEffect(PotionEffectType.SLOW_DIGGING) &&
					p.getPotionEffect(PotionEffectType.SLOW_DIGGING).getAmplifier() == 3){
				
				p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
			}			
		}
	}
	
	@EventHandler
	public void onPaperClick(PlayerInteractEvent e) {
		
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getClickedBlock().getType().equals(Material.BEACON)) {
				if(e.getPlayer().isSneaking()) {
					if(e.getHand() == EquipmentSlot.HAND) {
						if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.PAPER)) {
							String playername = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName();
							if(Bukkit.getPlayer(playername) != null) {
								TileState beacon = (TileState)e.getClickedBlock().getState();
								byte[] newKeyList = Serialize.DeserializeConcatAndSerialize(beacon, playername);
								beacon.getPersistentDataContainer().set(plugin.getBeaconWhitelist(), PersistentDataType.BYTE_ARRAY, newKeyList);
								beacon.update();
								e.getPlayer().sendMessage(ChatColor.GREEN + "Added " + playername + " to whitelist");
								e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 100F, 10F);
							}
							else {
								e.getPlayer().sendMessage(ChatColor.RED + "could not find player with the name " + playername);
								e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.2F, -10F);
							}	
						}		
					}
				}
			}
		}		
	}
	
	@EventHandler
	public void onBookClick(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getPlayer().isSneaking()) {
				if(e.getHand() == EquipmentSlot.HAND) {
					if(e.getClickedBlock().getType().equals(Material.BEACON)) {
						if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BOOK)) {
							
							TileState b = (TileState) e.getClickedBlock().getState();
							e.getPlayer().sendMessage(
								Arrays.toString(
									Serialize.getWhitelist(
										b.getPersistentDataContainer().get(
											plugin.getBeaconWhitelist(), PersistentDataType.BYTE_ARRAY)).toArray()));
							e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 3F, -100F);
						}
					}	
				}
			}
		}		
	}
	
	@EventHandler
	public void onFlintClick(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getPlayer().isSneaking()) {
				if(e.getHand() == EquipmentSlot.HAND) {
					if(e.getClickedBlock().getType().equals(Material.BEACON)) {
						if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.FLINT)) {
							
							String name = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName();
							TileState b = (TileState) e.getClickedBlock().getState();
							byte[] key = Serialize.DeserializeRemoveAndSerialize(b, name);
							b.getPersistentDataContainer().set(plugin.getBeaconWhitelist(), PersistentDataType.BYTE_ARRAY, key);
							b.update();
							e.getPlayer().sendMessage(ChatColor.GRAY + "removed " + name + " from the whitelist");
							e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_USE, 0.5F, 4F);
						}
					}	
				}
			}
		}		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
