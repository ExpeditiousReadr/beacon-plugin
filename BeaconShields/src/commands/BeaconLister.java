package commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.Main;

/*
 * A console only command that lists all beacons that the plugin has recognized, along with their coordinates
 */

public class BeaconLister implements CommandExecutor {

	Main p = Main.getPlugin();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if(lbl.equalsIgnoreCase("blist")) {
			if(!(sender instanceof Player)) {
				int c = 0;
				for(Location l : p.getBeacons()) {
					sender.sendMessage(ChatColor.BLUE + "[BeaconShields] " + ChatColor.WHITE + l.getX() + ", " + l.getY() + ", " + l.getZ());
					c++;
				}
				sender.sendMessage(ChatColor.BLUE + "[BeaconShields] " + ChatColor.WHITE + c + " beacons found");
				return true;
			}
			Player p = (Player) sender;
			p.sendMessage(ChatColor.RED + "must be a console to run this command!");
		}
		return false;
	}

}
