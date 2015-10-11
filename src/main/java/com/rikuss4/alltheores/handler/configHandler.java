package com.rikuss4.alltheores.handler;

import ic2.api.recipe.Recipes;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;

import com.rikuss4.alltheores.blocks.ATOBlock;
import com.rikuss4.alltheores.blocks.ATOBrick;
import com.rikuss4.alltheores.blocks.ATOOre;
import com.rikuss4.alltheores.items.Armor.Armor;
import com.rikuss4.alltheores.items.Resources.ATOCrushed;
import com.rikuss4.alltheores.items.Resources.ATOCrushedPurified;
import com.rikuss4.alltheores.items.Resources.ATODust;
import com.rikuss4.alltheores.items.Resources.ATODustTiny;
import com.rikuss4.alltheores.items.Resources.ATOIngot;
import com.rikuss4.alltheores.items.Resources.ATONugget;
import com.rikuss4.alltheores.items.Tools.Axe;
import com.rikuss4.alltheores.items.Tools.Hoe;
import com.rikuss4.alltheores.items.Tools.Pickaxe;
import com.rikuss4.alltheores.items.Tools.Spade;
import com.rikuss4.alltheores.items.Weapons.Sword;
import com.rikuss4.alltheores.reference.Reference;
import com.rikuss4.alltheores.utility.ImageUtils;
import com.rikuss4.alltheores.utility.LogHelper;
import com.rikuss4.alltheores.utility.Utils;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;

public class configHandler {

	private static int oreRenderType;
	private static String underlyingBlock;
	private static String modID;
	static LinkedList<ATOOre> POORORES_LIST = new LinkedList<ATOOre>();
	static LinkedList<ATOOre> ORES_LIST = new LinkedList<ATOOre>();
	static LinkedList<ATOOre> DENSEORES_LIST = new LinkedList<ATOOre>();
	static LinkedList<ATOOre> POORSANDORES_LIST = new LinkedList<ATOOre>();
	static LinkedList<ATOOre> SANDORES_LIST = new LinkedList<ATOOre>();
	static LinkedList<ATOOre> DENSESANDORES_LIST = new LinkedList<ATOOre>();
	static LinkedList<ATOOre> POORGRAVELORES_LIST = new LinkedList<ATOOre>();
	static LinkedList<ATOOre> GRAVELORES_LIST = new LinkedList<ATOOre>();
	static LinkedList<ATOOre> DENSEGRAVELORES_LIST = new LinkedList<ATOOre>();
	static LinkedList<ATOOre> POORNETHERORES_LIST = new LinkedList<ATOOre>();
	static LinkedList<ATOOre> NETHERORES_LIST = new LinkedList<ATOOre>();
	static LinkedList<ATOOre> DENSENETHERORES_LIST = new LinkedList<ATOOre>();
	static LinkedList<ATOOre> POORENDORES_LIST = new LinkedList<ATOOre>();
	static LinkedList<ATOOre> ENDORES_LIST = new LinkedList<ATOOre>();
	static LinkedList<ATOOre> DENSEENDORES_LIST = new LinkedList<ATOOre>();
	static ATODust ATODust = null;
	static ATODustTiny ATODustTiny = null;
	static ATONugget ATONugget = null;
	static ATOIngot ATOIngot = null;
	static ATOCrushed ATOCrushed = null;
	static ATOCrushedPurified ATOCrushedPurified = null;
	static ATOBlock ATOBlock = null;
	static ATOBrick ATOBrick = null;

	public static void preInit(Path configDir) {
		Configuration config = new Configuration(Paths.get(configDir.toString() + "/core.cfg").toFile());

		// Global Enable/Disable
		Reference.CONFIG_ADD_SMELTING = config.getBoolean("add_smelting", Configuration.CATEGORY_GENERAL, true, "Enable/Disable recipes for smelting");
		Reference.CONFIG_ADD_CRAFTING_BLOCKS = config.getBoolean("add_crafting_blocks", Configuration.CATEGORY_GENERAL, true, "Enable/Disable recipes for crafting blocks");
		Reference.CONFIG_ADD_CRAFTING_BRICKS = config.getBoolean("add_crafting_bricks", Configuration.CATEGORY_GENERAL, true, "Enable/Disable recipes for crafting bricks");
		Reference.CONFIG_ADD_CRAFTING_NUGGETS = config.getBoolean("add_crafting_nuggets", Configuration.CATEGORY_GENERAL, true, "Enable/Disable recipes for crafting nuggets");
		Reference.CONFIG_ADD_CRAFTING_INGOTS = config.getBoolean("add_crafting_ingots", Configuration.CATEGORY_GENERAL, true, "Enable/Disable recipes for crafting ingots");
		Reference.CONFIG_ADD_CRAFTING_WEAPONS = config.getBoolean("add_crafting_weapons", Configuration.CATEGORY_GENERAL, true, "Enable/Disable recipes for crafting weapons");
		Reference.CONFIG_ADD_CRAFTING_TOOLS = config.getBoolean("add_crafting_tools", Configuration.CATEGORY_GENERAL, true, "Enable/Disable recipes for crafting tools");
		Reference.CONFIG_ADD_CRAFTING_ARMORS = config.getBoolean("add_crafting_armors", Configuration.CATEGORY_GENERAL, true, "Enable/Disable recipes for crafting armor");
		Reference.CONFIG_GENERATE_ORES_CONFIG = config.getBoolean("generate_starter_ores", Configuration.CATEGORY_GENERAL, false, "Enable/Disable generating All.cfg of ores for starting point");
		Reference.CONFIG_GENERATE_NETHER_ORES = config.getBoolean("generate_nether_ores", Configuration.CATEGORY_GENERAL, true, "Enable/Disable generating nether ores");
		Reference.CONFIG_GENERATE_DENSENETHER_ORES = config.getBoolean("generate_dense_nether_ores", Configuration.CATEGORY_GENERAL, true, "Enable/Disable generating dense nether ores");
		Reference.CONFIG_GENERATE_POORNETHER_ORES = config.getBoolean("generate_poor_nether_ores", Configuration.CATEGORY_GENERAL, true, "Enable/Disable generating poor nether ores");
		Reference.CONFIG_GENERATE_END_ORES = config.getBoolean("generate_end_ores", Configuration.CATEGORY_GENERAL, true, "Enable/Disable generating end ores");
		Reference.CONFIG_GENERATE_DENSEEND_ORES = config.getBoolean("generate_dense_end_ores", Configuration.CATEGORY_GENERAL, true, "Enable/Disable generating dense end ores");
		Reference.CONFIG_GENERATE_POOREND_ORES = config.getBoolean("generate_poor_end_ores", Configuration.CATEGORY_GENERAL, true, "Enable/Disable generating poor end ores");
		Reference.CONFIG_GENERATE_POOR_ORES = config.getBoolean("generate_poor_ores", Configuration.CATEGORY_GENERAL, true, "Enable/Disable generating poor ores from base ores");
		Reference.CONFIG_GENERATE_DENSE_ORES = config.getBoolean("generate_dense_ores", Configuration.CATEGORY_GENERAL, true, "Enable/Disable generating dense ores from base ores");
		Reference.CONFIG_GENERATE_SAND_ORES = config.getBoolean("generate_sand_ores", Configuration.CATEGORY_GENERAL, true, "Enable/Disable generating sand ores from base ores");
		Reference.CONFIG_GENERATE_DENSESAND_ORES = config.getBoolean("generate_dense_sand_ores", Configuration.CATEGORY_GENERAL, true, "Enable/Disable generating dense sand ores");
		Reference.CONFIG_GENERATE_POORSAND_ORES = config.getBoolean("generate_poor_sand_ores", Configuration.CATEGORY_GENERAL, true, "Enable/Disable generating poor sand ores");
		Reference.CONFIG_GENERATE_GRAVEL_ORES = config.getBoolean("generate_gravel_ores", Configuration.CATEGORY_GENERAL, true, "Enable/Disable generating gravel ores from base ores");
		Reference.CONFIG_GENERATE_DENSEGRAVEL_ORES = config.getBoolean("generate_dense_gravel_ores", Configuration.CATEGORY_GENERAL, true, "Enable/Disable generating dense gravel ores");
		Reference.CONFIG_GENERATE_POORGRAVEL_ORES = config.getBoolean("generate_poor_gravel_ores", Configuration.CATEGORY_GENERAL, true, "Enable/Disable generating poor gravel ores");

		if (Reference.CONFIG_GENERATE_ORES_CONFIG) {
			createDefaults(configDir);
			// config = new Configuration(Paths.get(configDir +
			// "/core.cfg").toFile());
			LogHelper.mod_debug("Test = " + config.getCategory(Configuration.CATEGORY_GENERAL).get("generate_starter_ores").getBoolean());
			Property key = config.getCategory(Configuration.CATEGORY_GENERAL).get("generate_starter_ores");
			key.setValue(false);
			key.comment = "Enable/Disable generating All.cfg of ores for starting point";
			LogHelper.mod_debug("Test2 = " + config.getCategory(Configuration.CATEGORY_GENERAL).get("generate_starter_ores").getBoolean());
		}

		if (config.hasChanged()) {
			config.save();
		}

		// // FORTESTING: Preferred mod order
		Reference.PreferredOrder.add("IC2");
		Reference.PreferredOrder.add("Metallurgy");
		Reference.PreferredOrder.add("ThermalFoundation");

		// Make Ores folder if it does not exist
		File dir = new File(configDir + "/Ores");
		dir.mkdirs();
		// read all matching categories and add corresponding blocks and items
		ArrayList<String> oreNames = new ArrayList<String>();
		for (final File fileEntry : dir.listFiles()) {
			if (fileEntry.isFile() && fileEntry.toPath().toString().endsWith(".cfg")) {
				config = new Configuration(fileEntry);
				for (String category : config.getCategoryNames()) {
					String oreName = category.trim();
					if (oreName != null && (!oreName.isEmpty()) && (!oreNames.contains(oreName))) {
						LogHelper.mod_debug("***" + oreName + "***");
						oreNames.add(oreName);
						String underlyingBlockName = config.get(category, "underlyingBlock", "minecraft:stone").getString();
						Block underlyingBlock = Block.getBlockFromName(underlyingBlockName);
						if (underlyingBlock != null) {
							ATODust = null;
							ATODustTiny = null;
							ATONugget = null;
							ATOIngot = null;
							ATOCrushed = null;
							ATOCrushedPurified = null;
							ATOBlock = null;
							ATOBrick = null;

							// Enabling/Disabling section
							boolean toolsEnable = false;
							boolean weaponsEnable = false;
							boolean armorEnable = false;
							boolean blockEnable = true;
							boolean brickEnable = true;
							boolean ingotEnable = true;
							boolean nuggetEnable = true;
							boolean dustEnable = true;
							boolean dustTinyEnable = true;
							boolean crushedEnable = true;
							boolean crushingEnable = true;
							boolean smeltEnable = true;
							boolean maceratingEnable = true;
							if (config.hasKey(category, "toolsEnable")) {
								toolsEnable = config.get(category, "toolsEnable", false).getBoolean();
							}
							if (config.hasKey(category, "weaponsEnable")) {
								weaponsEnable = config.get(category, "weaponsEnable", false).getBoolean();
							}
							if (config.hasKey(category, "armorEnable")) {
								armorEnable = config.get(category, "armorEnable", false).getBoolean();
							}
							if (config.hasKey(category, "blockEnable")) {
								blockEnable = config.get(category, "blockEnable", true).getBoolean();
							}
							if (config.hasKey(category, "brickEnable")) {
								blockEnable = config.get(category, "brickEnable", true).getBoolean();
							}
							if (config.hasKey(category, "ingotEnable")) {
								ingotEnable = config.get(category, "ingotEnable", false).getBoolean();
							}
							if (config.hasKey(category, "nuggetEnable")) {
								nuggetEnable = config.get(category, "nuggetEnable", false).getBoolean();
							}
							if (config.hasKey(category, "dustEnable")) {
								dustEnable = config.get(category, "dustEnable", false).getBoolean();
							}
							if (config.hasKey(category, "dustTinyEnable")) {
								dustTinyEnable = config.get(category, "dustTinyEnable", false).getBoolean();
							}
							if (config.hasKey(category, "crushedEnable")) {
								crushedEnable = config.get(category, "crushedEnable", false).getBoolean();
							}
							if (config.hasKey(category, "smeltEnable")) {
								smeltEnable = config.get(category, "smeltEnable", false).getBoolean();
							}
							if (config.hasKey(category, "crushingEnable")) {
								crushingEnable = config.get(category, "crushingEnable", false).getBoolean();
							}
							if (config.hasKey(category, "maceratingEnable")) {
								maceratingEnable = config.get(category, "maceratingEnable", false).getBoolean();
							}

							// Enable/Disable forcing items to be created
							boolean toolsForce = false;
							boolean weaponsForce = false;
							boolean armorForce = false;
							boolean blockForce = false;
							boolean brickForce = false;
							boolean ingotForce = false;
							boolean nuggetForce = false;
							boolean dustForce = false;
							boolean dustTinyForce = false;
							boolean crushedForce = false;
							boolean crushedPurifiedForce = false;
							boolean smeltForce = false;
							if (config.hasKey(category, "toolsForce")) {
								toolsForce = config.get(category, "toolsForce", false).getBoolean();
							}
							if (config.hasKey(category, "weaponsForce")) {
								weaponsForce = config.get(category, "weaponsForce", false).getBoolean();
							}
							if (config.hasKey(category, "armorForce")) {
								armorForce = config.get(category, "armorForce", false).getBoolean();
							}
							if (config.hasKey(category, "blockForce")) {
								blockForce = config.get(category, "blockForce", false).getBoolean();
							}
							if (config.hasKey(category, "brickForce")) {
								blockForce = config.get(category, "brickForce", false).getBoolean();
							}
							if (config.hasKey(category, "ingotForce")) {
								ingotForce = config.get(category, "ingotForce", false).getBoolean();
							}
							if (config.hasKey(category, "nuggetForce")) {
								nuggetForce = config.get(category, "nuggetForce", false).getBoolean();
							}
							if (config.hasKey(category, "dustForce")) {
								dustForce = config.get(category, "dustForce", false).getBoolean();
							}
							if (config.hasKey(category, "dustTinyForce")) {
								dustTinyForce = config.get(category, "dustTinyForce", false).getBoolean();
							}
							if (config.hasKey(category, "crushedForce")) {
								crushedForce = config.get(category, "crushedForce", false).getBoolean();
							}
							if (config.hasKey(category, "crushedPurifiedForce")) {
								crushedPurifiedForce = config.get(category, "crushedPurifiedForce", false).getBoolean();
							}
							if (config.hasKey(category, "smeltForce")) {
								smeltForce = config.get(category, "smeltForce", false).getBoolean();
							}

							// Ore setup section
							int oreDropMin = 1;
							int oreDropMax = 1;
							// int oreHardness = 3;
							int oreHarvestLevel = 0;
							int oreDropType = 0;
							int oreRenderType = 0;
							int oreDenseRenderType = -1;
							int orePoorRenderType = -1;
							String oreType = "";
							boolean outputDust = false;
							Boolean oreFalls = false;
							Boolean oreSilkHarvest = true;
							int oreColor = Utils.intFromHex(config.get(category, "oreColor", "ffffff").getString());

							if (config.hasKey(category, "oreDropMin")) {
								oreDropMin = config.get(category, "oreDropMin", 1).getInt();
							}
							if (config.hasKey(category, "oreDropMax")) {
								oreDropMax = config.get(category, "oreDropMax", 1).getInt();
							}
							/*
							 * if (config.hasKey(category, "oreHardness")) {
							 * oreHardness = config.get(category, "oreHardness",
							 * 3).getInt(); }
							 */
							if (config.hasKey(category, "oreHarvestLevel"))
								oreHarvestLevel = config.get(category, "oreHarvestLevel", 0).getInt();
							if (config.hasKey(category, "oreDropType"))
								oreDropType = config.get(category, "oreDropType", 0).getInt();
							if (config.hasKey(category, "oreRenderType"))
								oreRenderType = config.get(category, "oreRenderType", 0).getInt();
							if (config.hasKey(category, "oreDenseRenderType"))
								oreDenseRenderType = config.get(category, "oreDenseRenderType", -1).getInt();
							if (config.hasKey(category, "orePoorRenderType"))
								orePoorRenderType = config.get(category, "orePoorRenderType", -1).getInt();
							if (config.hasKey(category, "oreType"))
								oreType = config.get(category, "oreType", "").getString();
							if (config.hasKey(category, "oreFalls"))
								oreFalls = config.get(category, "oreFalls", false).getBoolean();
							if (config.hasKey(category, "oreSilkHarvest"))
								oreSilkHarvest = config.get(category, "oreSilkHarvest", true).getBoolean();
							if (config.hasKey(category, "outputDust"))
								outputDust = config.get(category, "outputDust", false).getBoolean();

							// Enable/Disable Ores
							Boolean oreDenseNetherEnable = true;
							Boolean oreDenseEndEnable = true;
							Boolean oreDenseSandEnable = true;
							Boolean oreDenseGravelEnable = true;
							Boolean oreDenseEnable = true;
							Boolean orePoorNetherEnable = true;
							Boolean orePoorEndEnable = true;
							Boolean orePoorSandEnable = true;
							Boolean orePoorGravelEnable = true;
							Boolean orePoorEnable = true;
							Boolean oreNetherEnable = true;
							Boolean oreEndEnable = true;
							Boolean oreSandEnable = true;
							Boolean oreGravelEnable = true;
							Boolean oreBaseEnable = true;
							if (config.hasKey(category, "oreDenseNetherEnable"))
								oreDenseNetherEnable = config.get(category, "oreDenseNetherEnable", true).getBoolean();
							if (config.hasKey(category, "oreDenseEndEnable"))
								oreDenseEndEnable = config.get(category, "oreDenseEndEnable", true).getBoolean();
							if (config.hasKey(category, "oreDenseSandEnable"))
								oreDenseSandEnable = config.get(category, "oreDenseSandEnable", true).getBoolean();
							if (config.hasKey(category, "oreDenseGravelEnable"))
								oreDenseGravelEnable = config.get(category, "oreDenseGravelEnable", true).getBoolean();
							if (config.hasKey(category, "oreDenseEnable"))
								oreDenseEnable = config.get(category, "oreDenseEnable", true).getBoolean();
							if (config.hasKey(category, "orePoorNetherEnable"))
								orePoorNetherEnable = config.get(category, "orePoorNetherEnable", true).getBoolean();
							if (config.hasKey(category, "orePoorEndEnable"))
								orePoorEndEnable = config.get(category, "orePoorEndEnable", true).getBoolean();
							if (config.hasKey(category, "orePoorSandEnable"))
								orePoorSandEnable = config.get(category, "orePoorSandEnable", true).getBoolean();
							if (config.hasKey(category, "orePoorGravelEnable"))
								orePoorGravelEnable = config.get(category, "orePoorGravelEnable", true).getBoolean();
							if (config.hasKey(category, "orePoorEnable"))
								orePoorEnable = config.get(category, "orePoorEnable", true).getBoolean();
							if (config.hasKey(category, "oreNetherEnable"))
								oreNetherEnable = config.get(category, "oreNetherEnable", true).getBoolean();
							if (config.hasKey(category, "oreEndEnable"))
								oreEndEnable = config.get(category, "oreEndEnable", true).getBoolean();
							if (config.hasKey(category, "oreSandEnable"))
								oreSandEnable = config.get(category, "oreSandEnable", true).getBoolean();
							if (config.hasKey(category, "oreGravelEnable"))
								oreGravelEnable = config.get(category, "oreGravelEnable", true).getBoolean();
							if (config.hasKey(category, "oreBaseEnable"))
								oreBaseEnable = config.get(category, "oreBaseEnable", true).getBoolean();

							// Enable/Disable Recipes
							boolean blockRecipeEnable = true;
							boolean brickRecipeEnable = true;
							boolean ingotRecipeEnable = true;
							boolean nuggetRecipeEnable = true;
							boolean dustRecipeEnable = true;
							boolean dustTinyRecipeEnable = true;
							boolean weaponRecipeEnable = true;
							boolean toolsRecipeEnable = true;
							boolean armorRecipeEnable = true;
							if (config.hasKey(category, "blockRecipeEnable"))
								blockRecipeEnable = config.get(category, "blockRecipeEnable", true).getBoolean();
							if (config.hasKey(category, "brickRecipeEnable"))
								brickRecipeEnable = config.get(category, "brickRecipeEnable", true).getBoolean();
							if (config.hasKey(category, "ingotRecipeEnable"))
								ingotRecipeEnable = config.get(category, "ingotRecipeEnable", true).getBoolean();
							if (config.hasKey(category, "nuggetRecipeEnable"))
								nuggetRecipeEnable = config.get(category, "nuggetRecipeEnable", true).getBoolean();
							if (config.hasKey(category, "dustRecipeEnable"))
								dustRecipeEnable = config.get(category, "dustRecipeEnable", true).getBoolean();
							if (config.hasKey(category, "dustTinyRecipeEnable"))
								dustTinyRecipeEnable = config.get(category, "dustTinyRecipeEnable", true).getBoolean();
							if (config.hasKey(category, "weaponRecipeEnable"))
								weaponRecipeEnable = config.get(category, "weaponRecipeEnable", true).getBoolean();
							if (config.hasKey(category, "toolsRecipeEnable"))
								toolsRecipeEnable = config.get(category, "toolsRecipeEnable", true).getBoolean();
							if (config.hasKey(category, "armorRecipeEnable"))
								armorRecipeEnable = config.get(category, "armorRecipeEnable", true).getBoolean();

							// World gen
							int veinRate = 0;
							int veinSize = 0;
							int veinHeight = 0;
							if (config.hasKey(category, "veinRate")) {
								veinRate = config.get(category, "veinRate", 0).getInt();
							}
							if (config.hasKey(category, "veinSize")) {
								veinSize = config.get(category, "veinSize", 0).getInt();
							}
							if (config.hasKey(category, "veinHeight")) {
								veinHeight = config.get(category, "veinHeight", 0).getInt();
							}
							String dimWhiteListStr = "";
							LinkedList<Integer> dimWhiteList = new LinkedList<Integer>();
							if (config.hasKey(category, "dimWhiteList")) {
								dimWhiteListStr = config.get(category, "dimWhiteList", "").getString();
							}
							if (dimWhiteListStr.length() != 0) {
								String[] dimWhiteListStrArray = dimWhiteListStr.split(",");
								for (String entry : dimWhiteListStrArray) {
									try {
										dimWhiteList.add(Integer.parseInt(entry));
									} catch (NumberFormatException e) {
										LogHelper.warn(oreName + ": Could not parse " + entry + " to an integer.");
										LogHelper.warn(oreName + ": Entry will be ignored.");
									}
								}
							}
							String dimBlackListStr = "-1,1";
							List<Integer> dimBlackList = new LinkedList<Integer>();
							if (config.hasKey(category, "dimBlackList")) {
								dimBlackListStr = config.get(category, "dimBlackList", "-1,1").getString();
							}
							if (dimBlackListStr.length() != 0) {
								String[] dimBlackListStrArray = dimBlackListStr.split(",");
								for (String entry : dimBlackListStrArray) {
									try {
										dimBlackList.add(Integer.parseInt(entry));
									} catch (NumberFormatException e) {
										LogHelper.warn(oreName + ": Could not parse " + entry + " to an integer.");
										LogHelper.warn(oreName + ": Entry will be ignored.");
									}
								}
							}

							// Ore material setup section
							int pickHarvestLevel = 1;
							int durability = 10;
							int miningSpeed = 5;
							int damage = 1;
							int enchantability = 10;
							int damageReductionTotal = 10;
							if (config.hasKey(category, "pickHarvestLevel")) {
								pickHarvestLevel = Math.min(config.get(category, "pickHarvestLevel", 1).getInt(), 10);
							}
							if (config.hasKey(category, "durability")) {
								durability = Math.min(config.get(category, "durability", 10).getInt(), 50);
							}
							if (config.hasKey(category, "miningSpeed")) {
								miningSpeed = Math.min(config.get(category, "miningSpeed", 5).getInt(), 15);
							}
							if (config.hasKey(category, "damage")) {
								damage = Math.min(config.get(category, "damage", 1).getInt(), 4);
							}
							if (config.hasKey(category, "enchantability")) {
								enchantability = Math.min(config.get(category, "enchantability", 10).getInt(), 30);
							}
							if (config.hasKey(category, "damageReductionTotal")) {
								damageReductionTotal = Math.min(config.get(category, "damageReductionTotal", 10).getInt(), 20);
							}

							// Section: Smelt setup
							String smeltName = "";
							int smeltMeta = -1;
							int smeltAmount = Math.max(1, oreDropMax);
							if (config.hasKey(category, "smelt")) {
								smeltName = config.get(category, "smelt", "").getString();
							}
							if (config.hasKey(category, "smeltMeta")) {
								smeltMeta = config.get(category, "smeltMeta", -1).getInt();
							}
							if (config.hasKey(category, "smeltAmount")) {
								smeltAmount = config.get(category, "smeltAmount", smeltAmount).getInt();
							}

							String extraDustName = "";
							int extraDustMeta = -1;
							String extraDustTinyName = "";
							int extraDustTinyMeta = -1;
							if (config.hasKey(category, "extraDust")) {
								extraDustName = config.get(category, "extraDust", "").getString();
							}
							if (config.hasKey(category, "extraDustMeta")) {
								extraDustMeta = config.get(category, "extraDustMeta", -1).getInt();
							}
							if (config.hasKey(category, "extraDustTiny")) {
								extraDustTinyName = config.get(category, "extraDustTiny", "").getString();
							}
							if (config.hasKey(category, "extraDustTinyMeta")) {
								extraDustTinyMeta = config.get(category, "extraDustTinyMeta", -1).getInt();
							}

							// Section: Register Ore
							ATOOre ore = new ATOOre(oreName, oreColor, oreDropType, oreDropMin, oreDropMax, 2, oreHarvestLevel, oreRenderType, oreType, underlyingBlockName, veinRate, veinSize, veinHeight, dimWhiteList, dimBlackList, smeltEnable, maceratingEnable, crushingEnable, oreFalls, outputDust, true, null);
							ore.oreDenseRenderType = (oreDenseRenderType < 0) ? oreRenderType : oreDenseRenderType;
							ore.orePoorRenderType = (orePoorRenderType < 0) ? oreRenderType : orePoorRenderType;

							// Section: Ingot setup
							String ingotName = "";
							String ingotType = "ingot";
							String ingotOreDictList = "";
							int ingotMeta = -1;
							int ingotRenderType = 0;
							int ingotBurnTime = 0;
							int ingotColor = -1;
							if (config.hasKey(category, "ingot")) {
								ingotName = config.get(category, "ingot", "").getString();
							}
							if (config.hasKey(category, "ingotMeta")) {
								ingotMeta = config.get(category, "ingotMeta", -1).getInt();
							}
							if (config.hasKey(category, "ingotType")) {
								ingotType = config.get(category, "ingotType", "ingot").getString();
							}
							if (config.hasKey(category, "ingotOreDictList")) {
								ingotOreDictList = config.get(category, "ingotOreDictList", "").getString();
							}
							if (config.hasKey(category, "ingotRenderType")) {
								ingotRenderType = config.get(category, "ingotRenderType", 0).getInt();
							}
							if (config.hasKey(category, "ingotBurnTime")) {
								ingotBurnTime = config.get(category, "ingotBurnTime", 0).getInt();
							}
							if (config.hasKey(category, "ingotColor")) {
								ingotColor = Utils.intFromHex(config.get(category, "ingotColor", "000000").getString());
							}
							// Register Ingot
							if (ingotEnable && oreType.equals("")) {
								ingotOreDictList = (ingotOreDictList.equalsIgnoreCase("") ? ingotType : ingotOreDictList);
								List<String> ingotOreDictListArray = new ArrayList<String>(Arrays.asList(ingotOreDictList.split("\\s*,\\s*")));
								ItemStack ingot = Utils.getItemStack(ingotName, ingotMeta);
								if (!ingotForce && ingot != null) {
									ore.setIngot(ingot);
								} else {
									ATOIngot = new ATOIngot(oreName, ore, (ingotColor < 0) ? oreColor : ingotColor, ingotType, ingotOreDictListArray, ingotRenderType, ingotBurnTime);
									ore.setIngot(new ItemStack(ATOIngot).copy());
								}
							}

							// Section: Nugget setup
							String nuggetName = "";
							String nuggetType = "nugget";
							String nuggetOreDictList = "";
							int nuggetMeta = -1;
							int nuggetRenderType = 0;
							int nuggetBurnTime = 0;
							int nuggetColor = -1;
							if (config.hasKey(category, "nugget")) {
								nuggetName = config.get(category, "nugget", "").getString();
							}
							if (config.hasKey(category, "nuggetMeta")) {
								nuggetMeta = config.get(category, "nuggetMeta", -1).getInt();
							}
							if (config.hasKey(category, "nuggetType")) {
								nuggetType = config.get(category, "nuggetType", "nugget").getString();
							}
							if (config.hasKey(category, "nuggetOreDictList")) {
								nuggetOreDictList = config.get(category, "nuggetOreDictList", "").getString();
							}
							if (config.hasKey(category, "nuggetRenderType")) {
								nuggetRenderType = config.get(category, "nuggetRenderType", 0).getInt();
							}
							if (config.hasKey(category, "nuggetBurnTime")) {
								nuggetBurnTime = config.get(category, "nuggetBurnTime", 0).getInt();
							}
							if (config.hasKey(category, "nuggetColor")) {
								nuggetColor = Utils.intFromHex(config.get(category, "nuggetColor", "000000").getString());
							}
							// Register Nugget
							if (nuggetEnable && oreType.equals("")) {
								nuggetOreDictList = (nuggetOreDictList.equalsIgnoreCase("") ? nuggetType : nuggetOreDictList);
								List<String> nuggetOreDictListArray = new ArrayList<String>(Arrays.asList(nuggetOreDictList.split("\\s*,\\s*")));
								ItemStack nugget = Utils.getItemStack(nuggetName, nuggetMeta);
								if (!nuggetForce && nugget != null)
									ore.setNugget(nugget);
								else {
									ATONugget = new ATONugget(oreName, ore, (nuggetColor < 0) ? oreColor : nuggetColor, nuggetType, nuggetOreDictListArray, nuggetRenderType, nuggetBurnTime);
									ore.setNugget(new ItemStack(ATONugget));
								}
							}

							// Section: Dust setup
							int dustRenderType = 0;
							int dustBurnTime = 0;
							int dustColor = -1;
							String dustName = "";
							String dustType = "dust";
							String dustOreDictList = "";
							int dustMeta = -1;
							int dustAmount = Math.max(2, oreDropMax * 2);
							if (config.hasKey(category, "dust")) {
								dustName = config.get(category, "dust", "").getString();
							}
							if (config.hasKey(category, "dustMeta")) {
								dustMeta = config.get(category, "dustMeta", -1).getInt();
							}
							if (config.hasKey(category, "dustType")) {
								dustType = config.get(category, "dustType", "dust").getString();
							}
							if (config.hasKey(category, "dustOreDictList")) {
								dustOreDictList = config.get(category, "dustOreDictList", "").getString();
							}
							if (config.hasKey(category, "dustAmount")) {
								dustAmount = config.get(category, "dustAmount", dustAmount).getInt();
							}
							if (config.hasKey(category, "dustRenderType")) {
								dustRenderType = config.get(category, "dustRenderType", 0).getInt();
							}
							if (config.hasKey(category, "dustBurnTime")) {
								dustBurnTime = config.get(category, "dustBurnTime", 0).getInt();
							}
							if (config.hasKey(category, "dustColor")) {
								dustColor = Utils.intFromHex(config.get(category, "dustColor", "000000").getString());
							}
							// Register Dust
							if (dustEnable && oreType.equals("")) {
								dustOreDictList = (dustOreDictList.equalsIgnoreCase("") ? dustType : dustOreDictList);
								List<String> dustOreDictListArray = new ArrayList<String>(Arrays.asList(dustOreDictList.split("\\s*,\\s*")));
								ItemStack dust = Utils.getItemStack(dustName, dustMeta);
								if (!dustForce && dust != null)
									ore.setDust(dust);
								else {
									ATODust = new ATODust(oreName, ore, (dustColor < 0) ? oreColor : dustColor, dustType, dustOreDictListArray, dustRenderType, dustBurnTime);
									ore.setDust(new ItemStack(ATODust));
								}
							}

							// Section: Dust Tiny setup
							int dustTinyRenderType = 0;
							int dustTinyBurnTime = 0;
							int dustTinyColor = -1;
							String dustTinyName = "";
							String dustTinyType = "dustTiny";
							String dustTinyOreDictList = "";
							int dustTinyMeta = -1;

							if (config.hasKey(category, "dustTiny")) {
								dustTinyName = config.get(category, "dustTiny", "").getString();
							}
							if (config.hasKey(category, "dustTinyMeta")) {
								dustTinyMeta = config.get(category, "dustTinyMeta", -1).getInt();
							}
							if (config.hasKey(category, "dustTinyType")) {
								dustType = config.get(category, "dustTinyType", "dustTiny").getString();
							}
							if (config.hasKey(category, "dustTinyOreDictList")) {
								dustTinyOreDictList = config.get(category, "dustTinyOreDictList", "").getString();
							}
							if (config.hasKey(category, "dustTinyRenderType")) {
								dustTinyRenderType = config.get(category, "dustTinyRenderType", 0).getInt();
							}
							if (config.hasKey(category, "dustTinyBurnTime")) {
								dustTinyBurnTime = config.get(category, "dustTinyBurnTime", 0).getInt();
							}
							if (config.hasKey(category, "dustTinyColor")) {
								dustTinyColor = Utils.intFromHex(config.get(category, "dustTinyColor", "000000").getString());
							}
							// Register Tiny Dust
							if (dustTinyEnable && oreType.equals("")) {
								dustTinyOreDictList = (dustTinyOreDictList.isEmpty() ? dustTinyType : dustTinyOreDictList);
								List<String> dustTinyOreDictListArray = new ArrayList<String>(Arrays.asList(dustTinyOreDictList.split("\\s*,\\s*")));
								ItemStack dustTiny = Utils.getItemStack(dustTinyName, dustTinyMeta);
								if (!dustTinyForce && dustTiny != null)
									ore.setDustTiny(dustTiny);
								else {
									ATODustTiny = new ATODustTiny(oreName, ore, (dustTinyColor < 0) ? oreColor : dustTinyColor, dustTinyType, dustTinyOreDictListArray, dustTinyRenderType, dustTinyBurnTime);
									ore.setDustTiny(new ItemStack(ATODustTiny));
								}
							}

							// Section: Crushed Ore setup
							int crushedRenderType = 0;
							int crushedBurnTime = 0;
							int crushedColor = -1;
							String crushedName = "";
							String crushedType = "crushed";
							String crushedOreDictList = "";
							int crushedMeta = -1;
							int crushedAmount = Math.max(2, oreDropMax * 2);
							if (config.hasKey(category, "crushed")) {
								crushedName = config.get(category, "crushed", "").getString();
							}
							if (config.hasKey(category, "crushedMeta")) {
								crushedMeta = config.get(category, "crushedMeta", -1).getInt();
							}
							if (config.hasKey(category, "crushedAmount")) {
								crushedAmount = config.get(category, "crushedAmount", crushedAmount).getInt();
							}
							if (config.hasKey(category, "crushedType")) {
								crushedType = config.get(category, "crushedType", "crushed").getString();
							}
							if (config.hasKey(category, "crushedOreDictList")) {
								crushedOreDictList = config.get(category, "crushedOreDictList", "").getString();
							}
							if (config.hasKey(category, "crushedRenderType")) {
								crushedRenderType = config.get(category, "crushedRenderType", 0).getInt();
							}
							if (config.hasKey(category, "crushedBurnTime")) {
								crushedBurnTime = config.get(category, "crushedBurnTime", 0).getInt();
							}
							if (config.hasKey(category, "crushedColor")) {
								crushedColor = Utils.intFromHex(config.get(category, "crushedColor", "000000").getString());
							}
							// Section: Purified Crushed Ore setup
							int crushedPurifiedRenderType = 0;
							int crushedPurifiedBurnTime = 0;
							int crushedPurifiedColor = -1;
							String crushedPurifiedName = "";
							String crushedPurifiedType = "crushedPurified";
							String crushedPurifiedOreDictList = "";
							int crushedPurifiedMeta = -1;
							if (config.hasKey(category, "crushedPurified")) {
								crushedPurifiedName = config.get(category, "crushedPurified", "").getString();
							}
							if (config.hasKey(category, "crushedPurifiedMeta")) {
								crushedPurifiedMeta = config.get(category, "crushedPurifiedMeta", -1).getInt();
							}
							if (config.hasKey(category, "crushedPurifiedType")) {
								crushedPurifiedType = config.get(category, "crushedPurifiedType", "crushedPurified").getString();
							}
							if (config.hasKey(category, "crushedPurifiedOreDictList")) {
								crushedPurifiedOreDictList = config.get(category, "crushedPurifiedOreDictList", "").getString();
							}
							if (config.hasKey(category, "crushedPurifiedRenderType")) {
								crushedPurifiedRenderType = config.get(category, "crushedPurifiedRenderType", 0).getInt();
							}
							if (config.hasKey(category, "crushedPurifiedBurnTime")) {
								crushedPurifiedBurnTime = config.get(category, "crushedPurifiedBurnTime", 0).getInt();
							}
							if (config.hasKey(category, "crushedPurifiedColor")) {
								crushedPurifiedColor = Utils.intFromHex(config.get(category, "crushedPurifiedColor", "000000").getString());
							}
							// Register Crushed Ore
							if (Loader.isModLoaded("IC2") && oreType.equals("")) {
								if (crushedEnable) {
									Boolean oreFound = false;
									for (ItemStack oreDictOre : OreDictionary.getOres(ore.oreDictName)) {
										if (Recipes.macerator.getOutputFor(oreDictOre, true) == null) {
											// oreFound = oreDictOre;
											break;
										}
									}
									if (Recipes.macerator.getOutputFor(new ItemStack(ore, 1), true) == null) {
										ItemStack crushed = Utils.getItemStack(crushedName, crushedMeta);
										if (!crushedForce && crushed != null)
											ore.setCrushed(crushed);
										else {
											crushedOreDictList = (crushedOreDictList.isEmpty() ? crushedType : crushedOreDictList);
											List<String> crushedOreDictListArray = new ArrayList<String>(Arrays.asList(crushedOreDictList.split("\\s*,\\s*")));
											ATOCrushed = new ATOCrushed(oreName, ore, (crushedColor < 0) ? oreColor : crushedColor, crushedType, crushedOreDictListArray, crushedRenderType, crushedBurnTime);
											ore.setCrushed(new ItemStack(ATOCrushed));
										}
									}
									// Register Crushed Purified Ore
									if (Recipes.oreWashing.getOutputFor(ore.getCrushed(1), true) == null) {
										ItemStack crushedPurified = Utils.getItemStack(crushedPurifiedName, crushedPurifiedMeta);
										if (!crushedPurifiedForce && crushedPurified != null)
											ore.setCrushedPurified(crushedPurified);
										else {
											crushedPurifiedOreDictList = (crushedPurifiedOreDictList.isEmpty() ? crushedPurifiedType : crushedPurifiedOreDictList);
											List<String> crushedPurifiedOreDictListArray = new ArrayList<String>(Arrays.asList(crushedPurifiedOreDictList.split("\\s*,\\s*")));
											ATOCrushedPurified = new ATOCrushedPurified(oreName, ore, (crushedPurifiedColor < 0) ? oreColor : crushedPurifiedColor, crushedPurifiedType, crushedPurifiedOreDictListArray, crushedPurifiedRenderType, crushedPurifiedBurnTime);
											ore.setCrushedPurified(new ItemStack(ATOCrushedPurified));
										}
									}
								}
							}

							// Section: Block setup
							String blockName = "";
							int blockRenderType = 0;
							int blockBurnTime = 0;
							int blockColor = -1;
							int blockMeta = -1;
							if (config.hasKey(category, "block")) {
								blockName = config.get(category, "block", "").getString();
							}
							if (config.hasKey(category, "blockMeta")) {
								blockMeta = config.get(category, "blockMeta", -1).getInt();
							}
							if (config.hasKey(category, "blockRenderType")) {
								blockRenderType = config.get(category, "blockRenderType", 0).getInt();
							}
							if (config.hasKey(category, "blockBurnTime")) {
								blockBurnTime = config.get(category, "blockBurnTime", 0).getInt();
							}
							if (config.hasKey(category, "blockColor")) {
								blockColor = Utils.intFromHex(config.get(category, "blockColor", "000000").getString());
							}
							// Register Block
							if (blockEnable && (oreType.equals("") || oreType.equals("ingot") || oreType.equals("gem"))) {
								ItemStack block = Utils.getItemStack(blockName, blockMeta);
								if (!blockForce && block != null)
									ore.setBlock(block);
								else {
									ATOBlock = new ATOBlock(oreName, ore, (blockColor < 0) ? ((ingotColor < 0) ? oreColor : ingotColor) : blockColor, blockRenderType, blockBurnTime);
									ore.setBlock(new ItemStack(ATOBlock));
								}
							}

							// Section: Brick setup
							String brickName = "";
							int brickRenderType = 0;
							int brickBurnTime = 0;
							int brickColor = -1;
							int brickMeta = -1;
							if (config.hasKey(category, "brick")) {
								brickName = config.get(category, "brick", "").getString();
							}
							if (config.hasKey(category, "brickMeta")) {
								brickMeta = config.get(category, "brickMeta", -1).getInt();
							}
							if (config.hasKey(category, "brickRenderType")) {
								brickRenderType = config.get(category, "brickRenderType", 0).getInt();
							}
							if (config.hasKey(category, "brickBurnTime")) {
								brickBurnTime = config.get(category, "brickBurnTime", 0).getInt();
							}
							if (config.hasKey(category, "brickColor")) {
								brickColor = Utils.intFromHex(config.get(category, "brickColor", "000000").getString());
							}
							// Register Brick
							if (brickEnable && (oreType.equals("") || oreType.equals("ingot") || oreType.equals("gem"))) {
								ItemStack brick = Utils.getItemStack(brickName, brickMeta);
								if (!brickForce && brick != null)
									ore.setBlock(brick);
								else {
									ATOBrick = new ATOBrick(oreName, ore, (blockColor < 0) ? ((ingotColor < 0) ? oreColor : ingotColor) : brickColor, brickRenderType, brickBurnTime);
									ore.setBrick(new ItemStack(ATOBrick));
								}
							}

							// Section: Weapons setup
							ToolMaterial material = EnumHelper.addToolMaterial(oreName, pickHarvestLevel, durability, miningSpeed, damage, enchantability);
							if ((weaponsEnable && (oreType.equals(""))) || weaponsForce) {
								Sword sword = new Sword(oreName, material, ore, (ingotColor < 0) ? oreColor : ingotColor, 0);
								ore.sword = sword;
							}

							// Section: Tools setup
							if ((toolsEnable && (oreType.equals(""))) || toolsForce) {
								Spade spade = new Spade(oreName, material, ore, (ingotColor < 0) ? oreColor : ingotColor, 0);
								ore.spade = spade;
								Hoe hoe = new Hoe(oreName, material, ore, (ingotColor < 0) ? oreColor : ingotColor, 0);
								ore.hoe = hoe;
								Axe axe = new Axe(oreName, material, ore, (ingotColor < 0) ? oreColor : ingotColor, 0);
								ore.axe = axe;
								Pickaxe pickaxe = new Pickaxe(oreName, material, ore, (ingotColor < 0) ? oreColor : ingotColor, 0);
								ore.pickaxe = pickaxe;
							}

							// Section: Armor setup
							if ((armorEnable && (oreType.equals(""))) || armorForce) {
								int helmet = (int) Math.floor(3 / 20 * damageReductionTotal);
								int chestplate = (int) Math.floor(8 / 20 * damageReductionTotal);
								int legs = (int) Math.floor(6 / 20 * damageReductionTotal);
								int boots = (int) Math.floor(3 / 20 * damageReductionTotal);
								ArmorMaterial armorMaterial = EnumHelper.addArmorMaterial(oreName, durability, new int[] { helmet, chestplate, legs, boots }, enchantability);
								int armorRenderID = RenderingRegistry.getNextAvailableRenderId();
								RenderingRegistry.addNewArmourRendererPrefix(Integer.toString(armorRenderID));
								ore.helmet = new Armor(oreName + "_helmet", armorRenderID, armorMaterial, 0, ore, (ingotColor < 0) ? oreColor : ingotColor, 0);
								ore.chestplate = new Armor(oreName + "_chestplate", armorRenderID, armorMaterial, 1, ore, (ingotColor < 0) ? oreColor : ingotColor, 0);
								ore.legs = new Armor(oreName + "_legs", armorRenderID, armorMaterial, 2, ore, (ingotColor < 0) ? oreColor : ingotColor, 0);
								ore.boots = new Armor(oreName + "_boots", armorRenderID, armorMaterial, 3, ore, (ingotColor < 0) ? oreColor : ingotColor, 0);
							}

							// Section: Recipes Setup
							ore.blockRecipe = blockRecipeEnable;
							ore.brickRecipe = brickRecipeEnable;
							ore.ingotRecipe = ingotRecipeEnable;
							ore.nuggetRecipe = nuggetRecipeEnable;
							ore.dustRecipe = dustRecipeEnable;
							ore.dustTinyRecipe = dustTinyRecipeEnable;
							ore.weaponRecipe = weaponRecipeEnable;
							ore.toolsRecipe = toolsRecipeEnable;
							ore.armorRecipe = armorRecipeEnable;

							// Section: Register Ores
							if (underlyingBlock == Blocks.end_stone) {
								// Register End Ores
								LogHelper.mod_debug("Creating End Ores.");
								createEndOres(ore, oreEndEnable, oreDenseEndEnable, orePoorEndEnable, true);
							} else if (underlyingBlock == Blocks.netherrack) {
								// Register Nether & End Ores
								LogHelper.mod_debug("Creating Nether Ores.");
								createNetherOres(ore, oreNetherEnable, oreDenseNetherEnable, orePoorNetherEnable, true);
								createEndOres(ore, oreEndEnable, oreDenseEndEnable, orePoorEndEnable, false);
							} else if (underlyingBlock == Blocks.stone) {
								// Base Ore
								LogHelper.mod_debug("Creating All Ores.");
								createBaseOres(ore, oreBaseEnable, oreDenseEnable, orePoorEnable);
								createSandOres(ore, oreSandEnable, oreDenseSandEnable, orePoorSandEnable, false);
								createGravelOres(ore, oreGravelEnable, oreDenseGravelEnable, orePoorGravelEnable, false);
								createNetherOres(ore, oreNetherEnable, oreDenseNetherEnable, orePoorNetherEnable, false);
								createEndOres(ore, oreEndEnable, oreDenseEndEnable, orePoorEndEnable, false);
							} else {
								ORES_LIST.add(ore);
							}

							// Resources
							if (ATODust != null && dustEnable)
								Reference.DUST_LIST.add(ATODust);
							if (ATODustTiny != null && dustTinyEnable)
								Reference.DUSTTINY_LIST.add(ATODustTiny);
							if (ATONugget != null && nuggetEnable)
								Reference.NUGGET_LIST.add(ATONugget);
							if (ATOIngot != null && ingotEnable)
								Reference.INGOT_LIST.add(ATOIngot);
							if (ATOCrushed != null && crushedEnable)
								Reference.CRUSHED_LIST.add(ATOCrushed);
							if (ATOCrushedPurified != null && crushedEnable)
								Reference.CRUSHEDPURIFIED_LIST.add(ATOCrushedPurified);
							if (ATOBlock != null && blockEnable)
								Reference.BLOCK_LIST.add(ATOBlock);
							if (ATOBrick != null && brickEnable)
								Reference.BRICK_LIST.add(ATOBrick);
						} else {
							LogHelper.warn(oreName + ": Underlying Block \"" + underlyingBlockName + "\" not found.");
							LogHelper.warn(oreName + ": Ore will not be added.");
						}
					} else {
						LogHelper.warn(oreName + ": Must specify a unique ore name.");
						LogHelper.warn(oreName + ": Ore will not be added.");
					}
				}
			}
			if (config.hasChanged()) {
				config.save();
			}
		}

		// Sort ore list
		ORES_LIST = Utils.sortOresList(ORES_LIST);
		POORORES_LIST = Utils.sortOresList(POORORES_LIST);
		DENSEORES_LIST = Utils.sortOresList(DENSEORES_LIST);
		SANDORES_LIST = Utils.sortOresList(SANDORES_LIST);
		POORSANDORES_LIST = Utils.sortOresList(POORSANDORES_LIST);
		DENSESANDORES_LIST = Utils.sortOresList(DENSESANDORES_LIST);
		GRAVELORES_LIST = Utils.sortOresList(GRAVELORES_LIST);
		POORGRAVELORES_LIST = Utils.sortOresList(POORGRAVELORES_LIST);
		DENSEGRAVELORES_LIST = Utils.sortOresList(DENSEGRAVELORES_LIST);
		NETHERORES_LIST = Utils.sortOresList(NETHERORES_LIST);
		POORNETHERORES_LIST = Utils.sortOresList(POORNETHERORES_LIST);
		DENSENETHERORES_LIST = Utils.sortOresList(DENSENETHERORES_LIST);
		ENDORES_LIST = Utils.sortOresList(ENDORES_LIST);
		POORENDORES_LIST = Utils.sortOresList(POORENDORES_LIST);
		DENSEENDORES_LIST = Utils.sortOresList(DENSEENDORES_LIST);

		// Sort other lists
		Reference.INGOT_LIST = Utils.sortIngotList(Reference.INGOT_LIST);
		Reference.NUGGET_LIST = Utils.sortNuggetList(Reference.NUGGET_LIST);
		Reference.DUST_LIST = Utils.sortDustList(Reference.DUST_LIST);
		Reference.DUSTTINY_LIST = Utils.sortDustTinyList(Reference.DUSTTINY_LIST);
		Reference.CRUSHED_LIST = Utils.sortCrushedList(Reference.CRUSHED_LIST);
		Reference.CRUSHEDPURIFIED_LIST = Utils.sortCrushedPurifiedList(Reference.CRUSHEDPURIFIED_LIST);
		Reference.BLOCK_LIST = Utils.sortBlockList(Reference.BLOCK_LIST);
		Reference.BRICK_LIST = Utils.sortBrickList(Reference.BRICK_LIST);

		// Add all special ores to ORES_LIST
		Reference.ORES_LIST.addAll(ORES_LIST);
		Reference.ORES_LIST.addAll(SANDORES_LIST);
		Reference.ORES_LIST.addAll(GRAVELORES_LIST);
		Reference.ORES_LIST.addAll(NETHERORES_LIST);
		Reference.ORES_LIST.addAll(ENDORES_LIST);
		Reference.ORES_LIST.addAll(POORORES_LIST);
		Reference.ORES_LIST.addAll(POORSANDORES_LIST);
		Reference.ORES_LIST.addAll(POORGRAVELORES_LIST);
		Reference.ORES_LIST.addAll(POORNETHERORES_LIST);
		Reference.ORES_LIST.addAll(POORENDORES_LIST);
		Reference.ORES_LIST.addAll(DENSEORES_LIST);
		Reference.ORES_LIST.addAll(DENSENETHERORES_LIST);
		Reference.ORES_LIST.addAll(DENSESANDORES_LIST);
		Reference.ORES_LIST.addAll(DENSEGRAVELORES_LIST);
		Reference.ORES_LIST.addAll(DENSEENDORES_LIST);
	}

	private static void createBaseOres(ATOOre ore, Boolean oreBaseEnable, Boolean oreDenseEnable, Boolean orePoorEnable) {
		if (oreBaseEnable) {
			ORES_LIST.add(ore);
			// Register Dense Ore
			if (Reference.CONFIG_GENERATE_DENSE_ORES && oreDenseEnable) {
				// Match ore outputs with dense ore outputs
				ATOOre oreSpecial = new ATOOre(ore.baseName, ore, "dense", "minecraft:stone", false);
				DENSEORES_LIST.add(oreSpecial);
			}
			// Register Poor Ore
			if (Reference.CONFIG_GENERATE_POOR_ORES && orePoorEnable) {
				// Match ore outputs with poor ore outputs
				ATOOre oreSpecial = new ATOOre(ore.baseName, ore, "poor", "minecraft:stone", false);
				POORORES_LIST.add(oreSpecial);
			}
		}
	}

	private static void changeBaseOre(ATOOre ore) {
		if (ATODust != null)
			ATODust.setOre(ore);
		if (ATODustTiny != null)
			ATODustTiny.setOre(ore);
		if (ATONugget != null)
			ATONugget.setOre(ore);
		if (ATOIngot != null)
			ATOIngot.setOre(ore);
		if (ATOCrushed != null)
			ATOCrushed.setOre(ore);
		if (ATOCrushedPurified != null)
			ATOCrushedPurified.setOre(ore);
		if (ATOBlock != null)
			ATOBlock.setOre(ore);
		if (ATOBrick != null)
			ATOBrick.setOre(ore);
	}

	private static void createSandOres(ATOOre ore, Boolean oreSandEnable, Boolean oreDenseSandEnable, Boolean orePoorSandEnable, boolean baseOre) {
		if (Reference.CONFIG_GENERATE_SAND_ORES && oreSandEnable) {
			// Register Sand Ore
			ATOOre oreSpecial = new ATOOre(ore.baseName + "__sand", ore, "sand", "minecraft:sand", false);
			oreSpecial.falls = true;
			SANDORES_LIST.add(oreSpecial);
			if (baseOre)
				changeBaseOre(oreSpecial);
			if (Reference.CONFIG_GENERATE_DENSESAND_ORES && oreDenseSandEnable) {
				// Register Dense Sand Ore
				oreSpecial = new ATOOre(ore.baseName + "__sand", ore, "densesand", "minecraft:sand", false);
				oreSpecial.falls = true;
				DENSESANDORES_LIST.add(oreSpecial);
			}
			if (Reference.CONFIG_GENERATE_POORSAND_ORES && orePoorSandEnable) {
				// Register Poor Sand Ore
				oreSpecial = new ATOOre(ore.baseName + "__sand", ore, "poorsand", "minecraft:sand", false);
				oreSpecial.falls = true;
				POORSANDORES_LIST.add(oreSpecial);
			}
		}
	}

	private static void createGravelOres(ATOOre ore, Boolean oreGravelEnable, Boolean oreDenseGravelEnable, Boolean orePoorGravelEnable, boolean baseOre) {
		if (Reference.CONFIG_GENERATE_GRAVEL_ORES && oreGravelEnable) {
			// Register Gravel Ores
			ATOOre oreSpecial = new ATOOre(ore.baseName + "__gravel", ore, "gravel", "minecraft:gravel", false);
			oreSpecial.falls = true;
			GRAVELORES_LIST.add(oreSpecial);
			if (baseOre)
				changeBaseOre(oreSpecial);
			if (Reference.CONFIG_GENERATE_DENSEGRAVEL_ORES && oreDenseGravelEnable) {
				// Register Dense Gravel Ore
				oreSpecial = new ATOOre(ore.baseName + "__gravel", ore, "densegravel", "minecraft:gravel", false);
				oreSpecial.falls = true;
				DENSEGRAVELORES_LIST.add(oreSpecial);
			}
			if (Reference.CONFIG_GENERATE_POORGRAVEL_ORES && orePoorGravelEnable) {
				// Register Poor Gravel Ore
				oreSpecial = new ATOOre(ore.baseName + "__gravel", ore, "poorgravel", "minecraft:gravel", false);
				oreSpecial.falls = true;
				POORGRAVELORES_LIST.add(oreSpecial);
			}
		}
	}

	private static void createNetherOres(ATOOre ore, Boolean oreNetherEnable, Boolean oreDenseNetherEnable, Boolean orePoorNetherEnable, Boolean baseOre) {
		if ((Reference.CONFIG_GENERATE_NETHER_ORES && oreNetherEnable)) {
			ATOOre oreSpecial = new ATOOre(ore.baseName + "__nether", ore, "nether", "minecraft:netherrack", baseOre);
			NETHERORES_LIST.add(oreSpecial);
			if (baseOre)
				changeBaseOre(oreSpecial);
			// Register Dense Nether Ore
			if (Reference.CONFIG_GENERATE_DENSENETHER_ORES && oreDenseNetherEnable) {
				// Match ore outputs with dense nether ore
				// outputs
				oreSpecial = new ATOOre(ore.baseName + "__nether", ore, "densenether", "minecraft:netherrack", false);
				DENSENETHERORES_LIST.add(oreSpecial);
			}
			// Register Poor Nether Ore
			if (Reference.CONFIG_GENERATE_POORNETHER_ORES && orePoorNetherEnable) {
				// Match ore outputs with poor nether ore
				// outputs
				oreSpecial = new ATOOre(ore.baseName + "__nether", ore, "poornether", "minecraft:netherrack", false);
				POORNETHERORES_LIST.add(oreSpecial);
			}
		}
	}

	private static void createEndOres(ATOOre ore, Boolean oreEndEnable, Boolean oreDenseEndEnable, Boolean orePoorEndEnable, Boolean baseOre) {
		if (Reference.CONFIG_GENERATE_END_ORES && oreEndEnable) {
			ATOOre oreSpecial = new ATOOre(ore.baseName + "__end", ore, "end", "minecraft:end_stone", baseOre);
			ENDORES_LIST.add(oreSpecial);
			if (baseOre)
				changeBaseOre(oreSpecial);
			// Register Dense End Ore
			if (Reference.CONFIG_GENERATE_DENSEEND_ORES && oreDenseEndEnable) {
				// Match ore outputs with dense end ore
				// outputs
				oreSpecial = new ATOOre(ore.baseName + "__end", ore, "denseend", "minecraft:end_stone", false);
				DENSEENDORES_LIST.add(oreSpecial);
			}
			// Register Poor End Ore
			if (Reference.CONFIG_GENERATE_POOREND_ORES && orePoorEndEnable) {
				// Match ore outputs with poor end ore
				// outputs
				oreSpecial = new ATOOre(ore.baseName + "__end", ore, "poorend", "minecraft:end_stone", false);
				POORENDORES_LIST.add(oreSpecial);
			}
		}
	}

	public static void createDefaults(Path configDir) {
		// Make Ores folder if it does not exist
		File dir = new File(configDir + "/Ores");
		dir.mkdirs();
		// create example file if folder is empty or does not exist
		File file = new File(dir, "All.cfg");
		if (file.exists()) {
			file.delete();
		}
		Configuration configOres = new Configuration(file);
		if (dir.listFiles().length <= 0 || Reference.DEBUG) {
			String[] oreDict = OreDictionary.getOreNames();
			for (String s : oreDict) {
				if (s.startsWith("ore") && (!s.startsWith("orePoor")) && (!s.startsWith("oreDense")) && (!s.startsWith("poorore")) && (!s.startsWith("denseore"))) {
					String name = s.substring(3);
					String sname = Utils.getSpecialName(name);
					String iname = "";
					LogHelper.mod_debug("ore = " + s);
					LogHelper.mod_debug("name = " + name);
					LogHelper.mod_debug("sname = " + sname);
					int meta = 0;
					if (OreDictionary.getOres(s).size() > 0) {
						if (!configOres.hasCategory(sname)) {
							configOres.getCategory(sname);
						}
						configOres.removeCategory(configOres.getCategory(sname));
						String category = configOres.getCategory(sname).getName();
						Block ore = Block.getBlockFromItem(OreDictionary.getOres(s).get(0).getItem());
						int oreRenderType = 0;

						boolean toolsEnable = true;
						boolean weaponsEnable = true;
						boolean armorEnable = true;
						boolean blockEnable = true;
						boolean brickEnable = true;
						boolean ingotEnable = true;
						boolean nuggetEnable = true;
						boolean dustEnable = true;
						boolean dustTinyEnable = true;
						boolean crushedEnable = true;
						boolean crushingEnable = true;
						boolean smeltEnable = true;
						boolean maceratingEnable = true;
						boolean average = true;

						if (ore != null) {
							iname = Item.itemRegistry.getNameForObject(OreDictionary.getOres(s).get(0).getItem());
							meta = OreDictionary.getOres(s).get(0).getItemDamage();
							int oreHarvestLevel = (ore.getHarvestLevel(meta) >= 0) ? ore.getHarvestLevel(meta) : 0;
							int oreColor = 0;
							modID = iname != null ? iname.lastIndexOf(":") >= 0 ? iname.substring(0, iname.lastIndexOf(":")) : "minecraft" : "minecraft";

							if (iname != null && (!modID.equalsIgnoreCase("rotarycraft") || (!modID.equalsIgnoreCase("reactorcraft"))) && (!modID.equalsIgnoreCase("minecraft"))) {
								// Get color from texture
								Block block = ore;
								LogHelper.mod_debug("block = block" + name);
								if (OreDictionary.getOres("block" + name).size() > 0) {
									LogHelper.mod_debug("OreDictionary.getOres(\"block" + name + "\").size() = " + OreDictionary.getOres("block" + name).size());
									block = Block.getBlockFromItem(OreDictionary.getOres("block" + name).get(0).getItem());
									meta = OreDictionary.getOres("block" + name).get(0).getItemDamage();
								}

								oreColor = getColor(name, sname, block, meta);
								LogHelper.mod_debug("oreColor = " + oreColor);
								if (oreColor >= 0) {

									configOres.get(category, "ore", iname + ":" + meta).getString();
									// configOres.get(category, "oreMeta",
									// meta).getInt();
									configOres.get(category, "oreHarvestLevel", oreHarvestLevel).getInt();
									configOres.get(category, "oreRenderType", oreRenderType).getInt();
									configOres.get(category, "oreColor", Integer.toHexString(oreColor)).getString();
									if (sname.startsWith("quartz") || sname.startsWith("nether")) {
										underlyingBlock = "minecraft:netherrack";
									}
									if (underlyingBlock != null) {
										configOres.get(category, "underlyingBlock", underlyingBlock).getString();
									} else {
										configOres.get(category, "underlyingBlock", "minecraft:stone").getString();
									}
								}

								// Set up default Block info
								if (OreDictionary.getOres("block" + name).size() > 0) {
									iname = Item.itemRegistry.getNameForObject(OreDictionary.getOres("block" + name).get(0).getItem());
									meta = OreDictionary.getOres("block" + name).get(0).getItemDamage();

									if (iname != null) {
										configOres.get(category, "block", iname + ":" + meta).getString();
										// configOres.get(category, "blockMeta",
										// meta).getInt();
									}
								}

								// Set up default Brick info
								if (OreDictionary.getOres("brick" + name).size() > 0) {
									iname = Item.itemRegistry.getNameForObject(OreDictionary.getOres("brick" + name).get(0).getItem());
									meta = OreDictionary.getOres("brick" + name).get(0).getItemDamage();

									if (iname != null) {
										configOres.get(category, "brick", iname + ":" + meta).getString();
										// configOres.get(category, "brickMeta",
										// meta).getInt();
									}
								}

								// Set up default Ingot info
								if (OreDictionary.getOres("ingot" + name).size() > 0 || OreDictionary.getOres("gem" + name).size() > 0) {
									int ingotType = 0;
									if (OreDictionary.getOres("ingot" + name).size() > 0) {
										iname = Item.itemRegistry.getNameForObject(OreDictionary.getOres("ingot" + name).get(0).getItem());
										meta = OreDictionary.getOres("ingot" + name).get(0).getItemDamage();
									} else if (OreDictionary.getOres("gem" + name).size() > 0) {
										ingotType = 2;
									}

									if (iname != null) {
										configOres.get(category, "ingot", iname + ":" + meta).getString();
										// configOres.get(category, "ingotMeta",
										// meta).getInt();
										if (ingotType == 2) {
											configOres.get(category, "ingotRenderType", ingotType).getInt();
											configOres.get(category, "ingotType", ingotType).getInt();
										}
									}
								}

								// Set up default Dust info
								if (OreDictionary.getOres("dust" + name).size() > 0) {
									iname = Item.itemRegistry.getNameForObject(OreDictionary.getOres("dust" + name).get(0).getItem());
									meta = OreDictionary.getOres("dust" + name).get(0).getItemDamage();

									if (iname != null) {
										configOres.get(category, "dust", iname + ":" + meta).getString();
										// configOres.get(category, "dustMeta",
										// meta).getInt();
									}
								}

								if (OreDictionary.getOres("dustTiny" + name).size() > 0) {
									iname = Item.itemRegistry.getNameForObject(OreDictionary.getOres("dustTiny" + name).get(0).getItem());
									meta = OreDictionary.getOres("dustTiny" + name).get(0).getItemDamage();

									if (iname != null) {
										configOres.get(category, "dustTiny", iname + ":" + meta).getString();
										// configOres.get(category, "dustTinyMeta",
										// meta).getInt();
									}
								}

								if (OreDictionary.getOres("nugget" + name).size() > 0) {
									iname = Item.itemRegistry.getNameForObject(OreDictionary.getOres("nugget" + name).get(0).getItem());
									meta = OreDictionary.getOres("nugget" + name).get(0).getItemDamage();

									if (iname != null) {
										configOres.get(category, "nugget", iname + ":" + meta).getString();
										// configOres.get(category, "nuggetMeta",
										// meta).getInt();
									}
								}

								if (OreDictionary.getOres("crushed" + name).size() > 0) {
									iname = Item.itemRegistry.getNameForObject(OreDictionary.getOres("crushed" + name).get(0).getItem());
									meta = OreDictionary.getOres("crushed" + name).get(0).getItemDamage();

									if (iname != null) {
										configOres.get(category, "crushed", iname + ":" + meta).getString();
										// configOres.get(category, "crushedMeta",
										// meta).getInt();
									}
								}

								if (OreDictionary.getOres("crushedPurified" + name).size() > 0) {
									iname = Item.itemRegistry.getNameForObject(OreDictionary.getOres("crushedPurified" + name).get(0).getItem());
									meta = OreDictionary.getOres("crushedPurified" + name).get(0).getItemDamage();

									if (iname != null) {
										configOres.get(category, "crushedPurified", iname + ":" + meta).getString();
										// configOres.get(category, "crushedPurifiedMeta",
										// meta).getInt();
									}
								}
								if (!toolsEnable) {
									configOres.get(category, "toolsEnable", toolsEnable).getBoolean();
								}
								if (!weaponsEnable) {
									configOres.get(category, "weaponsEnable", weaponsEnable).getBoolean();
								}
								if (!armorEnable) {
									configOres.get(category, "armorEnable", armorEnable).getBoolean();
								}
								if (!blockEnable) {
									configOres.get(category, "blockEnable", blockEnable).getBoolean();
								}
								if (!brickEnable) {
									configOres.get(category, "brickEnable", brickEnable).getBoolean();
								}
								if (!ingotEnable) {
									configOres.get(category, "ingotEnable", ingotEnable).getBoolean();
								}
								if (!nuggetEnable) {
									configOres.get(category, "nuggetEnable", nuggetEnable).getBoolean();
								}
								if (!dustEnable) {
									configOres.get(category, "dustEnable", dustEnable).getBoolean();
								}
								if (!dustTinyEnable) {
									configOres.get(category, "dustTinyEnable", dustTinyEnable).getBoolean();
								}
								if (!crushedEnable) {
									configOres.get(category, "crushedEnable", crushedEnable).getBoolean();
								}
								if (!smeltEnable) {
									configOres.get(category, "smeltEnable", smeltEnable).getBoolean();
								}
								if (!crushingEnable) {
									configOres.get(category, "crushingEnable", crushingEnable).getBoolean();
								}
								if (!maceratingEnable) {
									configOres.get(category, "maceratingEnable", maceratingEnable).getBoolean();
								}
							} else {
								configOres.removeCategory(configOres.getCategory(sname));
							}
						}
					}
				}
			}
		}
		if (configOres.hasChanged()) {
			configOres.save();
		}
	}

	public static int getColor(String name, String sname, Block block, int meta) {
		int oreColor = 0;
		String blockName = Item.itemRegistry.getNameForObject(new ItemStack(block, meta).getItem());
		int blockMeta = meta;
		if (OreDictionary.getOres("block" + name).size() > 0) {
			blockName = Item.itemRegistry.getNameForObject(OreDictionary.getOres("block" + name).get(0).getItem());
			blockMeta = OreDictionary.getOres("block" + name).get(0).getItemDamage();
		}
		modID = blockName != null ? blockName.lastIndexOf(":") >= 0 ? blockName.substring(0, blockName.lastIndexOf(":")) : "minecraft" : "minecraft";
		if (blockName != null) {
			blockName = (blockName.lastIndexOf(":") < 0) ? blockName : blockName.substring(blockName.lastIndexOf(":") + 1);
		}
		List<String> textureNamesArray = new ArrayList();
		Boolean average = false;
		if (name.toLowerCase().startsWith("nether")) {
			underlyingBlock = "minecraft:netherrack";
			average = true;
		} else {
			underlyingBlock = "minecraft:stone";
		}
		LogHelper.mod_debug("modID = " + modID);

		// Metallurgy texture
		if (modID.equalsIgnoreCase("metallurgy")) {
			String mname = blockName.substring(blockName.lastIndexOf(":") + 1, blockName.indexOf("."));
			textureNamesArray.add(mname + "/" + sname + "_block");
			mname = mname.toLowerCase();
			if (mname.equals("ender")) {
				oreRenderType = 4;
				underlyingBlock = "minecraft:end_stone";
			} else if (mname.equals("fantasy")) {
				oreRenderType = 5;
			} else if (mname.equals("nether")) {
				average = true;
				oreRenderType = 6;
				underlyingBlock = "minecraft:netherrack";
			} else if (mname.equals("precious")) {
				oreRenderType = 7;
			} else if (mname.equals("base") || mname.equals("utility")) {
				oreRenderType = 8;
			}

			// Galacticraft texture
		} else if (modID.equalsIgnoreCase("galacticraftcore")) {
			if (name.contains("cheese")) {
				underlyingBlock = "galacticraftcore:stone";
			}
			// HEE texture
		} else if (modID.equalsIgnoreCase("hardcoreenderexpansion")) {
			underlyingBlock = "minecraft:end_stone";
			// IC2 texture
		} else if (modID.equalsIgnoreCase("ic2")) {
			if (name.toLowerCase().startsWith("uran")) {
				textureNamesArray.add("textures/items/resources/itemPurifiedCrushedUranOre");
			} else {
				textureNamesArray.add("textures/items/resources/itemPurifiedCrushed" + name + "Ore");
			}
			// ThermalFoundation
		} else if (modID.equalsIgnoreCase("thermalfoundation")) {
			textureNamesArray.add("ore/Ore_" + name);
			// BigReactors texture
			// } else if (modID.equals("bigreactors")) {
			// textureNames.add(name + "Ore");
			// Default texture
		} else {
			// Look for ingot first
			ItemStack itemStack = null;

			if (OreDictionary.getOres("ingot" + name).size() > 0) {
				itemStack = OreDictionary.getOres("ingot" + name).get(0);
				try {
					textureNamesArray.add(modID + "textures/items/" + itemStack.getItem().getIcon(itemStack, 0).getIconName());
				} catch (NullPointerException e) {
					// LogHelper.mod_debug(e.getMessage());
				}
			}

			textureNamesArray.add(blockName);
			textureNamesArray.add(blockName + "_bottom");
			textureNamesArray.add("block_" + blockName);
			textureNamesArray.add("block" + blockName);
			textureNamesArray.add(blockName + "Block");
			textureNamesArray.add(blockName + "block");
			textureNamesArray.add(blockName.toLowerCase() + "block");
			textureNamesArray.add("block." + blockName.toLowerCase().replaceAll("_", "."));
			textureNamesArray.add(blockName.toLowerCase().replaceAll("_", ".") + ".block");

			textureNamesArray.add("block_" + name);
			textureNamesArray.add("block" + name);
			textureNamesArray.add(name + "Block");
			textureNamesArray.add(name + "block");
			textureNamesArray.add(name.toLowerCase() + "block");
			textureNamesArray.add(name.toLowerCase() + "_block");
			textureNamesArray.add(name.toLowerCase() + "_block_1");
			textureNamesArray.add("block." + name.toLowerCase().replaceAll("_", "."));
			textureNamesArray.add(name.toLowerCase().replaceAll("_", ".") + ".block");
			textureNamesArray.add("compressed_" + name);

			textureNamesArray.add(name);
			textureNamesArray.add(name + "_bottom");
			textureNamesArray.add("ore_" + name);
			textureNamesArray.add("ore" + name);
			textureNamesArray.add(name + "Ore");
			textureNamesArray.add(name + "ore");
			textureNamesArray.add(name.toLowerCase() + "ore");
			textureNamesArray.add(name.toLowerCase() + "_ore");
			textureNamesArray.add(name.toLowerCase() + "_ore_1");
			textureNamesArray.add("ore." + sname.replaceAll("_", "."));
			textureNamesArray.add(sname.replaceAll("_", ".") + ".ore");

			itemStack = new ItemStack(block, 1, blockMeta);
			Field field = Utils.getField(block.getClass(), "textureName");
			if (field != null) {
				Utils.makeAccessible(field);
			}
			if (field != null && block != null) {
				try {
					String textureName = (String) field.get(block);
					if (textureName != null) {
						textureNamesArray.add(textureName);
					}
				} catch (IllegalArgumentException e) {
					LogHelper.mod_debug(e.getMessage());
				} catch (IllegalAccessException e) {
					LogHelper.mod_debug(e.getMessage());
				}
			}
		}

		String[] textureNames = new String[textureNamesArray.size()];
		textureNames = textureNamesArray.toArray(textureNames);
		for (int i = 0; i < textureNames.length; i++) {
			textureNames[i] = modID + ":" + textureNames[i];
		}
		oreColor = ImageUtils.getColor(average, textureNames);
		return oreColor;
	}
}
