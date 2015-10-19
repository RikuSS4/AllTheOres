package com.rikuss4.alltheores.textures;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.ResourceLocation;

import com.rikuss4.alltheores.blocks.ATOOre;
import com.rikuss4.alltheores.reference.Reference;
import com.rikuss4.alltheores.utility.ImageUtils;
import com.rikuss4.alltheores.utility.LogHelper;

public class ItemTexture extends TextureAtlasSprite {
	private ATOOre ore;
	private String item;
	private String typeName;
	private int color;
	private int renderType;
	private int type;

	public ItemTexture(ATOOre ore, String name, int color, int renderType, String item) {
		this(ore, name, color, renderType, item, 0);
	}

	public ItemTexture(ATOOre ore, String name, int color, int renderType, String item, int type) {
		super(name);
		this.type = type;
		this.typeName = (type == 1 ? "models/armor" : "items");
		this.item = item;
		this.color = color;
		this.renderType = renderType;
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

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
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
		ResourceLocation location1 = new ResourceLocation(location.getResourceDomain(), String.format("%s/%s%s", "textures/items", location.getResourcePath(), ".png"));
		try {
			// check to see if the resource can be loaded (someone added an
			// override)
			manager.getResource(location1);
			LogHelper.info("Detected override for " + getItem() + ".");
			return false;
		} catch (IOException e) {
			return true;
		}
	}

	@Override
	public boolean load(IResourceManager manager, ResourceLocation location) {
		int mp = Minecraft.getMinecraft().gameSettings.mipmapLevels + 1;

		BufferedImage[] item_image = new BufferedImage[mp];

		int wi = 1;

		AnimationMetadataSection animation = null;

		// read grayscale texture
		BufferedImage base_image;
		try {
			IResource iResourceItem = manager.getResource(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/" + this.typeName + "/" + getItem() + "_" + getRenderType() + ".png"));
			base_image = ImageIO.read(iResourceItem.getInputStream());
		} catch (IOException e) {
			base_image = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);
			// LogHelper.debug(e.getMessage());
			return true;
		}
		int w = base_image.getWidth();
		int h = base_image.getHeight();

		// read underlay texture
		BufferedImage underlay_image;
		try {
			IResource iResourceUnderlay = manager.getResource(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/" + this.typeName + "/" + getItem() + "_underlay_" + getRenderType() + ".png"));
			underlay_image = ImageIO.read(iResourceUnderlay.getInputStream());
		} catch (IOException e) {
			underlay_image = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
			// LogHelper.debug(e.getMessage());
		}

		BufferedImage output_image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		output_image = ImageUtils.colorize(base_image, getColor());
		output_image = ImageUtils.overlayImages(underlay_image, output_image);

		// replace the old texture
		item_image[0] = output_image;

		if (Reference.DEBUG) {
			try {
				new File("SampleImages").mkdir();
				ImageIO.write(output_image, "png", new File("SampleImages/" + getName() + "_" + getRenderType() + ".png"));
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}

		// load the texture
		// LogHelper.all("Successfully generated texture for \"" + ore.name +
		// "\". Place \"" + getName() +
		// ".png\" in the assets folder to override.");
		if (this.type != 1) {
			this.loadSprite(item_image, animation, (float) Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1.0F);
			return false;
		} else {
			return true;
		}
	}
}
