package com.rikuss4.alltheores.textures;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

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

public class BlockTexture extends TextureAtlasSprite {
	private ATOOre ore;
	private int renderType;
	private int color;
	private int type;
	private String name;
	private String blockName;
	private Boolean isOverlay;
	public BufferedImage output_image;

	public BlockTexture(String name, String blockName, int type, int color, int renderType, ATOOre ore, Boolean isOverlay) {
		super(name);
		this.name = name;
		this.blockName = blockName;
		this.renderType = ((ore != null && blockName.equals("denseore")) ? ore.oreDenseRenderType : ((ore != null && blockName.equals("poorore")) ? ore.orePoorRenderType : renderType));
		this.type = type;
		this.color = color;
		this.ore = ore;
		this.isOverlay = isOverlay;
	}

	public BlockTexture(String name, String blockName, int type, int color, int renderType) {
		this(name, blockName, type, color, renderType, null, false);
	}

	public String getTypeName() {
		return (this.type == 1 ? "items" : "blocks");
	}

	@Override
	public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
		ResourceLocation location1 = new ResourceLocation(location.getResourceDomain(), String.format("%s/%s%s", "textures/" + (this.type == 1 ? "items" : "blocks"), location.getResourcePath(), ".png"));
		try {
			// check to see if the resource can be loaded (someone added an
			// override)
			manager.getResource(location1);
			LogHelper.info("Detected override for " + location.getResourcePath() + ".");
			return false;
		} catch (IOException e) {
			return true;
		}
	}

	@Override
	public boolean load(IResourceManager manager, ResourceLocation location) {
		int mp = Minecraft.getMinecraft().gameSettings.mipmapLevels + 1;

		BufferedImage[] block_image = new BufferedImage[mp];

		int wi = 1;

		AnimationMetadataSection animation = null;

		// background block texture for ores
		BufferedImage background_image;
		int w = 16;
		int h = 16;
		if (!isOverlay) {
			try {
				IResource iResourceUnderlay = manager.getResource(ImageUtils.getResource(ore.underlyingBlockName));
				background_image = ImageIO.read(iResourceUnderlay.getInputStream());

				w = background_image.getWidth();
				h = background_image.getHeight();
			} catch (IOException e) {
				background_image = null;
			}
		} else {
			background_image = null;
		}

		// read grayscale texture to color
		BufferedImage base_image;
		try {
			IResource iResourceBlock = manager.getResource(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/" + getTypeName() + "/" + this.blockName + "_" + renderType + ".png"));
			base_image = ImageIO.read(iResourceBlock.getInputStream());
		} catch (IOException e) {
			base_image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		}

		// read underlay texture
		BufferedImage underlay_image;
		try {
			IResource iResourceUnderlay = manager.getResource(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/" + getTypeName() + "/" + this.blockName + renderType + ".png"));
			underlay_image = ImageIO.read(iResourceUnderlay.getInputStream());
		} catch (IOException e) {
			underlay_image = null;
		}

		// read overlay texture
		BufferedImage overlay_image;
		try {
			IResource iResourceOverlay = manager.getResource(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/" + getTypeName() + "/" + this.blockName + "_overlay_" + renderType + ".png"));
			overlay_image = ImageIO.read(iResourceOverlay.getInputStream());
		} catch (IOException e) {
			overlay_image = null;
		}

		BufferedImage output_image = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		output_image = ImageUtils.colorize(base_image, this.color);
		if (blockName.equals("denseore")) {
			output_image = ImageUtils.createDenseTexture(output_image, renderType);
		}
		if (background_image != null) {
			if (underlay_image != null) {
				output_image = ImageUtils.overlayImages(background_image, underlay_image);
			} else {
				output_image = ImageUtils.overlayImages(background_image, output_image);
			}
		}
		if (underlay_image != null) {
			output_image = ImageUtils.overlayImages(underlay_image, output_image);
		}
		if (overlay_image != null) {
			output_image = ImageUtils.overlayImages(output_image, overlay_image);
		}

		// replace the old texture
		block_image[0] = output_image;

		// write image to filesystem
		try {
			new File("SampleImages").mkdir();
			ImageIO.write(output_image, "png", new File("SampleImages\\" + location.getResourcePath().replaceAll("[^A-Za-z0-9()\\[\\]]", "_") + "_" + (renderType) + ".png"));
		} catch (IOException e) {
			LogHelper.all("Could not find: " + e.getMessage());
		}

		// load the texture
		this.loadSprite(block_image, animation, (float) Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1.0F);
		// LogHelper.all("Successfully generated texture for \"" +
		// location.getResourcePath() + "\". Place \"" +
		// location.getResourcePath() +
		// ".png\" in the assets folder to override.");
		return false;
	}
}
