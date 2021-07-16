package org.rusherhack.bookmod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Our command to start and stop storing book info
 *
 * @author John200410 5/15/2021 for bookmod
 */
public class ArchiverCommand extends CommandBase {
	
	@Override
	public String getName() {
		return "archiver";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return this.getName() + " <start/stop>";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		//if there are no arguments, just print whatever is in the book buffer
		if(args.length == 0) {
			sender.sendMessage(new TextComponentString(BookMod.PREFIX + " " + BookMod.BOOK_BUFFER));
		} else {
			switch (args[0].toLowerCase()) {
				case "start":
					sender.sendMessage(new TextComponentString(BookMod.PREFIX + " " + (BookMod.BOOK_BUFFER.start() ? "Started listening" : "Buffer already active! Run '/archiver stop' first")));
					break;
				case "stop":
					try {
						sender.sendMessage(new TextComponentString(BookMod.PREFIX + " " + (BookMod.BOOK_BUFFER.finish() ? "Saved books" : "Buffer not active!")));
					} catch (Throwable t) { //some i/o exception
						t.printStackTrace();
						sender.sendMessage(new TextComponentString(BookMod.PREFIX + " " + "Failed to save books! Check log for more details"));
					}
					break;
				default:
					sender.sendMessage(new TextComponentString(BookMod.PREFIX + " " + "Invalid Arguments!"));
			}
		}
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}
}
