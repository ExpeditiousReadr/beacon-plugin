package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataType;

import main.Main;

/*
 *  Utility class for serializing and deserializing objects for storage in PersistentDataContainers
 */

public class Serialize {
	
	private static Main plugin = Main.getPlugin();
	
	private static byte[] serializeNames(List<String> names) {		
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(names);		
		}
		catch(Exception ex) {
			System.out.println(ex);
		}		
		return bos.toByteArray();		
	}
	
	@SuppressWarnings("unchecked")
	private static List<String> deserializeNames(byte[] serialized) {
		
		/*
		 * somewhat inelegant, can return null. not sure how to make cleaner
		 */
		
		
		List<String> deserialized = new ArrayList<String>();
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
			ObjectInputStream ois = new ObjectInputStream(bis);
			deserialized = (List<String>) ois.readObject();
			return deserialized;		
		}
		catch(EOFException e) {
			return deserialized;
		}
		catch(Exception ex)	{
			System.out.println(ex);
		}	
		
		return null;
	}
	
	public static byte[] DeserializeConcatAndSerialize(TileState b,  String name) {
		
		List<String> keylist = deserializeNames(b.getPersistentDataContainer().get(plugin.getBeaconWhitelist(), PersistentDataType.BYTE_ARRAY));
		
		//silent check for duplicates, probably should be somewhere else, but nowhere else has access to the deserialized list
		if(!(keylist.contains(name))) {
			keylist.add(name);
			return serializeNames(keylist);
		}		
		return b.getPersistentDataContainer().get(plugin.getBeaconWhitelist(), PersistentDataType.BYTE_ARRAY);			
	}
	
	public static byte[] DeserializeRemoveAndSerialize(TileState b, String name) {
		
		List<String> keylist = deserializeNames(b.getPersistentDataContainer().get(plugin.getBeaconWhitelist(), PersistentDataType.BYTE_ARRAY));
		
		if(keylist.contains(name)) {
			keylist.remove(name);
			return serializeNames(keylist);			
		}	
		return b.getPersistentDataContainer().get(plugin.getBeaconWhitelist(), PersistentDataType.BYTE_ARRAY);
	}
	
	
	public static List<String> getWhitelist(byte[] key) {
		
		return deserializeNames(key);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
