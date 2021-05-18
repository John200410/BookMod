package org.rusherhack.bookmod;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.rusherhack.bookmod.util.BookBuffer;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * This is a mod that was commissioned by Crystallising.
 * <p>
 * The goal of this mod is to make it easier to submit written books for archival.
 *
 * @author John200410
 */
@Mod(modid = BookMod.MOD_ID, name = BookMod.MOD_NAME, version = BookMod.VERSION)
public class BookMod {
	
	/**
	 * Constants
	 */
	public static final String MOD_ID = "autobookarchiver";
	public static final String MOD_NAME = "AutoBookArchiver";
	public static final String VERSION = "1.0-SNAPSHOT";
	public static final File PATH = new File(MOD_NAME);
	public static final String PREFIX = String.format("%s[%s]", TextFormatting.DARK_AQUA, MOD_NAME);
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH mm ss");
	public static final BookBuffer BOOK_BUFFER = new BookBuffer();
	
	/** This is the instance of your mod as created by Forge. It will never be null. */
	@Mod.Instance(MOD_ID)
	public static BookMod INSTANCE;
	
	/**
	 * This is the first initialization event.
	 */
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		//create path to store book files, if it doesn't exist.
		if(!PATH.isDirectory()) {
			PATH.mkdir();
		}
	}
	
	/**
	 * This is the second initialization event. Register custom recipes
	 */
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		//register our bookmod command
		ClientCommandHandler.instance.registerCommand(new ArchiverCommand());
	}
	
}
