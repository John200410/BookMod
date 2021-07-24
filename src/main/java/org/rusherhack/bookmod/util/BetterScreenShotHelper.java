package org.rusherhack.bookmod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

/**
 * Copied stuff from ScreenShotHelper.class and modified it lul
 *
 * @author John200410 7/24/2021 for bookmod
 */
public class BetterScreenShotHelper {
	
	private static IntBuffer pixelBuffer;
	private static int[] pixelValues;
	
	public static BufferedImage createScreenshot(int x, int y, int width, int height) {
		int i = width * height;
		
		if(pixelBuffer == null || pixelBuffer.capacity() < i) {
			pixelBuffer = BufferUtils.createIntBuffer(i);
			pixelValues = new int[i];
		}
		
		GlStateManager.glPixelStorei(3333, 1);
		GlStateManager.glPixelStorei(3317, 1);
		pixelBuffer.clear();
		
		GlStateManager.glReadPixels(x, Minecraft.getMinecraft().displayHeight - y - height, width, height, 32993, 33639, pixelBuffer);
		
		pixelBuffer.get(pixelValues);
		TextureUtil.processPixelValues(pixelValues, width, height);
		BufferedImage bufferedimage = new BufferedImage(width, height, 1);
		bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);
		return bufferedimage;
	}
	
}
