package com.rikuss4.alltheores.init;

import java.util.LinkedList;

import net.minecraft.item.ItemStack;

import com.rikuss4.alltheores.blocks.ATOBlock;
import com.rikuss4.alltheores.blocks.ATOBrick;
import com.rikuss4.alltheores.blocks.ATOOre;
import com.rikuss4.alltheores.creativetab.ATOCreativeTab;
import com.rikuss4.alltheores.items.ATOBlockItem;
import com.rikuss4.alltheores.items.ATOBrickItem;
import com.rikuss4.alltheores.items.ATOOreItem;
import com.rikuss4.alltheores.reference.Reference;
import com.rikuss4.alltheores.utility.LogHelper;
import com.rikuss4.alltheores.utility.Utils;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import exterminatorJeff.undergroundBiomes.api.UBAPIHook;

public class Blocks {
	public static void preInit(FMLPreInitializationEvent event) {
		LinkedList<ATOBlock> BLOCK_LIST = new LinkedList<ATOBlock>();
		LinkedList<ATOBrick> BRICK_LIST = new LinkedList<ATOBrick>();

		for (ATOOre ore : Reference.ORES_LIST) {
			LogHelper.mod_debug("All The Ores Block Register:");
			LogHelper.mod_debug(ore.name);
			LogHelper.mod_debug(ore.getUnlocalizedName());
			// Register the ore blocks
			GameRegistry.registerBlock(ore, ATOOreItem.class, ore.name);
			// Add to Ore Creative Tab
			ore.setCreativeTab(ATOCreativeTab.ORES_TAB);
			// Register Items Display Name
			String name = new ItemStack(ore).getItem().getItemStackDisplayName(new ItemStack(ore));
			LanguageRegistry.instance().addStringLocalization(ore.getUnlocalizedName() + ".name", name);

			// UBC Support
			if (Reference.isUBCLoaded && (ore.underlyingBlockName.equals("stone") || ore.underlyingBlockName.equals("minecraft:stone"))) {
				// Underground Biomes Constructs Support
				try {
					UBAPIHook.ubAPIHook.ubOreTexturizer.setupUBOre(ore, 0, Reference.MOD_ID.toLowerCase() + ":" + ore.name + "_overlay", event);
				} catch (Exception err) {
					LogHelper.info(err.toString());
				}
			}

			if (ore.ore == null) {
				// Register the storage blocks
				if (ore.createBlock()) {
					BLOCK_LIST.add(ore.block);
				}

				// Register the bricks
				if (ore.createBrick()) {
					BRICK_LIST.add(ore.brick);
				}

				// Register Items from ore
				if (ore.createIngot()) {
					GameRegistry.registerItem(ore.ingot, ore.ingot.getName());
					ore.ingot.setCreativeTab(ATOCreativeTab.ITEMS_TAB);
				}
				if (ore.createNugget()) {
					GameRegistry.registerItem(ore.nugget, ore.nugget.getName());
					ore.nugget.setCreativeTab(ATOCreativeTab.ITEMS_TAB);
				}
				if (ore.createDust()) {
					GameRegistry.registerItem(ore.dust, ore.dust.getName());
					ore.dust.setCreativeTab(ATOCreativeTab.ITEMS_TAB);
				}
				if (ore.createDustTiny()) {
					GameRegistry.registerItem(ore.dustTiny, ore.dustTiny.getName());
					ore.dustTiny.setCreativeTab(ATOCreativeTab.ITEMS_TAB);
				}
				if (ore.createCrushed()) {
					GameRegistry.registerItem(ore.crushed, ore.crushed.getName());
					ore.crushed.setCreativeTab(ATOCreativeTab.ITEMS_TAB);
				}
				if (ore.createCrushedPurified()) {
					GameRegistry.registerItem(ore.crushedPurified, ore.crushedPurified.getName());
					ore.crushedPurified.setCreativeTab(ATOCreativeTab.ITEMS_TAB);
				}
				// Ingot based recipes
				if (ore.getIngot(1) != null) {
					// Armor
					// Helmet
					if (ore.helmet != null) {
						GameRegistry.registerItem(ore.helmet, ore.helmet.getName());
						//ore.helmet.setCreativeTab(ATOCreativeTab.ARMOR_TAB);
					}
					// Chestplate
					if (ore.chestplate != null) {
						GameRegistry.registerItem(ore.chestplate, ore.chestplate.getName());
						//ore.chestplate.setCreativeTab(ATOCreativeTab.ARMOR_TAB);
					}
					// Legs
					if (ore.legs != null) {
						GameRegistry.registerItem(ore.legs, ore.legs.getName());
						//ore.legs.setCreativeTab(ATOCreativeTab.ARMOR_TAB);
					}
					// Boots
					if (ore.boots != null) {
						GameRegistry.registerItem(ore.boots, ore.boots.getName());
						//ore.boots.setCreativeTab(ATOCreativeTab.ARMOR_TAB);
					}

					// Weapons
					// Sword
					if (ore.sword != null) {
						GameRegistry.registerItem(ore.sword, ore.sword.getName());
						//ore.sword.setCreativeTab(ATOCreativeTab.TOOLS_TAB);
					}

					// Tools
					// Spade
					if (ore.spade != null) {
						GameRegistry.registerItem(ore.spade, ore.spade.getName());
						//ore.spade.setCreativeTab(ATOCreativeTab.TOOLS_TAB);
					}
					// Hoe
					if (ore.hoe != null) {
						GameRegistry.registerItem(ore.hoe, ore.hoe.getName());
						//ore.hoe.setCreativeTab(ATOCreativeTab.TOOLS_TAB);
					}
					// Axe
					if (ore.axe != null) {
						GameRegistry.registerItem(ore.axe, ore.axe.getName());
						//ore.axe.setCreativeTab(ATOCreativeTab.TOOLS_TAB);
					}
					// Pickaxe
					if (ore.pickaxe != null) {
						GameRegistry.registerItem(ore.pickaxe, ore.pickaxe.getName());
						//ore.pickaxe.setCreativeTab(ATOCreativeTab.TOOLS_TAB);
					}
				}
			}
		}
		for (ATOBlock block : BLOCK_LIST) {
			GameRegistry.registerBlock(block, ATOBlockItem.class, block.getName());
			block.setCreativeTab(ATOCreativeTab.BLOCKS_TAB);
		}
		for (ATOBrick brick : BRICK_LIST) {
			GameRegistry.registerBlock(brick, ATOBrickItem.class, brick.getName());
			brick.setCreativeTab(ATOCreativeTab.BLOCKS_TAB);
		}
	}

	public static void init() {
		// Register blocks in ore dictionary
		for (ATOOre ore : Reference.ORES_LIST) {
			LogHelper.mod_debug("Ores Registry:");
			LogHelper.mod_debug(ore.oreDictName);
			//ore.registerOreDict();
			if (ore.ore == null) {
				if (ore.createBlock()) {
					ore.block.registerOreDict();
				}
				if (ore.createBrick()) {
					ore.brick.registerOreDict();
				}
			}
		}
		// Register crafting recipes
		Utils.removeCrafting();
		StringBuilder toSend = new StringBuilder();
		for (ATOOre ore : Reference.ORES_LIST) {
			if (Reference.isIC2Loaded)
				ore.registerIC2Recipes();
			if (Reference.isEIOLoaded)
				ore.registerEnderIORecipes();
			ore.registerCrafting();
			ore.registerSmelting();
		}
	}

	public static void postInit() {
	}
}
