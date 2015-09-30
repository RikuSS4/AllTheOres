package com.rikuss4.alltheores.items.Resources;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import com.rikuss4.alltheores.blocks.ATOOre;
import com.rikuss4.alltheores.reference.Reference;
import com.rikuss4.alltheores.textures.ItemTexture;
import com.rikuss4.alltheores.utility.LogHelper;
import com.rikuss4.alltheores.utility.Utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Nugget extends Item {
	private String name;
	private String baseName;
	private ATOOre ore;
	private String oreDictName;
	private int color;
	private int burnTime;
	private int renderType;

	public Nugget(String name, ATOOre ore, int color, String type, int renderType, int burnTime) {
		super();
		setBaseName(name.toLowerCase());
		setName(name.toLowerCase() + (type.equals("") ? "" : "_") + type);
		setUnlocalizedName(name.toLowerCase() + (type.equals("") ? "" : "_") + type);
		setBurnTime(burnTime);
		setOre(ore);
		setColor(color);
		setOreDictName(type, name);
		setRenderType(renderType);
		setCreativeTab(CreativeTabs.tabAllSearch);
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemStack) {
		String name = new ItemStack(getOre()).getDisplayName();
		name = name.substring(0, (name.lastIndexOf(StatCollector.translateToLocal("AllTheOres.oreSubstite").trim().replaceFirst("ORENAME", ""))));
		return (StatCollector.translateToLocal("AllTheOres.nuggetSubstite")).replaceFirst("ORENAME", name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getColor() {
		if (color < 0) {
			return getOre().color;
		} else {
			return color;
		}
	}

	public ATOOre getOre() {
		return ore;
	}

	public void setOre(ATOOre ore) {
		this.ore = ore;
	}

	public String getOreDictName() {
		return oreDictName;
	}

	public void setOreDictName(String prefix, String oreDictName) {
		this.oreDictName = (prefix.equals("") ? oreDictName.toLowerCase().charAt(0) + Utils.capitalize(oreDictName.substring(1)) : prefix + Utils.capitalize(oreDictName)).replaceAll(" ", "");
	}

	public int getRenderType() {
		return renderType;
	}

	public void setRenderType(int renderType) {
		this.renderType = renderType;
	}

	public int getBurnTime() {
		return burnTime;
	}

	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}

	public void registerOreDict() {
		if (getOreDictName() != null) {
			OreDictionary.registerOre(getOreDictName(), this);
		}
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
				texture = new ItemTexture(this.getOre(), name, getColor(), getRenderType(), "nugget");
				if (!map.setTextureEntry(name, texture)) {
					LogHelper.error(getName() + ": Could not add texture after creation");
				}
			}

			itemIcon = map.getTextureExtry(name);
		}
	}
}
