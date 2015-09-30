package com.rikuss4.alltheores.items.Tools;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.rikuss4.alltheores.blocks.ATOOre;
import com.rikuss4.alltheores.reference.Reference;
import com.rikuss4.alltheores.textures.ItemTexture;
import com.rikuss4.alltheores.utility.LogHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Axe extends ItemAxe {
	private ATOOre ore;
	private String name;
	private int color;
	private int renderType;

	public Axe(String unlocalizedName, ToolMaterial material, ATOOre ore, int color, int renderType) {
		super(material);
		this.setName(unlocalizedName + "_axe");
		this.setOre(ore);
		this.setColor(color);
		this.setRenderType(renderType);
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(Reference.MOD_ID.toLowerCase() + ":" + unlocalizedName);
		this.setCreativeTab(CreativeTabs.tabAllSearch);
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemStack) {
		String name = new ItemStack(getOre()).getDisplayName();
		name = name.substring(0, (name.lastIndexOf(StatCollector.translateToLocal("AllTheOres.oreSubstite").trim().replaceFirst("ORENAME", ""))));
		return (StatCollector.translateToLocal("AllTheOres.axeSubstite")).replaceFirst("ORENAME", name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public int getRenderType() {
		return renderType;
	}

	public void setRenderType(int renderType) {
		this.renderType = renderType;
	}

	public ATOOre getOre() {
		return ore;
	}

	public void setOre(ATOOre ore) {
		this.ore = ore;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		if (iconRegister instanceof TextureMap) {
			TextureMap map = (TextureMap) iconRegister;

			String name = Reference.MOD_ID.toLowerCase() + ":" + getName();

			// load texture from file or generate from baseBlock
			TextureAtlasSprite texture = map.getTextureExtry(name);
			if (texture == null) {
				texture = new ItemTexture(this.getOre(), name, getColor(), getRenderType(), "axe");
				if (!map.setTextureEntry(name, texture)) {
					LogHelper.error(getName() + ": Could not add texture after creation");
				}
			}

			itemIcon = map.getTextureExtry(name);
		}
	}
}
