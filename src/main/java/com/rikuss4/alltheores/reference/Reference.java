package com.rikuss4.alltheores.reference;

import java.util.LinkedList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.rikuss4.alltheores.blocks.ATOOre;

import cpw.mods.fml.common.Loader;

public class Reference {
	public static final String MOD_ID = "AllTheOres";
	public static final String MOD_NAME = "All The Ores";
	public static final String VERSION = "0.1";
	public static final String DEPS = "after:*;after:aobd";
	public static final String RESOURCE_PREFIX = MOD_ID.toLowerCase() + ":";
	public static final String CONFIG_PREFIX = ".";
	public static final Boolean DEBUG = true;

	public static boolean isIC2Loaded = false;
	public static boolean isEIOLoaded = false;
	public static boolean isUBCLoaded = false;

	public static final ItemStack IC2StoneDust = Loader.isModLoaded("IC2") ? new ItemStack(
			(Item) Item.itemRegistry.getObject("IC2:itemDust"), 1, 9) : null;
	public static final boolean useOreDictForItems = true;

	//Preferred mod order when searching through ore dictionary
	public static LinkedList<String> PreferredOrder = new LinkedList<String>();

	public static LinkedList<ATOOre> ORES_LIST = new LinkedList<ATOOre>();
	public static boolean CONFIG_WORLDGEN_COAL = false;
	public static boolean CONFIG_WORLDGEN_DIAMOND = false;
	public static boolean CONFIG_WORLDGEN_EMERALD = false;
	public static boolean CONFIG_WORLDGEN_GOLD = false;
	public static boolean CONFIG_WORLDGEN_IRON = false;
	public static boolean CONFIG_WORLDGEN_LAPIS = false;
	public static boolean CONFIG_WORLDGEN_REDSTONE = false;
	public static boolean CONFIG_WORLDGEN_QUARTZ = false;
	public static boolean CONFIG_ADD_SMELTING = true;
	public static boolean CONFIG_ADD_CRAFTING_INGOTS = true;
	public static boolean CONFIG_ADD_CRAFTING_DUSTS = true;
	public static boolean CONFIG_ADD_CRAFTING_NUGGETS = true;
	public static boolean CONFIG_ADD_CRAFTING_INGOT = true;
	public static boolean CONFIG_ADD_CRAFTING_DUSTSTINY = true;
	public static boolean CONFIG_ADD_CRAFTING_BLOCKS = true;
	public static boolean CONFIG_ADD_CRAFTING_BRICKS = true;
	public static boolean CONFIG_ADD_CRAFTING_WEAPONS = false;
	public static boolean CONFIG_ADD_CRAFTING_TOOLS = false;
	public static boolean CONFIG_ADD_CRAFTING_ARMOR = false;
	public static boolean CONFIG_GENERATE_GRAVEL_ORES = true;
	public static boolean CONFIG_GENERATE_POORGRAVEL_ORES = true;
	public static boolean CONFIG_GENERATE_DENSEGRAVEL_ORES = true;
	public static boolean CONFIG_GENERATE_SAND_ORES = true;
	public static boolean CONFIG_GENERATE_POORSAND_ORES = true;
	public static boolean CONFIG_GENERATE_DENSESAND_ORES = true;
	public static boolean CONFIG_GENERATE_POOR_ORES = true;
	public static boolean CONFIG_GENERATE_DENSE_ORES = true;
	public static boolean CONFIG_GENERATE_NETHER_ORES = true;
	public static boolean CONFIG_GENERATE_POORNETHER_ORES = true;
	public static boolean CONFIG_GENERATE_DENSENETHER_ORES = true;
	public static boolean CONFIG_GENERATE_END_ORES = true;
	public static boolean CONFIG_GENERATE_POOREND_ORES = true;
	public static boolean CONFIG_GENERATE_DENSEEND_ORES = true;
	public static boolean CONFIG_REMOVE_CRAFTING_RECIPES = false;
	public static boolean CONFIG_GENERATE_ORES_CONFIG = false;
	public static boolean CONFIG_OUTPUT_BASE_ORES = false;
	public static boolean CONFIG_FORCE_OVERWRITE_MOD_RECIPES = true;
}
