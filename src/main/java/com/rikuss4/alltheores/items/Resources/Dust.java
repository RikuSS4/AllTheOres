package com.rikuss4.alltheores.items.Resources;

import java.util.ArrayList;
import java.util.List;

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

public class Dust extends Item {
	private String name;
	private String baseName;
	private String dustType;
	private ATOOre ore;
	private List<String> oreDictNames = new ArrayList<String> ();
	private int color;
	private int burnTime;
	private int renderType;

	public Dust(String name, ATOOre ore, int color, String type, List<String> oreDictList, int renderType, int burnTime) {
		super();
		String ltype = type.toLowerCase();
		setBaseName(name.toLowerCase());
		setName(name.toLowerCase() + (ltype.equals("") ? "" : "_") + ltype);
		setType(type);
		setUnlocalizedName(name.toLowerCase() + (ltype.equals("") ? "" : "_") + ltype);
		setOre(ore);
		setColor(color);
		setBurnTime(burnTime);
		setOreDictName(oreDictList, name);
		setRenderType(renderType);
		setCreativeTab(CreativeTabs.tabAllSearch);
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemStack) {
		String name = new ItemStack(getOre()).getDisplayName();
		name = name.substring(0, (name.lastIndexOf(StatCollector.translateToLocal("AllTheOres.oreSubstite").trim().replaceFirst("ORENAME", ""))));
		return (StatCollector.translateToLocal("AllTheOres.dustSubstite")).replaceFirst("ORENAME", name);
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

	public String getType() {
		return dustType;
	}

	public void setType(String type) {
		this.dustType = type;
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

	public List<String> getOreDictName() {
		return oreDictNames;
	}

	public void setOreDictName(String prefix, String oreDictName) {
		this.oreDictNames.add((prefix.equals("") ? oreDictName.toLowerCase().charAt(0) + Utils.capitalize(oreDictName.substring(1)) : prefix + Utils.capitalize(oreDictName)).replaceAll(" ", ""));
	}

	public void setOreDictName(List<String> prefixs, String oreDictName) {
		for (String prefix : prefixs) {
			this.oreDictNames.add((prefix.equals("") ? oreDictName.toLowerCase().charAt(0) + Utils.capitalize(oreDictName.substring(1)) : prefix + Utils.capitalize(oreDictName)).replaceAll(" ", ""));
		}
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
			for (String oreDictName : getOreDictName()) {
				OreDictionary.registerOre(oreDictName, this);
			}
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
				texture = new ItemTexture(this.getOre(), name, getColor(), getRenderType(), "dust");
				if (!map.setTextureEntry(name, texture)) {
					LogHelper.error(getName() + ": Could not add texture after creation");
				}
			}

			itemIcon = map.getTextureExtry(name);
		}
	}
}
