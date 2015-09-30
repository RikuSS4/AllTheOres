package com.rikuss4.alltheores.items.Armor;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.rikuss4.alltheores.blocks.ATOOre;
import com.rikuss4.alltheores.reference.Reference;
import com.rikuss4.alltheores.textures.ArmorTexture;
import com.rikuss4.alltheores.textures.ItemTexture;
import com.rikuss4.alltheores.utility.LogHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Armor extends ItemArmor {
	private ATOOre ore;
	private String name;
	private String typeName;
	private int color;
	private int renderType;
	private ArmorMaterial material;

	public Armor(String unlocalizedName, int RenderID, ArmorMaterial material, int type, ATOOre ore, int color, int renderType) {
		super(material, RenderID, type);
		this.typeName = (type == 1 ? "chestplate" : (type == 2 ? "legs" : (type == 3 ? "boots" : "helmet")));
		this.setName(unlocalizedName);
		this.setMaterial(material);
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
		return (StatCollector.translateToLocal("AllTheOres." + this.typeName + "Substite")).replaceFirst("ORENAME", name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public void setMaterial(ArmorMaterial material) {
		this.material = material;
	}

	public ArmorMaterial getMaterial() {
		return material;
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
				texture = new ItemTexture(getOre(), name, getColor(), getRenderType(), getTypeName());
				if (!map.setTextureEntry(name, texture)) {
					LogHelper.error(getName() + ": Could not add texture after creation");
				}
			}
			itemIcon = map.getTextureExtry(name);

			// load texture from file or generate
			name = Reference.MOD_ID.toLowerCase() + ":textures/models/armor/" + getName();
			texture = map.getTextureExtry(name);
			if (texture == null) {
				texture = new ArmorTexture(getOre(), name, getColor(), getRenderType(), this.armorType == 2 ? 2 : 1);
				if (!map.setTextureEntry(name, texture)) {
					LogHelper.error(getName() + ": Could not add texture after creation");
				}
			}
		}
	}

	/*
	 * @Override
	 * 
	 * @SideOnly(Side.CLIENT) public ModelBiped getArmorModel(EntityLivingBase
	 * entityLiving, ItemStack itemStack, int armorSlot) { ModelBiped armor =
	 * new ModelBiped(); return null; }
	 */

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		// String name = Reference.MOD_ID.toLowerCase() + ":" + "armor_" +
		// getName() + "_" + this.typeName;
		// String name = Reference.MOD_ID.toLowerCase() +
		// ":textures/models/armor/" + getName() + ".png";
		String name = "minecraft:textures/models/armor/" + getName() + ".png";
		// String name = Reference.MOD_ID + ":textures/models/armor/armor_" +
		// (this.armorType == 2 ? "2" : "1") + "_" + this.renderType +".png";
		LogHelper.mod_debug("type = " + type);
		LogHelper.mod_debug("slot = " + slot);
		LogHelper.mod_debug("name = " + name);
		return name;
		// return null;
	}
}
