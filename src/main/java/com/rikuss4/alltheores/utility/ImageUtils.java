package com.rikuss4.alltheores.utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

public class ImageUtils {
	public static final int MAX_COLOR = 256;

	public static final float LUMINANCE_RED = 0.2126f;
	public static final float LUMINANCE_GREEN = 0.7152f;
	public static final float LUMINANCE_BLUE = 0.0722f;

	static double hue = 180;
	static double saturation = 50;
	static double lightness = 0;

	static int[] lum_red_lookup;
	static int[] lum_green_lookup;
	static int[] lum_blue_lookup;

	static int[] final_red_lookup;
	static int[] final_green_lookup;
	static int[] final_blue_lookup;

	public ImageUtils() {
		doInit();
	}

	public void doHSB(double t_hue, double t_sat, double t_bri, BufferedImage image) {
		hue = t_hue;
		saturation = t_sat;
		lightness = t_bri;
		doInit();
		doColorize(image);
	}

	public static void doInit() {
		lum_red_lookup = new int[MAX_COLOR];
		lum_green_lookup = new int[MAX_COLOR];
		lum_blue_lookup = new int[MAX_COLOR];

		double temp_hue = hue / 360f;
		double temp_sat = saturation / 100f;

		final_red_lookup = new int[MAX_COLOR];
		final_green_lookup = new int[MAX_COLOR];
		final_blue_lookup = new int[MAX_COLOR];

		for (int i = 0; i < MAX_COLOR; ++i) {
			lum_red_lookup[i] = (int) (i * LUMINANCE_RED);
			lum_green_lookup[i] = (int) (i * LUMINANCE_GREEN);
			lum_blue_lookup[i] = (int) (i * LUMINANCE_BLUE);

			double temp_light = (double) i / 255f;

			Color color = new Color(Color.HSBtoRGB((float) temp_hue, (float) temp_sat, (float) temp_light));

			final_red_lookup[i] = (int) (color.getRed());
			final_green_lookup[i] = (int) (color.getGreen());
			final_blue_lookup[i] = (int) (color.getBlue());
		}
	}

	public static int getAlpha(int col) {
		return (col & 0xff000000) >> 24;
	}

	public static int getRed(int col) {
		return (col & 0x00ff0000) >> 16;
	}

	public static int getGreen(int col) {
		return (col & 0x0000ff00) >> 8;
	}

	public static int getBlue(int col) {
		return col & 0x000000ff;
	}

	public static int changeAlpha(int color, int alpha) {
		return (color & 0x00ffffff) | (alpha << 24);
	}

	public static int makeColWithoutAlpha(int red, int green, int blue, int adjustPercent) {
		return changeAlpha(makeCol(red, green, blue, 0xff, adjustPercent), 0);
	}

	public static int makeCol(int red, int green, int blue) {
		return makeColWithoutAlpha(red, green, blue, 0);
	}

	public static int makeCol(int red, int green, int blue, int alpha) {
		return makeCol(red, green, blue, alpha, 0);
	}

	public static int makeCol(int red, int green, int blue, int alpha, int adjustPercent) {
		int color = (alpha << 24) | (red << 16) | (green << 8) | blue;
		adjustPercent = (int) Math.round(2.55 * adjustPercent);
		red = ((color >> 16) & 0x00FF) + adjustPercent;
		green = ((color >> 8) & 0x0000FF) + adjustPercent;
		blue = (color & 0x000000FF) + adjustPercent;
		color = (Math.min(alpha, 255) << 24) | (Math.min(red, 255) << 16) | (Math.min(green, 255) << 8) | Math.min(blue, 255);
		return color;
	}

	/**
	 * Colors an image with specified color.
	 * 
	 * @param src
	 *           The image to color
	 * @param r
	 *           Red value. Between 0 and 1
	 * @param g
	 *           Green value. Between 0 and 1
	 * @param b
	 *           Blue value. Between 0 and 1
	 * @return The colored image
	 */
	public static BufferedImage colorize2(BufferedImage src, float r, float g, float b) {
		// Copy image ( who made that so complicated :< )
		BufferedImage newImage = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TRANSLUCENT);
		Graphics2D graphics = newImage.createGraphics();
		graphics.drawImage(src, 0, 0, null);
		graphics.dispose();

		// Color image
		for (int i = 0; i < newImage.getWidth(); i++) {
			for (int j = 0; j < newImage.getHeight(); j++) {
				int ax = newImage.getColorModel().getAlpha(newImage.getRaster().getDataElements(i, j, null));
				int rx = newImage.getColorModel().getRed(newImage.getRaster().getDataElements(i, j, null));
				int gx = newImage.getColorModel().getGreen(newImage.getRaster().getDataElements(i, j, null));
				int bx = newImage.getColorModel().getBlue(newImage.getRaster().getDataElements(i, j, null));
				rx *= r;
				gx *= g;
				bx *= b;
				newImage.setRGB(i, j, (ax << 24) | (rx << 16) | (gx << 8) | (bx << 0));
			}
		}
		return newImage;
	}

	// converts texture name to resource location
	public static ResourceLocation getResource(String s2) {
		return getResource(s2, 0);
	}

	public static ResourceLocation getResource(String s2, int type) {
		String s1 = "minecraft";

		int ind = s2.indexOf(58);

		if (ind >= 0) {
			if (ind > 1) {
				s1 = s2.substring(0, ind);
			}

			s2 = s2.substring(ind + 1, s2.length());
		}

		s1 = s1.toLowerCase();
		if (s2.startsWith("textures/items/") || s2.startsWith("textures/blocks/")) {
			s2 = s2 + ".png";
		} else {
			s2 = "textures/" + ((type == 1) ? "items/" : "blocks/") + s2 + ".png";
		}

		return new ResourceLocation(s1, s2);
	}

	public static int getColor(Boolean avg, ItemStack item) {
		UniqueIdentifier UID = GameRegistry.findUniqueIdentifierFor(item.getItem());
		return getColorMain(avg, item, UID.modId + ":" + UID.name);
	}

	public static int getColor(Boolean avg, ItemStack item, String... location) {
		int avgCol = getColorMain(avg, item, location[0]);
		if (avgCol < 0) {
			for (int i = 1; i < location.length; i++) {
				if (location[i] != null) {
					avgCol = getColorMain(avg, item, location[i]);
				}
				if (avgCol >= 0)
					return avgCol;
			}
		} else {
			return avgCol;
		}
		return 0;
	}

	public static int getColorMain(Boolean avg, ItemStack item, String location) {
		if (location == null)
			return -1;
		InputStream is;
		BufferedImage image;
		ResourceLocation textureLocation;
		if (Block.getBlockFromItem(item.getItem()).equals(Blocks.air)) {
			textureLocation = getResource(location, 1);
		} else {
			textureLocation = getResource(location);
		}
		int avgCol = getColor(avg, textureLocation);
		UniqueIdentifier UID = GameRegistry.findUniqueIdentifierFor(item.getItem());
		return avgCol;
	}

	public static int getColor(Boolean avg, BufferedImage image, ResourceLocation textureLocation) {
		if (image == null || image.getWidth() <= 0 || image.getHeight() <= 0) {
			return -1;
		}
		try {
			new File("TestImages").mkdir();
			ImageIO.write(image, "png", new File("TestImages\\" + textureLocation.getResourcePath().replaceAll("[^A-Za-z0-9()\\[\\]]", "_") + ".png"));
		} catch (IOException e) {
			LogHelper.all("Could not find: " + e.getMessage());
		}
		image = changeBrightness(image, -15);
		int wi = 1;

		int col = avg ? getAverageColor(image, 0, 0) : getBrightessColor(image, 0, 0);
		return col < 0 ? -1 : col;
	}

	public static int getColor(Boolean avg, String... location) {
		if (location.length < 1 || location[0] == null)
			return -1;
		LogHelper.mod_debug("location[0] = " + location[0]);
		int avgCol = getColor(avg, getResource(location[0]));
		if (avgCol > 0) {
			return avgCol;
		} else {
			if (location.length > 1) {
				for (int i = 1; i < location.length; i++) {
					avgCol = getColor(avg, getResource(location[i]));
					LogHelper.mod_debug("location[" + i + "] = " + location[i]);
					if (avgCol > 0)
						return avgCol;
				}
			}
		}
		return -1;
	}

	public static int getColor(Boolean avg, ResourceLocation textureLocation) {
		BufferedImage bufferedimage = null;
		if (textureLocation != null) {
			try {
				bufferedimage = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(textureLocation).getInputStream());
			} catch (IOException e) {
				// LogHelper.mod_debug(e.getMessage());
				return -1;
			}
		}
		return (bufferedimage != null) ? getColor(avg, bufferedimage, textureLocation) : -1;
	}

	public static BufferedImage colorize(BufferedImage input_image, int color) {
		int w = input_image.getWidth();
		int h = input_image.getHeight();

		BufferedImage output_image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		int[] image_data = new int[w * h];

		input_image.getRGB(0, 0, w, w, image_data, 0, w);

		for (int y = 0; y < h; y += w) {
			int[] new_data = image_data.clone();

			for (int i = 0; i < new_data.length; i++) {
				int col = new_data[i];
				if (getAlpha(col) != 0x00) {
					int mult = getBlue(col);
					new_data[i] = makeCol((getRed(color) * getRed(col)) / 0xff, (getGreen(color) * getGreen(col)) / 0xff, (getBlue(color) * getBlue(col)) / 0xff, getAlpha(col));
				}
			}

			// write the new image data to the output image buffer
			output_image.setRGB(0, y, w, w, new_data, 0, w);
			input_image.getRGB(0, y, w, w, image_data, 0, w);
		}
		return output_image;
	}

	public static BufferedImage overlayImages(BufferedImage bgImage, BufferedImage fgImage) {
		if(bgImage == null && fgImage == null)
			return new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
		if(fgImage == null)
			return bgImage;
		if(bgImage == null)
			return fgImage;
		int w = Math.max(bgImage.getWidth(), fgImage.getWidth());
		int h = Math.max(bgImage.getHeight(), fgImage.getHeight());
		BufferedImage output_image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = output_image.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(bgImage, 0, 0, w, h, null);
     	g.drawImage(fgImage, 0, 0, w, h, null);
		g.dispose();
		return output_image;
	}

	public static BufferedImage resizeImage(BufferedImage image, int w) {
		if(image == null || (image.getWidth() == w))
			return image;
		w = Math.max(image.getWidth(), w);
		int ratio = w / image.getWidth();
		if(ratio <= 1)
			return image;
		int h = image.getHeight() * ratio;
		BufferedImage output_image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = output_image.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(image, 0, 0, w, h, null);
		g.dispose();
		return output_image;
	}

	public static BufferedImage overlayImagesTiled(BufferedImage bgImage, BufferedImage fgImage) {
		int w = Math.max(bgImage.getWidth(), fgImage.getWidth());
		int h = Math.max(bgImage.getHeight(), fgImage.getHeight());
		BufferedImage output_image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = output_image.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (bgImage.getHeight() < fgImage.getHeight()) {
			for (int y = 0; y < h; y += w)
            g.drawImage(bgImage, 0, y, w, w, null);
      } else
			g.drawImage(bgImage, 0, 0, w, w, null);
     	g.drawImage(fgImage, 0, 0, w, w, null);
		g.dispose();
		return output_image;
	}

	public static BufferedImage tileImages(BufferedImage image, int h) {
		if (image == null || image.getHeight() == h)
			 return image;
		int w = image.getWidth();
		BufferedImage output_image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = output_image.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (int y = 0; y < h; y += w)
           g.drawImage(image, 0, y, w, w, null);
		g.dispose();
		return output_image;
	}

	public static BufferedImage doColorize(BufferedImage image) {
		if (image == null)
			return null;
		int height = image.getHeight();
		int width;

		while (height-- != 0) {
			width = image.getWidth();

			while (width-- != 0) {
				Color color = new Color(image.getRGB(width, height), true);

				int lum = lum_red_lookup[color.getRed()] + lum_green_lookup[color.getGreen()] + lum_blue_lookup[color.getBlue()];

				if (lightness > 0) {
					lum = (int) ((double) lum * (100f - lightness) / 100f);
					lum += 255f - (100f - lightness) * 255f / 100f;
				} else if (lightness < 0) {
					lum = (int) (((double) lum * (lightness + 100f)) / 100f);
				}
				Color final_color = new Color(final_red_lookup[lum], final_green_lookup[lum], final_blue_lookup[lum], color.getAlpha());
				image.setRGB(width, height, final_color.getRGB());
			}
		}
		return image;
	}

	public BufferedImage changeContrast(BufferedImage inImage, float increasingFactor) {
		int w = inImage.getWidth();
		int h = inImage.getHeight();

		BufferedImage outImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color color = new Color(inImage.getRGB(i, j), true);
				int r, g, b, a;
				float fr, fg, fb;

				r = color.getRed();
				fr = (r - 128) * increasingFactor + 128;
				r = (int) fr;
				r = keep256(r);

				g = color.getGreen();
				fg = (g - 128) * increasingFactor + 128;
				g = (int) fg;
				g = keep256(g);

				b = color.getBlue();
				fb = (b - 128) * increasingFactor + 128;
				b = (int) fb;
				b = keep256(b);

				a = color.getAlpha();

				outImage.setRGB(i, j, new Color(r, g, b, a).getRGB());
			}
		}
		return outImage;
	}

	public BufferedImage changeGreen(BufferedImage inImage, int increasingFactor) {
		int w = inImage.getWidth();
		int h = inImage.getHeight();

		BufferedImage outImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color color = new Color(inImage.getRGB(i, j), true);
				int r, g, b, a;
				r = color.getRed();
				g = keep256(color.getGreen() + increasingFactor);
				b = color.getBlue();
				a = color.getAlpha();

				outImage.setRGB(i, j, new Color(r, g, b, a).getRGB());
			}
		}
		return outImage;
	}

	public BufferedImage changeBlue(BufferedImage inImage, int increasingFactor) {
		int w = inImage.getWidth();
		int h = inImage.getHeight();

		BufferedImage outImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color color = new Color(inImage.getRGB(i, j), true);
				int r, g, b, a;
				r = color.getRed();
				g = color.getGreen();
				b = keep256(color.getBlue() + increasingFactor);
				a = color.getAlpha();

				outImage.setRGB(i, j, new Color(r, g, b, a).getRGB());
			}
		}
		return outImage;
	}

	public BufferedImage changeRed(BufferedImage inImage, int increasingFactor) {
		int w = inImage.getWidth();
		int h = inImage.getHeight();

		BufferedImage outImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color color = new Color(inImage.getRGB(i, j), true);
				int r, g, b, a;
				r = keep256(color.getRed() + increasingFactor);
				g = color.getGreen();
				b = color.getBlue();
				a = color.getAlpha();

				outImage.setRGB(i, j, new Color(r, g, b, a).getRGB());
			}
		}
		return outImage;
	}

	public static BufferedImage changeBrightness(BufferedImage inImage, int increasingFactor) {
		int w = inImage.getWidth();
		int h = inImage.getHeight();

		BufferedImage outImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color color = new Color(inImage.getRGB(i, j), true);

				int r, g, b, a;

				r = keep256(color.getRed() + increasingFactor);
				g = keep256(color.getGreen() + increasingFactor);
				b = keep256(color.getBlue() + increasingFactor);
				a = color.getAlpha();

				outImage.setRGB(i, j, new Color(r, g, b, a).getRGB());
			}
		}
		return outImage;
	}

	public static int keep256(int i) {
		if (i <= 255 && i >= 0)
			return i;
		if (i > 255)
			return 255;
		return 0;
	}

	public static boolean isIncluded(Color target, int pixelColor, int tolerance) {
		Color pixel = new Color(pixelColor);
		int rT = target.getRed();
		int gT = target.getGreen();
		int bT = target.getBlue();
		int rP = pixel.getRed();
		int gP = pixel.getGreen();
		int bP = pixel.getBlue();
		return ((rP - tolerance <= rT) && (rT <= rP + tolerance) && (gP - tolerance <= gT) && (gT <= gP + tolerance) && (bP - tolerance <= bT) && (bT <= bP + tolerance));
	}

	public static boolean isGray(int[] rgbArr) {
		int rgDiff = rgbArr[0] - rgbArr[1];
		int rbDiff = rgbArr[0] - rgbArr[2];
		// Filter out black, white and grays...... (tolerance within 10 pixels)
		int tolerance = 10;
		if (rgDiff > tolerance || rgDiff < -tolerance)
			if (rbDiff > tolerance || rbDiff < -tolerance) {
				return false;
			}
		return true;
	}

	public static int[] getRGBArr(int pixel) {
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		return new int[] { red, green, blue };

	}

	public static int getAverageColor(BufferedImage bi, int x0, int y0) {
		if (bi == null)
			return -1;
		int w = bi.getWidth();
		int h = bi.getHeight();
		int x1 = x0 + w;
		int y1 = y0 + h;
		// Color gray = new Color(8553090);
		Color gray = new Color(Integer.parseInt("8f8f8f", 16));
		Color white = new Color(Integer.parseInt("ffffff", 16));
		// Color reddish = new Color(Integer.parseInt("eaaf8b", 16));
		// Encode
		// System.out.println("hex = " +
		// Integer.toHexString(Integer.parseInt("8f8f8f", 16)));
		// Decode
		// System.out.println("int = " + Integer.parseInt("8f8f8f", 16));

		long sumr = 0, sumg = 0, sumb = 0;
		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				int rgb = bi.getRGB(x, y);
				Color pixel = new Color(rgb);
				if (!isIncluded(gray, rgb, 50) && !isIncluded(white, rgb, 50)) {
					// if(!isGray(getRGBArr(rgb))) {
					sumr += pixel.getRed();
					sumg += pixel.getGreen();
					sumb += pixel.getBlue();
					// }
				}
			}
		}
		// Goal = 78c38d
		int num = w * h;
		int red = (int) (sumr / num);
		int green = (int) (sumg / num);
		int blue = (int) (sumb / num);
		return makeCol(red, green, blue, 20);
	}

	public static int getBrightessColor(BufferedImage bi, int x0, int y0) {
		if (bi == null)
			return -1;
		int w = bi.getWidth();
		int h = bi.getHeight();
		int x1 = x0 + w;
		int y1 = y0 + h;
		Color gray = new Color(Integer.parseInt("8f8f8f", 16));
		Color white = new Color(Integer.parseInt("ffffff", 16));
		// Color reddish = new Color(Integer.parseInt("eaaf8b", 16));

		int brightessRed = 0, brightessGreen = 0, brightessBlue = 0;
		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				int rgb = bi.getRGB(x, y);
				Color pixel = new Color(rgb);
				if (!isIncluded(gray, rgb, 50) && !isIncluded(white, rgb, 50)) {
					if (pixel.getRed() > brightessRed || pixel.getGreen() > brightessGreen || pixel.getBlue() > brightessBlue) {
						brightessRed = pixel.getRed();
						brightessGreen = pixel.getGreen();
						brightessBlue = pixel.getBlue();
					}
				}
			}
		}
		// Goal = 78c38d
		return makeCol(brightessRed, brightessGreen, brightessBlue, 0);
	}

	public static BufferedImage createDenseTexture(BufferedImage image, int renderType) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage output_image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		int[] ore_data = new int[width * width];

		for (int y = 0; y < height; y += width) {
			// read the ARGB color data into our arrays
			image.getRGB(0, y, width, width, ore_data, 0, width);

			// generate our new texture
			int[] new_data = createDenseTexture(width, ore_data, renderType);

			// write the new image data to the output image buffer
			output_image.setRGB(0, y, width, width, new_data, 0, width);
		}
		return output_image;
	}

	private static int[] createDenseTexture(int w, int[] ore_data, int renderType) {
		int[] new_data = new int[w * w];

		// we need to work out which pixels should be considered 'ore pixels' and
		// which should be 'base pixels'
		boolean[] same = new boolean[w * w];
		for (int i = 0; i < ore_data.length; i += 1) {
			if (getAlpha(ore_data[i]) == 0) {   // if the ore texture pixel is
															// transparent, overwrite with the
															// corresponding stone pixel
				same[i] = true;
			}
			new_data[i] = ore_data[i];
		}

		int[] dx;
		int[] dy;

		// allows for different convolution filters
		switch (renderType) {
			default:
			case 0:
				dx = new int[] { -1, 2, 3 };
				dy = new int[] { -1, 0, 1 };
				break;
			case 1:
				dx = new int[] { -1, 1, 0, 0, -1, -1, 1, 1, -2, 2, 0, 0 };
				dy = new int[] { 0, 0, -1, 1, -1, 1, -1, 1, 0, 0, -2, 2 };
				break;
			case 2:
				dx = new int[] { -1, 0, 1 };
				dy = new int[] { -1, 0, 1 };
				break;
			case 3:
				dx = new int[] { -2, 2, 1, 1 };
				dy = new int[] { 1, 1, -2, 2 };
			case 4:
				dx = new int[] { -6, -3, 3, 6 };
				dy = new int[] { 0, 0, 0, 0 };
				break;
			case 5:
				dx = new int[] { -5, -5, 5, 5 };
				dy = new int[] { -5, 5, -5, 5 };
				break;
			case 6:
				dx = new int[] { 0, 1, 2, 3 };
				dy = new int[] { 0, -3, 2, -1 };
				break;
			case 7:
				dx = new int[] { -1, 1, 0, 0 };
				dy = new int[] { 0, 0, -1, 1 };
				break;
		}

		// where the magic happens
		for (int i = 0; i < ore_data.length; i += 1) {
			int x = (i % w);
			int y = (i - x) / w;

			// if the pixel an ore pixel, we don't need to do anything so continue
			if (!same[i])
				continue;

			// use our convolution filter to see if we can find an ore pixel nearby
			for (int j = 0; j < dx.length; j++) {
				final int new_x = x + dx[j];
				final int new_y = y + dy[j];

				if (new_x >= 0 && new_x < w && new_y >= 0 && new_y < w) // is valid
																							// pixel
																							// location
					if (!same[new_x + new_y * w]) { // is it an ore pixel?
						new_data[i] = ore_data[new_x + new_y * w];
						break;
					}
			}
		}
		return new_data;
	}
}
