package com.rikuss4.alltheores.blocks;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.rikuss4.alltheores.items.Armor.Armor;
import com.rikuss4.alltheores.items.Resources.Crushed;
import com.rikuss4.alltheores.items.Resources.CrushedPurified;
import com.rikuss4.alltheores.items.Resources.Dust;
import com.rikuss4.alltheores.items.Resources.DustTiny;
import com.rikuss4.alltheores.items.Resources.Ingot;
import com.rikuss4.alltheores.items.Resources.Nugget;
import com.rikuss4.alltheores.items.Tools.Axe;
import com.rikuss4.alltheores.items.Tools.Hoe;
import com.rikuss4.alltheores.items.Tools.Pickaxe;
import com.rikuss4.alltheores.items.Tools.Spade;
import com.rikuss4.alltheores.items.Weapons.Sword;
import com.rikuss4.alltheores.reference.Reference;
import com.rikuss4.alltheores.textures.BlockTexture;
import com.rikuss4.alltheores.utility.LogHelper;
import com.rikuss4.alltheores.utility.Utils;

import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ATOOre extends BlockOre {

   public final Block oreBlock = null;
	public String name;
	public String fullname;
	public String suffix;
	public String oreDictName;
	public String type;
	public String subType;
	public String underlyingBlockName;
	public int veinRate;
	public int veinSize;
	public int veinHeight;
	public int color;
	public int harvestLevel;
	public int hardness;
	public int oreRenderType;
	public int oreDenseRenderType;
	public int orePoorRenderType;
	public int dropType = 0;
	private int dropMin;
	private int dropMax;
	public int macerateAmount;
	public int crushAmount;
	public int smeltAmount;
	public boolean fallInstantly = false;
	public boolean falls = false;
	public boolean usePrefix = false;
	public boolean useSuffix = false;
	public boolean useDust = false;
	public boolean enableSmelt = true;
	public boolean enableMacerate = true;
	public boolean enableCrush = true;
	public List<Integer> dimWhiteList;
	public List<Integer> dimBlackList;
	public ItemStack blockItem = null;
	public ItemStack brickItem = null;
	public ItemStack smeltingOutputItem = null;
	public ItemStack ingotItem = null;
	public ItemStack nuggetItem = null;
	public ItemStack dustItem = null;
	public ItemStack dustTinyItem = null;
	public ItemStack extraDustItem = null;
	public ItemStack extraDustTinyItem = null;
	public ItemStack crushedItem = null;
	public ItemStack crushedPurifiedItem = null;
	public ItemStack swordItem = null;
	public ItemStack helmetItem = null;
	public ItemStack chestplateItem = null;
	public ItemStack legsItem = null;
	public ItemStack bootsItem = null;
	public ItemStack pickaxeItem = null;
	public ItemStack spadeItem = null;
	public ItemStack hoeItem = null;
	public ItemStack axeItem = null;
	public CrushedPurified crushedPurified = null;
	public Crushed crushed = null;
	public Ingot ingot = null;
	public Nugget nugget = null;
	public Dust dust = null;
	public DustTiny dustTiny = null;
	public ATOBlock block = null;
	public ATOBrick brick = null;
	public Sword sword = null;
	public Armor helmet = null;
	public Armor chestplate = null;
	public Armor legs = null;
	public Armor boots = null;
	public Pickaxe pickaxe = null;
	public Spade spade = null;
	public Hoe hoe = null;
	public Axe axe = null;
	public ATOOre ore = null;
	public Item itemDrop;

	public ATOOre(String name, int color, int dropType, int dropMin, int dropMax, int hardness, int harvestLevel, int oreRenderType, String type, String underlyingBlockName, int veinRate, int veinSize, int veinHeight, List<Integer> dimWhiteList, List<Integer> dimBlackList, Boolean enableSmelt, Boolean enableMacerate, Boolean enableCrush, Boolean falls, Boolean useDust, ATOOre ore) {
		// subTypes = Poor, Dense, Nether, End
		this.subType = (type.toLowerCase().startsWith("poor") ? "poor" : (type.toLowerCase().startsWith("dense") ? "dense" : (type.toLowerCase().startsWith("nethernether") ? "nether" : (type.toLowerCase().startsWith("endend") ? "end" : ""))));
		// types = Sand, Gravel, Nether, End
		this.type = (type.toLowerCase().endsWith("nether") ? "nether" : (type.toLowerCase().endsWith("end") ? "end" : (type.toLowerCase().endsWith("sand") ? "sand" : (type.toLowerCase().endsWith("gravel") ? "gravel" : ""))));
		this.name = (this.subType.equals("") || this.subType.equals("nether") || this.subType.equals("end") ? "" : this.subType + "_") + name.replaceAll("__", "_").toLowerCase() + "_" + "ore";
		this.color = color;

		LogHelper.mod_debug("*** Setup for " + this.name + " ***");
		LogHelper.mod_debug("*** " + this.getUnlocalizedName() + " ***");
		this.dropType = dropType;
		this.dropMin = dropMin;
		this.dropMax = dropMax;
		this.enableSmelt = enableSmelt;
		this.enableCrush = enableCrush;
		this.enableMacerate = enableMacerate;

		this.harvestLevel = harvestLevel;
		this.hardness = hardness;

		this.veinRate = veinRate;
		this.veinSize = veinSize;
		this.veinHeight = veinHeight;
		this.dimWhiteList = dimWhiteList;
		this.dimBlackList = dimBlackList;
		this.underlyingBlockName = underlyingBlockName;
		this.oreRenderType = oreRenderType;
		this.falls = falls;
		this.useDust = useDust;
		this.oreDictName = (this.subType.equals("") ? "ore" : ((this.subType.equals("nether") || this.subType.equals("end")) ? "ore" : this.subType + "ore")) + Utils.capitalize(name).replaceAll(" ", "");

		this.setBlockName(this.name);
		this.setBlockTextureName(this.name);
		this.setHardness(hardness);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setCreativeTab(CreativeTabs.tabAllSearch);

		if (underlyingBlockName.equalsIgnoreCase("minecraft:sand") || type == "sand") {
			this.setStepSound(soundTypeSand);
			this.setHarvestLevel("shovel", harvestLevel);
		} else if (underlyingBlockName.equalsIgnoreCase("minecraft:gravel") || type == "gravel") {
			this.setStepSound(soundTypeGravel);
			this.setHarvestLevel("shovel", harvestLevel);
		} else {
			this.setStepSound(soundTypeStone);
			this.setHarvestLevel("pickaxe", harvestLevel);
		}
		this.ore = ore;
		this.setOreType();
		try {
			LogHelper.mod_debug("dropMin: " + this.dropMin);
			LogHelper.mod_debug("dropMax: " + this.dropMax);
			LogHelper.mod_debug("smeltAmount: " + this.smeltAmount);
			LogHelper.mod_debug("crushAmount: " + this.crushAmount);
			LogHelper.mod_debug("macerateAmount: " + this.macerateAmount);
			if (crushed != null)
				LogHelper.mod_debug("crushed: " + crushed);
			if (crushedItem != null)
				LogHelper.mod_debug("crushedItem: " + crushedItem);
			if (getIngot() != null)
				LogHelper.mod_debug("getIngot: " + getIngot());
			if (getNugget() != null)
				LogHelper.mod_debug("getNugget: " + getNugget());
			if (getDust() != null)
				LogHelper.mod_debug("getDust: " + getDust());
			if (getDustTiny() != null)
				LogHelper.mod_debug("getDustTiny: " + getDustTiny());
			if (getCrushed() != null)
				LogHelper.mod_debug("getCrushed: " + getCrushed());
			if (getCrushedPurified() != null)
				LogHelper.mod_debug("getCrushedPurified: " + getCrushedPurified());
		} catch (NullPointerException e) {
		}
	}

	public ATOOre(String name, int color, int dropType, int dropMin, int dropMax, int hardness, int harvestLevel, int oreRenderType, String type, String underlyingBlockName, int veinRate, int veinSize, int veinHeight, List<Integer> dimWhiteList, List<Integer> dimBlackList, Boolean enableSmelt, Boolean enableMacerate, Boolean enableCrush, Boolean falls, Boolean useDust) {
		this(name, color, dropType, dropMin, dropMax, hardness, harvestLevel, oreRenderType, type, underlyingBlockName, veinRate, veinSize, veinHeight, dimWhiteList, dimBlackList, enableSmelt, enableMacerate, enableCrush, falls, useDust, null);
	}

	public ATOOre(String name, ATOOre ore, String type, String underlyingBlockName) {
		this(name, ore.color, ore.dropType, ore.dropMin, ore.dropMax, ore.hardness, ore.harvestLevel, ore.oreRenderType, type, underlyingBlockName, ore.veinRate, ore.veinSize, ore.veinHeight, ore.dimWhiteList, ore.dimBlackList, ore.enableSmelt, ore.enableMacerate, ore.enableCrush, ore.falls, ore.useDust, ore);
		this.oreDenseRenderType = ore.oreDenseRenderType;
		this.orePoorRenderType = ore.orePoorRenderType;
		this.fallInstantly = ore.fallInstantly;

		this.crushedPurifiedItem = ore.crushedPurifiedItem;
		this.crushedItem = ore.crushedItem;
		this.ingotItem = ore.ingotItem;
		this.dustItem = ore.dustItem;
		this.nuggetItem = ore.nuggetItem;
		this.dustTinyItem = ore.dustTinyItem;

		this.crushedPurified = ore.crushedPurified;
		this.crushed = ore.crushed;
		this.ingot = ore.ingot;
		this.dust = ore.dust;
		this.nugget = ore.nugget;
		this.dustTiny = ore.dustTiny;

		this.blockItem = ore.blockItem;
		this.brickItem = ore.brickItem;

		this.block = ore.block;
		this.brick = ore.brick;

		this.itemDrop = ore.itemDrop;
		this.sword = ore.sword;
		this.helmet = ore.helmet;
		this.chestplate = ore.chestplate;
		this.legs = ore.legs;
		this.boots = ore.boots;
		this.pickaxe = ore.pickaxe;
		this.spade = ore.spade;
		this.hoe = ore.hoe;
		this.axe = ore.axe;
	}

	public String getUnlocalizedName() {
		return "tile." + Reference.MOD_ID.toLowerCase() + "_" + this.name;
	}

	public Boolean useSuffix() {
		return useSuffix;
	}

	public String getSuffix() {
		return new ItemStack(Block.getBlockFromName(underlyingBlockName)).getDisplayName().trim();
	}

	public Boolean usePrefix() {
		return usePrefix;
	}

	public String getPrefix() {
		if (!type.equals("")) {
			return Utils.capitalize(type).trim() + " ";
		} else {
			return "";
		}
	}

	public boolean useDust() {
		return useDust;
	}

	public Boolean doSmelting() {
		return enableSmelt;
	}

	public Boolean doMacerating() {
		return enableMacerate;
	}

	public Boolean doCrushing() {
		return enableCrush;
	}

	public int getDropMin() {
		if (dropMin > dropMax) {
			return dropMax;
		} else if (dropMin < 1) {
			return 1;
		} else {
			return dropMin;
		}
	}

	public int getDropMax() {
		if (dropMax < dropMin) {
			return dropMin;
		} else {
			return dropMax;
		}
	}

	public int getAmount(int size) {
		if (size > 64) {
			return 64;
		} else if (size <= 0) {
			return 1;
		}
		return size;
	}

	@Override
	public String getTextureName() {
		return this.textureName;
	}

	@Override
	public Block setBlockName(String unlocalizedName) {
		super.setBlockName(Reference.MOD_ID.toLowerCase() + ":" + unlocalizedName);
		return this;
	}

	public ItemStack getBaseOre(int stackSize) {
		if (ore != null) {
			return new ItemStack(ore, stackSize);
		}
		return null;
	}

	public ItemStack getIngot(int stackSize) {
		if (ingotItem != null && ingotItem.getItem() != null) {
			return ingotItem.splitStack(stackSize);
		} else if (ingot != null) {
			return new ItemStack(ingot, stackSize, 0);
		}
		return null;
	}

	public ItemStack getNugget(int stackSize) {
		if (nuggetItem != null && nuggetItem.getItem() != null) {
			return nuggetItem.splitStack(stackSize);
		} else if (nugget != null) {
			return new ItemStack(nugget, stackSize, 0);
		}
		return null;
	}

	public ItemStack getDust(int stackSize) {
		if (dustItem != null && dustItem.getItem() != null) {
			return dustItem.splitStack(stackSize);
		} else if (dust != null) {
			return new ItemStack(dust, stackSize, 0);
		}
		return null;
	}

	public ItemStack getDustTiny(int stackSize) {
		if (dustTinyItem != null && dustTinyItem.getItem() != null) {
			return dustTinyItem.splitStack(stackSize);
		} else if (dustTiny != null) {
			return new ItemStack(dustTiny, stackSize);
		}
		return null;
	}

	public ItemStack getCrushed(int stackSize) {
		if (crushedItem != null && crushedItem.getItem() != null) {
			return crushedItem.splitStack(stackSize);
		} else if (crushed != null) {
			return new ItemStack(crushed, stackSize);
		}
		return null;
	}

	public ItemStack getCrushedPurified(int stackSize) {
		if (crushedPurifiedItem != null && crushedPurifiedItem.getItem() != null) {
			return crushedPurifiedItem.splitStack(stackSize);
		} else if (crushedPurified != null) {
			return new ItemStack(crushedPurified, stackSize);
		}
		return null;
	}

	public ItemStack getExtraDust(int stackSize) {
		if (extraDustItem != null && extraDustItem.getItem() != null) {
			return extraDustItem.splitStack(stackSize);
		} else {
			return getDust(stackSize);
		}
	}

	public ItemStack getExtraDustTiny(int stackSize) {
		if (extraDustTinyItem != null && extraDustTinyItem.getItem() != null) {
			return extraDustTinyItem.splitStack(stackSize);
			// return new ItemStack(extraDustTinyItem.getItem(), stackSize,
			// extraDustTinyItem.getItemDamage());
		} else {
			return getDustTiny(stackSize);
		}
	}

	public ItemStack getBlock(int stackSize) {
		if (blockItem != null && blockItem.getItem() != null) {
			return blockItem.splitStack(stackSize);
		} else if (block != null) {
			return new ItemStack(block, stackSize, 0);
		}
		return null;
	}

	public ItemStack getBrick(int stackSize) {
		if (brickItem != null && brickItem.getItem() != null) {
			return brickItem.splitStack(stackSize);
		} else if (brick != null) {
			return new ItemStack(brick, stackSize, 0);
		}
		return null;
	}

	public void registerOreDict() {
		LinkedList<String> typeStones = new LinkedList<String>();
		List blocks = new LinkedList();
		typeStones.add("igneous");
		typeStones.add("metamorphic");
		typeStones.add("sedimentary");

		LogHelper.mod_debug("All The Ores Ore Dictionary Register:");
		LogHelper.mod_debug(this.name);
		LogHelper.mod_debug(this.oreDictName);
		LogHelper.mod_debug(this.getUnlocalizedName());
		if (oreDictName != null && Reference.DEBUG) {
			OreDictionary.registerOre(oreDictName, this);
			// UBC ore dictionary
			if (Reference.isUBCLoaded) {
				String blockName = "_" + Reference.MOD_ID.toLowerCase() + "_" + name;
				for (String stone : typeStones) {
					for (int i = 0; i < 8; i++) {
						LogHelper.mod_debug(Reference.MOD_ID + ":" + stone + blockName + ":" + i);
						ItemStack blockOreItem = new ItemStack(GameRegistry.findItem(Reference.MOD_ID, stone + blockName), 1, i);
						if (blockOreItem != null && OreDictionary.getOreID(blockOreItem) == -1)
							OreDictionary.registerOre(oreDictName, blockOreItem);
					}
				}
			}
		}
	}

	public IIcon getBlockIcon() {
		return blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		if (iconRegister instanceof TextureMap) {
			TextureMap map = (TextureMap) iconRegister;
			String name = Reference.MOD_ID.toLowerCase() + ":" + this.name;
			String prefix = this.subType.toLowerCase();

			// if necessary remove prefix
			if ((!prefix.equals("poor")) && (!prefix.equals("dense"))) {
				prefix = "";
			}

			// load texture from file or generate
			TextureAtlasSprite texture = map.getTextureExtry(name);
			if (texture == null) {
				texture = new BlockTexture(name, prefix + "ore", 0, color, oreRenderType, this, false);
				if (!map.setTextureEntry(name, texture)) {
					LogHelper.error(name + ": Could not add texture after creation!");
				}
			}
			blockIcon = map.getTextureExtry(name);

			// load overlay texture from file or generate
			if (underlyingBlockName.equalsIgnoreCase("minecraft:stone")) {
				name = Reference.MOD_ID.toLowerCase() + ":" + this.name + "_overlay";
				texture = map.getTextureExtry(name);
				if (texture == null) {
					texture = new BlockTexture(name, prefix + "ore", 0, color, oreRenderType, this, true);
					if (!map.setTextureEntry(name, texture)) {
						LogHelper.error(name + "_overlay: Could not add texture after creation!");
					}
				}
			}
		}
	}

	public ItemStack getIngot() {
		return getIngot(1);
	}

	public ItemStack getDust() {
		return getDust(1);
	}

	public ItemStack getNugget() {
		return getNugget(1);
	}

	public ItemStack getDustTiny() {
		return getDustTiny(1);
	}

	public ItemStack getCrushed() {
		return getCrushed(1);
	}

	public ItemStack getCrushedPurified() {
		return getCrushedPurified(1);
	}

	public ItemStack getBaseOre() {
		return getBaseOre(1);
	}

	@Override
	public void onBlockHarvested(World p_149681_1_, int p_149681_2_, int p_149681_3_, int p_149681_4_, int p_149681_5_, EntityPlayer p_149681_6_) {
		LogHelper.mod_debug("Block Harvested:" + new ItemStack(this).getUnlocalizedName());
	}
	
	private Random rand = new Random();
	
	@Override
	public Item getItemDropped(int metadata, Random random, int fortune) {
		LogHelper.mod_debug("Block Dropped:" + new ItemStack(this).getUnlocalizedName());
		// Drop section
		// Drop types:
		// 1 = Smelting output
		// 2 = Ingot
		// 3 = Dust
		// 4 = Nugget
		// 5 = Tiny Dust
		// 6 = Base Ore (Drops self if does not exist)
		switch (dropType) {
			case 1:
				if (getOutputSmelting() != null) {
					return getOutputSmelting().getItem();
				}
			case 2:
				if (getIngot() != null) {
					return getIngot().getItem();
				}
			case 3:
				if (getDust() != null) {
					return getDust().getItem();
				}
			case 4:
				if (getNugget() != null) {
					return getNugget().getItem();
				}
			case 5:
				if (getDustTiny() != null) {
					return getDustTiny().getItem();
				}
			case 6:
				if (getBaseOre() != null) {
					return getBaseOre().getItem();
				}
			default:
				if (Reference.isUBCLoaded) {
					//oreBlock.getItemDropped(metadata, random, fortune);
	            if (oreBlock != null) LogHelper.mod_debug("oreBlock: " + oreBlock.getUnlocalizedName());
					//BlockSets.oreUbifier.get(new OreBlockInfo (this.oreBlock, this.oreMeta, this.stoneBlock, this.oreLevel+1));
					return Item.getItemFromBlock(this);
				} else
					return Item.getItemFromBlock(this);
		}
	}

	@Override
	public int damageDropped(int metadata) {
		LogHelper.mod_debug("Block Meta:" + metadata);
		switch (dropType) {
			case 1:
				if (getOutputSmelting() != null) {
					return getOutputSmelting().getItemDamage();
				}
			case 2:
				if (getIngot() != null) {
					return getIngot().getItemDamage();
				}
			case 3:
				if (getDust() != null) {
					return getDust().getItemDamage();
				}
			case 4:
				if (getNugget() != null) {
					return getNugget().getItemDamage();
				}
			case 5:
				if (getDustTiny() != null) {
					return getDustTiny().getItemDamage();
				}
			default:
				return 0;
		}
	}

	@Override
	public int quantityDropped(int metadata, int fortune, Random random) {
		LogHelper.mod_debug("Block Drop Min:" + getDropMin());
		LogHelper.mod_debug("Block Drop Max:" + getDropMax());
		LogHelper.mod_debug("Block Drop Type:" + dropType);
		if (Utils.isBetween(dropType, 1, 6)) {
			int min = Math.min(getDropMin() + fortune, getDropMax());
			int amt = MathHelper.getRandomIntegerInRange(rand, min, getDropMax());
			LogHelper.mod_debug("Block Drop Amount:" + amt);
			return amt;
		} else {
			return 1;
		}
	}

	@Override
	public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
		int exp = 0;
		if(Utils.isBetween(dropType, 1, 5))
			exp = MathHelper.ceiling_double_int(MathHelper.getRandomIntegerInRange(rand, getHarvestLevel(metadata) + getDropMin(), getHarvestLevel(metadata) + getDropMax()));
		LogHelper.mod_debug("Block XP Dropped:" + exp);
		return exp;
	}

	/*
	 * @Override
	 * public void dropBlockAsItemWithChance(World world, int par1, int par2, int
	 * par3, int par4, float par5, int par6) {
	 * super.dropBlockAsItemWithChance(world, par1, par2, par3, par4, par5,
	 * par6);
	 * int xp = 0;
	 * if (this.getItemDropped(par4, world.rand, par6) !=
	 * Item.getItemFromBlock(this)) {
	 * xp = MathHelper.getRandomIntegerInRange(world.rand, 1,
	 * MathHelper.ceiling_double_int(this.getHarvestLevel(par4) * 2.5));
	 * this.dropXpOnBlockBreak(world, par1, par2, par3, xp);
	 * }
	 * }
	 */

	private HashMap<Integer, int[]> particles = new HashMap<Integer, int[]>();

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(World world, int blockX, int blockY, int blockZ, Random rand) {

		int meta = world.getBlockMetadata(blockX, blockY, blockZ);

		if (!this.particles.containsKey(meta)) {
			return;
		}

		int[] particle = this.particles.get(meta);

		// ParticleHandler.renderOreParticle(world, blockX, blockY, blockZ, rand,
		// particle);
	}

	public void setSubBlockParticles(int meta, int type, int red, int green, int blue) {
		int[] settings = { type, red, green, blue };
		this.particles.put(meta, settings);
	}

	public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
		if (this.falls) {
			p_149726_1_.scheduleBlockUpdate(p_149726_2_, p_149726_3_, p_149726_4_, (Block) this, this.tickRate(p_149726_1_));
		}
	}

	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
		if (this.falls) {
			p_149695_1_.scheduleBlockUpdate(p_149695_2_, p_149695_3_, p_149695_4_, this, this.tickRate(p_149695_1_));
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {
		if (!p_149674_1_.isRemote) {
			this.func_149830_m(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_);
		}
	}

	private void func_149830_m(World p_149830_1_, int p_149830_2_, int p_149830_3_, int p_149830_4_) {
		if (func_149831_e(p_149830_1_, p_149830_2_, p_149830_3_ - 1, p_149830_4_) && p_149830_3_ >= 0) {
			byte b0 = 32;

			if (!fallInstantly && p_149830_1_.checkChunksExist(p_149830_2_ - b0, p_149830_3_ - b0, p_149830_4_ - b0, p_149830_2_ + b0, p_149830_3_ + b0, p_149830_4_ + b0)) {
				if (!p_149830_1_.isRemote) {
					EntityFallingBlock entityfallingblock = new EntityFallingBlock(p_149830_1_, (double) ((float) p_149830_2_ + 0.5F), (double) ((float) p_149830_3_ + 0.5F), (double) ((float) p_149830_4_ + 0.5F), this, p_149830_1_.getBlockMetadata(p_149830_2_, p_149830_3_, p_149830_4_));
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

	public static boolean func_149831_e(World p_149831_0_, int p_149831_1_, int p_149831_2_, int p_149831_3_) {
		Block block = p_149831_0_.getBlock(p_149831_1_, p_149831_2_, p_149831_3_);

		if (block.isAir(p_149831_0_, p_149831_1_, p_149831_2_, p_149831_3_)) {
			return true;
		} else if (block == Blocks.fire) {
			return true;
		} else {
			Material material = block.getMaterial();
			return material == Material.water ? true : material == Material.lava;
		}
	}

	public void func_149828_a(World p_149828_1_, int p_149828_2_, int p_149828_3_, int p_149828_4_, int p_149828_5_) {
	}

	public ItemStack getOutputSmelting() {
		int stackSize = getAmount(smeltAmount);
		if (smeltingOutputItem != null && smeltingOutputItem.getItem() != null) {
			return new ItemStack(smeltingOutputItem.getItem(), stackSize, smeltingOutputItem.getItemDamage());
		} else {
			if ((subType == "poor" || dropType == 5) && (getDustTiny() != null) && useDust()) {
				return getDustTiny(stackSize);
			} else if ((subType == "poor" || dropType == 4) && (getNugget() != null)) {
				return getNugget(stackSize);
			} else if (((dropType == 3 && getDust() != null) || getDust() != null) && useDust()) {
				return getDust(stackSize);
			} else if ((dropType == 2 && getIngot() != null) || getIngot() != null) {
				return getIngot(stackSize);
			}
		}
		return null;
	}

	public ItemStack[] getOutputCrusher() {
		LinkedList<ItemStack> itemList = new LinkedList<ItemStack>();
		int stackSize = getAmount(crushAmount);
		ItemStack dustStone = Reference.IC2StoneDust.copy();
		dustStone.stackSize = 1;
		if (subType.equals("poor")) {
			if (getDustTiny(stackSize) != null) {
				itemList.add(getDustTiny(stackSize));
				itemList.add(dustStone);
			}
		} else if (!Reference.CONFIG_OUTPUT_BASE_ORES) {
			if (getDust(stackSize) != null) {
				itemList.add(getDust(stackSize));
				itemList.add(getExtraDustTiny(1));
				itemList.add(dustStone);
			}
		} else {
			itemList.add(getBaseOre(stackSize));
		}
		return itemList.toArray(new ItemStack[itemList.size()]);
	}

	public ItemStack getOutputMacerator() {
		int stackSize = getAmount(macerateAmount);
		if (subType == "poor") {
			return getDustTiny(stackSize);
		} else if (!Reference.CONFIG_OUTPUT_BASE_ORES) {
			return getCrushed(stackSize);
		} else {
			return getBaseOre(stackSize);
		}
	}

	public void registerSmelting() {
		// Section: Smelting
		LogHelper.mod_debug("*** Smelting for " + this.name + " ***");
		if (Reference.CONFIG_ADD_SMELTING) {
			try {
				if (getOutputSmelting() != null) {
					ItemStack output = getOutputSmelting();
					ItemStack input = new ItemStack(this, 1);
					LogHelper.mod_debug("Smelt Output) " + output);
					LogHelper.mod_debug("Smelt 1) " + input + " => " + output);
					GameRegistry.addSmelting(input, output, 0.1f);
					String[] inputInfo = Utils.getItemInfo(input);
					if (this.ore == null && getIngot(1) != null && inputInfo[3] != null && inputInfo[3].startsWith("ingot")) {
						output = getIngot(1);
						input = getDust(1);
						if (input != null && (input != output)) {
							LogHelper.mod_debug("Smelt 2) " + input + " => " + output);
							GameRegistry.addSmelting(input, output, 0.1f);
						}
						input = getCrushed(1);
						if (input != null && (input != output)) {
							LogHelper.mod_debug("Smelt 3) " + input + " => " + output);
							GameRegistry.addSmelting(input, output, 0.1f);
						}
						input = getCrushedPurified(1);
						if (input != null && (input != output)) {
							LogHelper.mod_debug("Smelt 4) " + input + " => " + output);
							GameRegistry.addSmelting(input, output, 0.1f);
						}
					}
				}
			} catch (NullPointerException e) {
				LogHelper.mod_debug("Smelt Output) " + e);
			}
		}
	}

	@Method(modid = "EnderIO")
	public void registerEnderIORecipes() {
		// Section: EIO Recipes
		LogHelper.mod_debug("*** EnderIO recipes for " + this.name + " ***");
		if (ore == null) {
			if (getIngot(1) != null) {
				String[] itemInfo = Utils.getItemInfo(getIngot(1));
				if (getDust(1) != null && !itemInfo[3].toLowerCase().startsWith("gem") && !itemInfo[3].toLowerCase().startsWith("crystal")) {
					// Ingot ==> Dust
					String recipe = Utils.getEIORecipe(getIngot(1), getDust(1));
					if (recipe != null)
						FMLInterModComms.sendMessage("EnderIO", "recipe:sagmill", recipe);

					// Dust ==> Ingot
					recipe = Utils.getEIORecipe(getDust(1), getIngot(1));
					/*
					 * Vanilla Furnace recipe already does this
					 * if (recipe != null)
					 * FMLInterModComms.sendMessage("EnderIO", "recipe:alloysmelter",
					 * recipe);
					 */
				}
			}
			// This ==> Smelting Output
			String recipe = Utils.getEIORecipe(new ItemStack(this, 1), getOutputSmelting());
			/*
			 * Vanilla Furnace recipe already does this
			 * if (recipe != null)
			 * FMLInterModComms.sendMessage("EnderIO", "recipe:alloysmelter",
			 * recipe);
			 */

			// This ==> Crusher Output
			recipe = Utils.getEIORecipe(new ItemStack(this, 1), getOutputCrusher());
			if (recipe != null)
				FMLInterModComms.sendMessage("EnderIO", "recipe:sagmill", recipe);
		}
	}

	@Method(modid = "IC2")
	public void registerIC2Recipes() {
		// Section: IC2 Recipes
		LogHelper.mod_debug("*** IC2 recipes for " + this.name + " ***");
		try {
			LogHelper.mod_debug("***crushed: " + crushed);
		} catch (NullPointerException e) {
		}
		try {
			LogHelper.mod_debug("***crushedItem: " + crushedItem);
		} catch (NullPointerException e) {
		}
		try {
			LogHelper.mod_debug("***getCrushed: " + getOutputMacerator());
		} catch (NullPointerException e) {
		}
		try {
			LogHelper.mod_debug("***getCrushedPurified: " + getCrushedPurified(1));
		} catch (NullPointerException e) {
		}

		// Macerating
		LogHelper.mod_debug("***Macerating***");
		if (getOutputMacerator() != null && Recipes.macerator.getOutputFor(new ItemStack(this), true) == null) {
			IRecipeInput input = new RecipeInputItemStack(new ItemStack(this));
			ItemStack output1 = getOutputMacerator();
			LogHelper.mod_debug("1) " + input.getInputs() + " => " + output1);
			Recipes.macerator.addRecipe(input, null, output1);
		}

		if ((!subType.equals("poor"))) {
			// Ore Washing
			LogHelper.mod_debug("***Ore Washing***");
			if (getCrushed(1) != null && getCrushedPurified(1) != null && Recipes.oreWashing.getOutputFor(getCrushed(1), true) == null) {
				IRecipeInput input = new RecipeInputItemStack(getCrushed(1));
				LogHelper.mod_debug("Input) " + input.getInputs());
				ItemStack output1 = getCrushedPurified(1);
				LogHelper.mod_debug("Output 1) " + output1);
				ItemStack output3 = Reference.IC2StoneDust;
				LogHelper.mod_debug("Output 3) " + output3);
				if (getDustTiny(1) != null) {
					ItemStack output2 = getDustTiny(2);
					LogHelper.mod_debug("Output 2) " + output2);
					Recipes.oreWashing.addRecipe(input, null, output1, output2, output3);
				} else {
					Recipes.oreWashing.addRecipe(input, null, output1, output3);
				}
			}

			// Thermal Centrifuge
			// Crushed
			LogHelper.mod_debug("***Thermal Centrifuge Crushed***");
			if ((!subType.equals("poor")) && getCrushed(1) != null && getDust(1) != null && Recipes.centrifuge.getOutputFor(getCrushed(1), true) == null) {
				IRecipeInput input = new RecipeInputItemStack(getCrushed(1));
				ItemStack output1 = getDust(1);
				ItemStack output3 = Reference.IC2StoneDust;
				LogHelper.mod_debug("Output 1 ) " + output1);
				LogHelper.mod_debug("Output 1 ) " + output1);
				LogHelper.mod_debug("Output 3) " + output3);
				if (getExtraDustTiny(1) != null) {
					ItemStack output2 = getExtraDustTiny(1);
					LogHelper.mod_debug("Output 2) " + output2);
					Recipes.centrifuge.addRecipe(input, null, output1, output2, output3);
				} else {
					Recipes.centrifuge.addRecipe(input, null, output1, output3);
				}
			}
			// Purified Crushed
			LogHelper.mod_debug("***Thermal Centrifuge Purified Crushed***");
			if (getCrushedPurified(1) != null && getDust(1) != null && Recipes.centrifuge.getOutputFor(getCrushedPurified(1), true) == null) {
				IRecipeInput input = new RecipeInputItemStack(getCrushedPurified(1));
				ItemStack output1 = getDust(1);
				LogHelper.mod_debug("Input) " + input.getInputs());
				LogHelper.mod_debug("Output 1) " + output1);
				if (getExtraDustTiny(1) != null) {
					ItemStack output2 = getExtraDustTiny(1);
					LogHelper.mod_debug("Output 2) " + output2);
					Recipes.centrifuge.addRecipe(input, null, output1, output2);
				} else {
					Recipes.centrifuge.addRecipe(input, null, output1);
				}
			}
		}
	}

	public void registerCrafting() {
		// Crafting
		LogHelper.mod_debug("*** Crafting for " + this.name + " ***");
		if (this.ore == null) {
			// Block Crafting
			if (Reference.CONFIG_ADD_CRAFTING_BLOCKS && getBlock(1) != null) {
				ItemStack input = null;
				ItemStack output = null;
				// Use Ingots for Block recipe
				if (getIngot(1) != null && !useDust()) {
					output = getIngot(9);
					input = getBlock(1);
					// Use Dusts for Block recipe
				} else if (getDust(1) != null && useDust()) {
					output = getDust(9);
					input = getBlock(1);
				}
				String[] outputInfo = Utils.getItemInfo(output);
				if (outputInfo != null && !outputInfo[3].equals("") && input != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(input, "nnn", "nnn", "nnn", 'n', outputInfo[3]));
				} else if (output != null && input != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(input, "nnn", "nnn", "nnn", 'n', output));
				}
				// Use Block ==> Ingots recipe
				if (output != null) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(output, new Object[] { input }));
				}
			}

			// Brick Crafting
			if (Reference.CONFIG_ADD_CRAFTING_BRICKS && getBrick(1) != null && getIngot(1) != null && !useDust()) {
				ItemStack input = getBrick(1);
				ItemStack output = getIngot(4);
				String[] outputInfo = Utils.getItemInfo(output);

				// Use Ingots ==> Brick recipe
				if (!outputInfo[3].equals("")) {
					// GameRegistry.addRecipe(new ShapedOreRecipe(input, "nn ",
					// "nn ", "   ", 'n', outputInfo[3]));
					GameRegistry.addRecipe(new ShapelessOreRecipe(input, new Object[] { outputInfo[3], outputInfo[3], outputInfo[3], outputInfo[3] }));
				} else if (getIngot(1) != null) {
					// GameRegistry.addRecipe(new ShapedOreRecipe(input, "nn ",
					// "nn ", "   ", 'n', getIngot(1)));
					GameRegistry.addRecipe(new ShapelessOreRecipe(input, new Object[] { getIngot(1), getIngot(1), getIngot(1), getIngot(1) }));
				}
				// Use Brick ==> Ingots recipe
				if (output != null) {
					GameRegistry.addRecipe(new ShapelessOreRecipe(output, new Object[] { input }));
				}
			}

			// Nugget Crafting
			if (Reference.CONFIG_ADD_CRAFTING_NUGGETS && getIngot(1) != null && getNugget(1) != null && getIngot(1) != getNugget(1)) {
				ItemStack input = getIngot(1);
				ItemStack output = getNugget(9);
				String[] inputInfo = Utils.getItemInfo(input);
				// Use Ingot ==> Nuggets recipe
				if (input != output && !inputInfo[3].equals("")) {
					LogHelper.mod_debug("*** " + inputInfo[3] + " --> " + output.getDisplayName() + " * 9 ***");
					GameRegistry.addRecipe(new ShapelessOreRecipe(output, new Object[] { inputInfo[3] }));
				} else if (input != null && output != null) {
					LogHelper.mod_debug("*** " + input.getDisplayName() + " --> " + output.getDisplayName() + " * 9 ***");
					GameRegistry.addRecipe(new ShapelessOreRecipe(output, new Object[] { input }));
				}
			}
			if (Reference.CONFIG_ADD_CRAFTING_INGOTS && getIngot(1) != null && getNugget(1) != null && getIngot(1) != getNugget(1)) {
				ItemStack input = getNugget(9);
				ItemStack output = getIngot(1);
				String[] inputInfo = Utils.getItemInfo(input);
				// Use Nuggets ==> Ingot recipe
				if (input != output && !inputInfo[3].equals("")) {
					LogHelper.mod_debug("*** " + inputInfo[3] + " * 9 --> " + output.getDisplayName() + " ***");
					GameRegistry.addRecipe(new ShapedOreRecipe(output, "nnn", "nnn", "nnn", 'n', inputInfo[3]));
				} else if (input != null && output != null) {
					LogHelper.mod_debug("*** " + input.getDisplayName() + " * 9 --> " + output.getDisplayName() + " ***");
					GameRegistry.addRecipe(new ShapedOreRecipe(output, "nnn", "nnn", "nnn", 'n', input));
				}
			}

			// Tiny Dust Crafting
			if (Reference.CONFIG_ADD_CRAFTING_NUGGETS && getDust(1) != null && getDustTiny(1) != null && getDust(1) != getDustTiny(1)) {
				ItemStack input = getDust(1);
				ItemStack output = getDustTiny(9);
				String[] inputInfo = Utils.getItemInfo(input);
				// Use Ingot ==> Nuggets recipe
				if (input != output && !inputInfo[3].equals("")) {
					LogHelper.mod_debug("*** " + inputInfo[3] + " --> " + output.getDisplayName() + " * 9 ***");
					GameRegistry.addRecipe(new ShapelessOreRecipe(output, new Object[] { inputInfo[3] }));
				} else if (input != null && output != null) {
					LogHelper.mod_debug("*** " + input.getDisplayName() + " --> " + output.getDisplayName() + " * 9 ***");
					GameRegistry.addRecipe(new ShapelessOreRecipe(output, new Object[] { input }));
				}
			}
			if (Reference.CONFIG_ADD_CRAFTING_INGOTS && getDust(1) != null && getDustTiny(1) != null && getDust(1) != getDustTiny(1)) {
				ItemStack input = getDustTiny(9);
				ItemStack output = getDust(1);
				String[] inputInfo = Utils.getItemInfo(input);
				// Use Nuggets ==> Ingot recipe
				if (input != output && !inputInfo[3].equals("")) {
					LogHelper.mod_debug("*** " + inputInfo[3] + " * 9 --> " + output.getDisplayName() + " ***");
					GameRegistry.addRecipe(new ShapedOreRecipe(output, "nnn", "nnn", "nnn", 'n', inputInfo[3]));
				} else if (input != null && output != null) {
					LogHelper.mod_debug("*** " + input.getDisplayName() + " * 9 --> " + output.getDisplayName() + " ***");
					GameRegistry.addRecipe(new ShapedOreRecipe(output, "nnn", "nnn", "nnn", 'n', input));
				}
			}

			// Weapon Crafting
			if (Reference.CONFIG_ADD_CRAFTING_WEAPONS && getIngot(1) != null) {
				String inputOreDict = null;
				String inputStick = "stickWood";
				ItemStack input = getIngot(1);
				ItemStack output = new ItemStack(sword, 1);

				if (sword != null && ingot != null && ingot.getOreDictName() != null) {
					inputOreDict = ingot.getOreDictName().get(0);
					GameRegistry.addRecipe(new ShapedOreRecipe(output, " n ", " n ", " m ", 'n', inputOreDict, 'm', inputStick));
				} else if (sword != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(output, " n ", " n ", " m ", 'n', input, 'm', inputStick));
				}
			}

			// Tools Crafting
			if (Reference.CONFIG_ADD_CRAFTING_TOOLS && getIngot(1) != null) {
				ItemStack input = getIngot(1);
				String inputStick = "stickWood";
				String inputIngot = null;
				if (input != null && ingot != null && ingot.getOreDictName() != null) {
					inputIngot = ingot.getOreDictName().get(0);
					if (spade != null) {
						ItemStack output = new ItemStack(spade, 1, 0);
						GameRegistry.addRecipe(new ShapedOreRecipe(output, " n ", " m ", " m ", 'n', inputIngot, 'm', inputStick));
					}
					if (hoe != null) {
						ItemStack output = new ItemStack(hoe, 1, 0);
						GameRegistry.addRecipe(new ShapedOreRecipe(output, "nn ", " m ", " m ", 'n', inputIngot, 'm', inputStick));
					}
					if (pickaxe != null) {
						ItemStack output = new ItemStack(pickaxe, 1, 0);
						GameRegistry.addRecipe(new ShapedOreRecipe(output, "nnn", " m ", " m ", 'n', inputIngot, 'm', inputStick));
					}
					if (axe != null) {
						ItemStack output = new ItemStack(axe, 1, 0);
						GameRegistry.addRecipe(new ShapedOreRecipe(output, "nn ", "nm ", " m ", 'n', inputIngot, 'm', inputStick));
					}
				} else if (input != null) {
					if (spade != null) {
						ItemStack output = new ItemStack(spade, 1, 0);
						GameRegistry.addRecipe(new ShapedOreRecipe(output, " n ", " m ", " m ", 'n', input, 'm', inputStick));
					}
					if (hoe != null) {
						ItemStack output = new ItemStack(hoe, 1, 0);
						GameRegistry.addRecipe(new ShapedOreRecipe(output, "nn ", " m ", " m ", 'n', input, 'm', inputStick));
					}
					if (pickaxe != null) {
						ItemStack output = new ItemStack(pickaxe, 1, 0);
						GameRegistry.addRecipe(new ShapedOreRecipe(output, "nnn", " m ", " m ", 'n', input, 'm', inputStick));
					}
					if (axe != null) {
						ItemStack output = new ItemStack(axe, 1, 0);
						GameRegistry.addRecipe(new ShapedOreRecipe(output, "nn ", "nm ", " m ", 'n', input, 'm', inputStick));
					}
				}
			}

			// Armor Crafting
			if (Reference.CONFIG_ADD_CRAFTING_ARMOR) {
				if (ingot != null && ingot.getOreDictName() != null) {
					String input = ingot.getOreDictName().get(0);
					if (helmet != null) {
						GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(helmet, 1, 0), "nnn", "n n", " m ", 'n', input));
					}
					if (chestplate != null) {
						GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chestplate, 1, 0), "n n", "nnn", "nnn", 'n', input));
					}
					if (legs != null) {
						GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(legs, 1, 0), "nnn", "n n", "n n", 'n', input));
					}
					if (boots != null) {
						GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(boots, 1, 0), "   ", "n n", "n n", 'n', input));
					}
				} else {
					ItemStack input = getIngot(1);
					if (input != null) {
						if (helmet != null) {
							GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(helmet, 1, 0), "nnn", "n n", "   ", 'n', input));
						}
						if (chestplate != null) {
							GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chestplate, 1, 0), "n n", "nnn", "nnn", 'n', input));
						}
						if (legs != null) {
							GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(legs, 1, 0), "nnn", "n n", "n n", 'n', input));
						}
						if (boots != null) {
							GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(boots, 1, 0), "   ", "n n", "n n", 'n', input));
						}
					}
				}
			}
		}
	}

	public ItemStack getDictName(String suffix, String... names) {
		ItemStack item = null;
		for (String name : names) {
			item = Utils.getOreDict(name + suffix);
			if (item != null) {
				return item;
			}
		}
		if (suffix.toLowerCase().startsWith(subType)) {
			suffix = suffix.substring(subType.length());
			for (String name : names) {
				item = Utils.getOreDict(name + suffix);
				if (item != null) {
					return item;
				}
			}
		}
		if (suffix.toLowerCase().startsWith(type)) {
			suffix = suffix.substring(type.length());
			for (String name : names) {
				item = Utils.getOreDict(name + suffix);
				if (item != null) {
					return item;
				}
			}
		}
		return null;
	}

	public void setIngot(String name, Ingot item, Boolean force) {
		if (!force) {
			ItemStack newItem = Utils.getItemStack(name, 1);
			if (newItem != null) {
				this.ingotItem = newItem;
				return;
			} else if (oreDictName != null && Reference.useOreDictForItems) {
				newItem = getDictName(oreDictName.substring(3), "ingot", "gem");
				if (newItem != null) {
					this.ingotItem = newItem;
					return;
				}
			}
		}
		this.ingot = item;
	}

	public void setNugget(String name, Nugget item, Boolean force) {
		if (!force) {
			ItemStack newItem = Utils.getItemStack(name, 1);
			if (newItem != null) {
				this.nuggetItem = newItem;
				return;
			} else if (oreDictName != null && Reference.useOreDictForItems) {
				newItem = getDictName(oreDictName.substring(3), "nugget");
				if (newItem != null) {
					this.nuggetItem = newItem;
					return;
				}
			}
		}
		this.nugget = item;
	}

	public void setDust(String name, Dust item, Boolean force) {
		LogHelper.mod_debug("*** Set Dust for " + this.name + " ***");
		if (!force) {
			ItemStack newItem = Utils.getItemStack(name, 1);
			if (newItem != null) {
				this.dustItem = newItem;
				LogHelper.mod_debug("*** Dust Meta = " + this.dustItem.getItemDamage() + " ***");
				return;
			} else if (oreDictName != null && Reference.useOreDictForItems) {
				newItem = getDictName(oreDictName.substring(3), "dust");
				if (newItem != null) {
					this.dustItem = newItem;
					LogHelper.mod_debug("*** Dust Meta = " + this.dustItem.getItemDamage() + " ***");
					return;
				}
			}
		}
		this.dust = item;
	}

	public void setDustTiny(String name, DustTiny item, Boolean force) {
		if (!force) {
			ItemStack newItem = Utils.getItemStack(name, 1);
			if (newItem != null) {
				this.dustTinyItem = newItem;
				return;
			} else if (oreDictName != null && Reference.useOreDictForItems) {
				newItem = getDictName(oreDictName.substring(3), "dustTiny");
				if (newItem != null) {
					this.dustTinyItem = newItem;
					return;
				}
			}
		}
		this.dustTiny = item;
	}

	public void setCrushed(String name, Crushed item, Boolean force) {
		if (!force) {
			ItemStack newItem = Utils.getItemStack(name, 1);
			if (newItem != null) {
				this.crushedItem = newItem;
				return;
			} else if (oreDictName != null && Reference.useOreDictForItems) {
				newItem = getDictName(oreDictName.substring(3), "crushed");
				if (newItem != null) {
					this.crushedItem = newItem;
					return;
				}
			}
		}
		this.crushed = item;
	}

	public void setCrushedPurified(String name, CrushedPurified item, Boolean force) {
		if (!force) {
			ItemStack newItem = Utils.getItemStack(name, 1);
			if (newItem != null) {
				this.crushedPurifiedItem = newItem;
				return;
			} else if (oreDictName != null && Reference.useOreDictForItems) {
				newItem = getDictName(oreDictName.substring(3), "crushedPurified");
				if (newItem != null) {
					this.crushedPurifiedItem = newItem;
					return;
				}
			}
		}
		this.crushedPurified = item;
	}

	public void setBlock(String name, ATOBlock item, Boolean force) {
		if (!force) {
			ItemStack newItem = Utils.getItemStack(name, 1);
			if (newItem != null) {
				this.blockItem = newItem;
				return;
			} else if (oreDictName != null && Reference.useOreDictForItems) {
				newItem = getDictName(oreDictName.substring(3), "block");
				if (newItem != null) {
					this.blockItem = newItem;
					return;
				}
			}
		}
		this.block = item;
	}

	public void setBrick(String name, ATOBrick item, Boolean force) {
		if (!force) {
			ItemStack newItem = Utils.getItemStack(name, 1);
			if (newItem != null) {
				this.brickItem = newItem;
				return;
			} else if (oreDictName != null && Reference.useOreDictForItems) {
				newItem = getDictName(oreDictName.substring(3), "brick");
				if (newItem != null) {
					this.brickItem = newItem;
					return;
				}
			}
		}
		this.brick = item;
	}

	public void setExtraDust(String name) {
		ItemStack newItem = Utils.getItemStack(name, 1);
		if (newItem == null) {
			this.extraDustItem = getDust(1);
		} else {
			this.extraDustItem = newItem;
		}
	}

	public void setExtraDustTiny(String name) {
		ItemStack newItem = Utils.getItemStack(name, 1);
		if (newItem == null) {
			this.extraDustTinyItem = getDustTiny(1);
		} else {
			this.extraDustTinyItem = newItem;
		}
	}

	/**
	 * Sets the ore smelting output.
	 *
	 * @param name
	 *           Full item name
	 */
	public void setSmeltingOutput(String name) {
		setSmeltingOutput(name, null);
	}

	/**
	 * Sets the ore smelting output.
	 *
	 * @param item
	 *           Item stack to use
	 */
	public void setSmeltingOutput(ItemStack item) {
		setSmeltingOutput("", item);
	}

	public void setSmeltingOutput(int smeltAmount) {
		setSmeltingOutput("", new ItemStack(this, 0, smeltAmount));
	}

	public void setSmeltingOutput() {
		setSmeltingOutput("", new ItemStack(this, 0, smeltAmount));
	}

	/**
	 * Sets the ore smelting output.
	 *
	 * @param name
	 *           Full item name
	 * @param item
	 *           Item stack to use
	 */
	public void setSmeltingOutput(String name, ItemStack item) {
		if (item != null && item.getItem() != null) {
			smeltingOutputItem = item;
		} else {
			ItemStack newItem = Utils.getItemStack(name, 1);
			if (newItem != null) {
				smeltingOutputItem = newItem;
			} else if (oreDictName != null && Reference.useOreDictForItems) {
				newItem = getDictName(oreDictName.substring(3), (useDust() ? "dust" : "ingot"), "gem");
				if (newItem != null) {
					this.smeltingOutputItem = newItem;
				} else {
					this.smeltingOutputItem = null;
				}
			}
		}
	}

	public boolean isDust() {
		if (dropType == 3 || dropType == 5 || useDust())
			return true;
		return false;
	}

	public void setOreValues(int dropMin, int dropMax, int smeltAmount, int crushAmount, int macerateAmount) {
		this.dropMin = dropMin;
		this.dropMax = dropMax;
		this.smeltAmount = smeltAmount;
		this.crushAmount = crushAmount;
		this.macerateAmount = macerateAmount;
	}

	public void setOreType() {
		// Section: Setup values for various ores
		// subTypes = Poor, Dense, Nether, End
		// types = Sand, Gravel, Nether, End
		// Drop types:
		// 1 = Smelting output
		// 2 = Ingot
		// 3 = Dust
		// 4 = Nugget
		// 5 = Tiny Dust
		// 6 = Base Ore (Drops self if does not exist)
		if (subType.equalsIgnoreCase("poor")) {
			if (type.equalsIgnoreCase("nether") || type.equalsIgnoreCase("end")) {
				// Poor Nether/End Ores
				if (isDust()) {
					crushedItem = getDustTiny();
					dropType = (dropType == 3 || dropType == 0) ? 5 : 0;
					setOreValues(2, 8, 4, 8, 8);
					setSmeltingOutput(getDustTiny(smeltAmount));
				} else {
					crushedItem = getNugget();
					dropType = (dropType == 2 || dropType == 0) ? 0 : 0;
					setOreValues(1, 1, 3, 6, 6);
					setSmeltingOutput(getNugget(smeltAmount));
				}
			} else {
				// Poor Sand/Gravel/Stone Ores
				if (isDust()) {
					crushedItem = getDustTiny();
					dropType = (dropType == 3) ? 5 : 0;
					setOreValues(1, 4, 2, 4, 4);
					setSmeltingOutput(getDustTiny(smeltAmount));
				} else {
					crushedItem = getNugget();
					dropType = (dropType == 2) ? 4 : 6;
					setOreValues(1, 2, 1, 2, 2);
					setSmeltingOutput(getNugget(smeltAmount));
				}
			}
		} else if (subType.equalsIgnoreCase("dense")) {
			if (type.equalsIgnoreCase("nether") || type.equalsIgnoreCase("end")) {
				// Dense Nether/End Ores
				if (isDust() || dropType == 3) {
					dropType = 3;
					setOreValues(8, 32, 16, 32, 32);
					setSmeltingOutput(getDust(smeltAmount));
				} else {
					if (dropType == 2) {
						setOreValues(6, 16, 8, 16, 16);
						setSmeltingOutput(getIngot(smeltAmount));
					} else {
						dropType = 0;
						setOreValues(1, 1, 8, 16, 16);
						setSmeltingOutput(smeltAmount);
					}
				}
			} else {
				// Dense Sand/Gravel/Stone Ores
				if (isDust() || dropType == 3) {
					dropType = 3;
					setOreValues(3, 8, 6, 8, 8);
					setSmeltingOutput(getDust(smeltAmount));
				} else {
					if (dropType == 2) {
						setOreValues(2, 6, 3, 6, 6);
						setSmeltingOutput(getIngot(smeltAmount));
					} else {
						dropType = 6;
						setOreValues(3, 3, 3, 6, 6);
						setSmeltingOutput(smeltAmount);
					}
				}
			}
		} else {
			if (type.equalsIgnoreCase("nether") || type.equalsIgnoreCase("end")) {
				// Nether/End Ores
				if (isDust() || dropType == 3) {
					dropType = 3;
					setOreValues(3, 2, 8, 16, 16);
					setSmeltingOutput(getDust(smeltAmount));
				} else {
					if (dropType == 2) {
						setOreValues(2, 1, 4, 8, 8);
						setSmeltingOutput(getIngot(smeltAmount));
					} else {
						dropType = 0;
						setOreValues(1, 1, 4, 8, 8);
						setSmeltingOutput(smeltAmount);
					}
				}
			} else {
				// Stone Ores
				if (isDust() || dropType == 3) {
					dropType = 3;
					setOreValues(1, 2, 2, 4, 4);
					setSmeltingOutput(getDust(smeltAmount));
				} else {
					if (dropType == 2) {
						setOreValues(1, 1, 1, 2, 2);
						setSmeltingOutput(getIngot(smeltAmount));
					} else {
						dropType = 0;
						setOreValues(1, 1, 1, 2, 2);
						setSmeltingOutput(getIngot(smeltAmount));
					}
				}
			}
		}
	}

	public boolean createBlock() {
		if (block != null && (blockItem == null || blockItem.getItem() == null)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean createBrick() {
		if (!useDust() && brick != null && (brickItem == null || brickItem.getItem() == null)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean createIngot() {
		if (ingot != null && (ingotItem == null || ingotItem.getItem() == null)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean createDust() {
		if (dust != null && (dustItem == null || dustItem.getItem() == null)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean createNugget() {
		if (nugget != null && (nuggetItem == null || ingotItem.getItem() == null)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean createDustTiny() {
		if (dustTiny != null && (dustTinyItem == null || dustTinyItem.getItem() == null)) {
			return true;
		}
		return false;
	}

	public boolean createCrushed() {
		if (crushedItem == null && getCrushed(1) != null && getCrushed(1).isItemEqual(new ItemStack(crushed, 1))) {
			return true;
		}
		return false;
	}

	public boolean createCrushedPurified() {
		if (crushedPurifiedItem == null && getCrushedPurified(1) != null && getCrushedPurified(1).isItemEqual(new ItemStack(crushedPurified, 1))) {
			return true;
		}
		return false;
	}
}
