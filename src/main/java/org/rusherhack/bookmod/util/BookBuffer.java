package org.rusherhack.bookmod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.rusherhack.bookmod.BookMod;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashSet;

/**
 * Used to store book information and write it to a file once finished
 *
 * @author John200410 5/15/2021 for bookmod
 */
public class BookBuffer {
	
	/**
	 * Set of book information in this buffer
	 */
	private final HashSet<BookInfo> books = new HashSet<>();
	
	/**
	 * Set of buffered images which will be written once this buffer has stopped
	 * <p>
	 * Key      = Name of book
	 * Value    = Screenshot
	 */
	private final HashSet<Tuple<String, BufferedImage>> screenshots = new HashSet<>();
	
	/**
	 * State of this buffer
	 */
	private boolean active = false;
	
	/**
	 * Folder to save book info and screenshots to
	 */
	private File dataFolder;
	
	/**
	 * Last item that the cursor has hovered over.
	 * <p>
	 * We have this because we don't want to try adding the same book over and over every frame.
	 * There is probably a better way to do this but i cba
	 */
	private ItemStack lastHoveredStack = null;
	
	/**
	 * Start tooltip listeners
	 *
	 * @return false if this buffer is already active
	 */
	public boolean start() {
		if(!this.active) {
			MinecraftForge.EVENT_BUS.register(this);
			this.dataFolder = new File(BookMod.PATH, BookMod.DATE_FORMAT.format(new Date()));
			this.dataFolder.mkdirs();
			return this.active = true;
		} else {
			return false;
		}
	}
	
	/**
	 * Will write all of the books in this buffer to a file and clear the buffer.
	 *
	 * @return false if this buffer isn't active
	 */
	public boolean finish() throws IOException {
		
		//check if this buffer is active
		if(this.active) {
			
			/**************************************************************
			 * Save book text file
			 *************************************************************/
			
			//file that the data in this buffer will be saved to
			final File storingFile = new File(this.dataFolder, "books.txt");
			
			//write data
			final FileWriter fileWriter = new FileWriter(storingFile);
			for (BookInfo book : this.books) {
				fileWriter.write("s!addbook " + book.toString() + System.lineSeparator());
			}
			fileWriter.close();
			
			//clear the buffer
			this.books.clear();
			
			/*************************************************************/
			
			/**************************************************************
			 * Save screenshots
			 *************************************************************/
			
			final File screenshotsFolder = new File(this.dataFolder, "screenshots");
			screenshotsFolder.mkdirs();
			
			for (Tuple<String, BufferedImage> screenshot : this.screenshots) {
				final File file = new File(screenshotsFolder, screenshot.getFirst() + ".png").getCanonicalFile();
				ImageIO.write(screenshot.getSecond(), "png", file);
			}
			
			//clear screenshots
			this.screenshots.clear();
			
			/*************************************************************/
			
			//reset state
			this.active = false;
			this.dataFolder = null;
			
			//unregister event listener
			this.lastHoveredStack = null;
			MinecraftForge.EVENT_BUS.unregister(this);
			
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Will try adding this book to the buffer and will take a screenshot
	 *
	 * @param title  title of book
	 * @param author author of book
	 */
	public void addBook(String title, String author) {
		if(this.active) {
			//add book, and if this book wasn't already there then take a screenshot
			final BookInfo bookInfo = new BookInfo(title, author);
			if(this.books.add(bookInfo)) {
				final Minecraft mc = Minecraft.getMinecraft();
				String name;
				
				//encode to make sure file can save properly if the file has special characters
				try {
					name = URLEncoder.encode(bookInfo.toString(), "UTF-8");
				} catch (UnsupportedEncodingException e) { //this shouldn't happen, BUT if it does then fallback to just author + epoch time
					e.printStackTrace();
					name = bookInfo.getAuthor() + System.currentTimeMillis();
				}
				
				//add screenshot
				if(BookMod.screenshot) {
					this.screenshots.add(new Tuple<>(name, ScreenShotHelper.createScreenshot(mc.displayWidth, mc.displayHeight, mc.getFramebuffer())));
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return (this.active ? "ACTIVE " : "IDLE ") + this.books + (this.dataFolder != null ? " " + this.dataFolder.getName() : "");
	}
	
	/**************************************************
	 * EVENTS
	 *************************************************/
	
	/**
	 * This is called every frame you are hovering over an item
	 */
	@SubscribeEvent
	public void onHover(RenderTooltipEvent.PostText event) {
		final ItemStack stack = event.getStack();
		
		//we aren't interested if this item isn't a written book
		if(!stack.getItem().equals(Items.WRITTEN_BOOK)) {
			return;
		}
		
		//check if this is the last book we have hovered over
		if(this.lastHoveredStack == null || !this.lastHoveredStack.equals(stack)) {
			
			//nbt of this itemstack
			final NBTTagCompound nbt = stack.getTagCompound();
			
			//verify that this is a real book
			if(ItemWrittenBook.validBookTagContents(nbt)) {
				//get book info from NBT
				final String title = nbt.getString("title");
				final String author = nbt.getString("author");
				
				//add book
				this.addBook(title, author);
			}
		}
		
		this.lastHoveredStack = stack;
	}
	
	/*************************************************/
	
}
