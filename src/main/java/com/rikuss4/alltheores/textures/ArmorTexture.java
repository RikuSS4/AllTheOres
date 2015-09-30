package com.rikuss4.alltheores.textures;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import com.rikuss4.alltheores.blocks.ATOOre;
import com.rikuss4.alltheores.reference.Reference;
import com.rikuss4.alltheores.utility.ImageUtils;
import com.rikuss4.alltheores.utility.LogHelper;

public class ArmorTexture extends TextureAtlasSprite {
	private ATOOre ore;
	private String name;
	private int color;
	private int renderType;
	private int item;

	public ArmorTexture(ATOOre ore, String name, int color, int renderType, int item) {
		super(name);
		this.item = item;
		this.color = color;
		this.renderType = renderType;
		this.name = name;
		setOre(ore);
	}

	public ATOOre getOre() {
		return ore;
	}

	public void setOre(ATOOre ore) {
		this.ore = ore;
	}

	public String getName() {
		return this.getIconName();
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getRenderType() {
		return renderType;
	}

	public void setRenderType(int renderType) {
		this.renderType = renderType;
	}

	@Override
	public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
		ResourceLocation location1 = new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + ".png");
		try {
			// check to see if the resource can be loaded (someone added an
			// override)
			manager.getResource(location1);
			LogHelper.info("Detected override for " + location1.getResourceDomain() + location1.getResourcePath() + ".png.");
			return false;
		} catch (IOException e) {
			return true;
		}
	}

	@Override
	public boolean load(IResourceManager manager, ResourceLocation location) {
		int mp = Minecraft.getMinecraft().gameSettings.mipmapLevels + 1;

		// read grayscale texture
		BufferedImage base_image;
		try {
			IResource iResourceItem = manager.getResource(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/models/armor/armor_" + name + "_" + getRenderType() + ".png"));
			base_image = ImageIO.read(iResourceItem.getInputStream());
		} catch (IOException e) {
			base_image = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
		}
		int w = base_image.getWidth();
		int h = base_image.getHeight();

		// read underlay texture
		BufferedImage underlay_image;
		try {
			IResource iResourceUnderlay = manager.getResource(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/models/armor/armor_" + name + "_underlay_" + getRenderType() + ".png"));
			underlay_image = ImageIO.read(iResourceUnderlay.getInputStream());
		} catch (IOException e) {
			underlay_image = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
		}

		BufferedImage output_image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		output_image = ImageUtils.colorize(base_image, getColor());
		if (underlay_image.getWidth() > 1) {
			output_image = ImageUtils.overlayImages(underlay_image, output_image);
		}

		/* write image to filesystem
		try {
			new File("SampleImages").mkdir();
			ImageIO.write(output_image, "png", new File("SampleImages\\" + getName().replaceAll("[^A-Za-z0-9()\\[\\]]", "_") + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		// load the texture
		//LogHelper.all("Successfully generated texture for \"" + ore.name + "\" armor. Place \"" + getName() + ".png\" in the assets folder to override.");
		//Minecraft mc = Minecraft.getMinecraft();
		// mc.renderEngine.allocateAndSetupTexture(output_image);
		// this.loadSprite(item_image, null, (float)
		// Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1.0F);
		return true;
	}

}
