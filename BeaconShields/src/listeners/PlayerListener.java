package listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Beacon;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import main.Main;
import util.IsBeaconProtected;
import util.Serialize;

public class PlayerListener implements Listener {

	private Main plugin = Main.getPlugin();
	private IsBeaconProtected checker = new IsBeaconProtected();	
	
	/*
	 * feature or bug?
	 * 
	 * the beacon only has to be tier 4 to give mining fatigue, it doesn't have to be "activated" and have the beacon beam
	 */
	
	@EventHandler
	public void enterBeaconRadius(PlayerMoveEvent e) {
		if(e.getFrom().getBlock().getX() != e.getTo().getBlock().getX() ||
			e.getFrom().getBlock().getZ() != e.getTo().getBlock().getZ()) {
			Player p = e.getPlayer();
			if(checker.isProtected(e.getTo(), p)) {			
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 2));
			}
			else if(p.hasPotionEffect(PotionEffectType.SLOW_DIGGING) &&
					p.getPotionEffect(PotionEffectType.SLOW_DIGGING).getAmplifier() == 2){
				
				p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
			}			
		}
	}
	
	@EventHandler
	public void whitelistManager(PlayerInteractEvent e) {
		
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getClickedBlock().getType().equals(Material.BEACON)) {
				if(e.getPlayer().isSneaking()) {
					if(e.getHand() == EquipmentSlot.HAND) {
							
						ItemStack heldItem = e.getPlayer().getInventory().getItemInMainHand();
						
						if(heldItem.getType().equals(Material.PAPER) || heldItem.getType().equals(Material.FLINT)) {
							if(Bukkit.getPlayer(heldItem.getItemMeta().getDisplayName()) != null) {
								
								Beacon b = (Beacon) e.getClickedBlock().getState();
								
								if(heldItem.getType().equals(Material.PAPER)) {
									byte[] newKeyList = Serialize.DeserializeConcatAndSerialize(b, heldItem.getItemMeta().getDisplayName());
									b.getPersistentDataContainer().set(plugin.getBeaconWhitelist(), PersistentDataType.BYTE_ARRAY, newKeyList);
									b.update();
									e.getPlayer().sendMessage(ChatColor.GREEN + "Added " + heldItem.getItemMeta().getDisplayName() + " to whitelist");
									e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_USE, 0.5F, 10F);
								}
								else {
									byte[] key = Serialize.DeserializeRemoveAndSerialize(b, heldItem.getItemMeta().getDisplayName());
									b.getPersistentDataContainer().set(plugin.getBeaconWhitelist(), PersistentDataType.BYTE_ARRAY, key);
									b.update();
									e.getPlayer().sendMessage(ChatColor.GRAY + "removed " + heldItem.getItemMeta().getDisplayName() + " from the whitelist");
									e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_PLACE, 1F, -5F);
								}		
							}	
							else {
								e.getPlayer().sendMessage(ChatColor.RED + "Could not find any players with the name " + heldItem.getItemMeta().getDisplayName());
								e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_PLACE, 1F, -5F);
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
}