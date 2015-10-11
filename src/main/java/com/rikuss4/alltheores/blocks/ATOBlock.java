package com.rikuss4.alltheores.blocks;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.rikuss4.alltheores.reference.Reference;
import com.rikuss4.alltheores.textures.BlockTexture;
import com.rikuss4.alltheores.utility.LogHelper;
import com.rikuss4.alltheores.utility.Utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ATOBlock extends Block {

	private String name;
	private String fullname;
	private String oreDictName;
	private int color;
	private int burnTime;
	private int blockRenderType;
	private boolean fallInstantly = false;
	private boolean fall = false;
	private boolean isCraftable = false;
	private ATOOre ore;

	public ATOBlock(String name, ATOOre ore, int color, int renderType, int burnTime) {
		super(Material.iron);
		this.setStepSound(soundTypeMetal);
		String lname = name.replaceAll("__", "_").toLowerCase() + "_block";
		name = Character.toString(name.charAt(0)).toUpperCase() + name.substring(1);
		setName(lname);
		setFullname(name);
		setBlockName(lname);
		setColor(color);
		setBlockTextureName(lname);
		setBlockRenderType(renderType);
		setOre(ore);
		setOreDictName("block" + getFullname().replaceAll(" ", ""));
		setCreativeTab(CreativeTabs.tabBlock);
		setCreativeTab(CreativeTabs.tabAllSearch);
	}

	public Boolean isCraftable() {
		return isCraftable;
	}

	public void setCraftable(Boolean isCraftable) {
		this.isCraftable = isCraftable;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public int getBlockRenderType() {
		return blockRenderType;
	}

	public void setBlockRenderType(int blockRenderType) {
		this.blockRenderType = blockRenderType;
	}

	public ATOOre getOre() {
		return ore;
	}

	public void setOre(ATOOre ore) {
		this.ore = ore;
	}

	public String getTextureName() {
		return this.textureName;
	}

	public String getOreDictName() {
		return oreDictName;
	}

	public void setOreDictName(String oreDictName) {
		this.oreDictName = oreDictName;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		if (fullname.lastIndexOf("__") > 0) {
			// setSuffix(fullname.substring(fullname.lastIndexOf("__") + 2));
			fullname = fullname.substring(0, fullname.lastIndexOf("__"));
		}
		Pattern p = Pattern.compile("_([a-zA-Z])");
		Matcher m = p.matcher(Character.toString(fullname.charAt(0)).toUpperCase() + fullname
		      .substring(1));
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, " " + m.group(1).toUpperCase());
		}
		m.appendTail(sb);
		this.fullname = sb.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void registerOreDict() {
		OreDictionary.registerOre(getOreDictName(), this);
	}

	public void registerCrafting() {
	}

	public IIcon getBlockIcon() {
		return blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		if (iconRegister instanceof TextureMap) {
			TextureMap map = (TextureMap) iconRegister;

			String name = Reference.MOD_ID.toLowerCase() + ":" + getName();
			// load texture from file or generate
			TextureAtlasSprite texture = map.getTextureExtry(name);
			if (texture == null) {
				texture = new BlockTexture(name, "block", 0, getColor(), getBlockRenderType());
				if (!map.setTextureEntry(name, texture)) {
					LogHelper.error(getName() + ": Could not add texture after creation!");
				}
			}

			blockIcon = map.getTextureExtry(name);
		}
	}

	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
		if (fall) {
			p_149726_1_.scheduleBlockUpdate(p_149726_2_, p_149726_3_, p_149726_4_, (Block) this,
			      this.tickRate(p_149726_1_));
		}
	}

	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_,
	      int p_149695_4_, Block p_149695_5_) {
		if (fall) {
			p_149695_1_.scheduleBlockUpdate(p_149695_2_, p_149695_3_, p_149695_4_, this,
			      this.tickRate(p_149695_1_));
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_,
	      Random p_149674_5_) {
		if (!p_149674_1_.isRemote) {
			this.func_149830_m(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_);
		}
	}

	private void func_149830_m(World p_149830_1_, int p_149830_2_, int p_149830_3_, int p_149830_4_) {
		if (func_149831_e(p_149830_1_, p_149830_2_, p_149830_3_ - 1, p_149830_4_) && p_149830_3_ >= 0) {
			byte b0 = 32;

			if (!fallInstantly && p_149830_1_.checkChunksExist(p_149830_2_ - b0, p_149830_3_ - b0,
			      p_149830_4_ - b0, p_149830_2_ + b0, p_149830_3_ + b0, p_149830_4_ + b0)) {
				if (!p_149830_1_.isRemote) {
					EntityFallingBlock entityfallingblock = new EntityFallingBlock(p_149830_1_,
					      (double) ((float) p_149830_2_ + 0.5F), (double) ((float) p_149830_3_ + 0.5F),
					      (double) ((float) p_149830_4_ + 0.5F), this, p_149830_1_.getBlockMetadata(
					            p_149830_2_, p_149830_3_, p_149830_4_));
					this.func_149829_a(entityfallingblock);
					p_149830_1_.spawnEntityInWorld(entityfallingblock);
				}
			} else {
				p_149830_1_.setBlockToAir(p_149830_2_, p_149830_3_, p_149830_4_);

				while (func_149831_e(p_149830_1_, p_149830_2_, p_149830_3_ - 1, p_149830_4_) && p_149830_3_ > 0) {
					--p_149830_3_;
				}

				if (p_149830_3_ > 0) {
					p_149830_1_.setBlock(p_149830_2_, p_149830_3_, p_149830_4_, this);
				}
			}
		}
	}

	protected void func_149829_a(EntityFallingBlock p_149829_1_) {
	}

	/**
	 * How many world ticks before ticking
	 */
	public int tickRate(World p_149738_1_) {
		return 2;
	}

	public static boolean func_149831_e(World p_149831_0_, int p_149831_1_, int p_149831_2_,
	      int p_149831_3_) {
		Block block = p_149831_0_.getBlock(p_149831_1_, p_149831_2_, p_149831_3_);

		if (block.isAir(p_149831_0_, p_149831_1_, p_149831_2_, p_149831_3_)) {
			return true;
		} else if (block == Blocks.fire) {
			return true;
		} else {
			// TODO: King, take a look here when doing liquids!
			Material material = block.getMaterial();
			return material == Material.water ? true : material == Material.lava;
		}
	}

	public void func_149828_a(World p_149828_1_, int p_149828_2_, int p_149828_3_, int p_149828_4_,
	      int p_149828_5_) {
	}
}
