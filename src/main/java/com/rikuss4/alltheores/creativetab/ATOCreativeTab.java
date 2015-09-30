package com.rikuss4.alltheores.creativetab;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import com.rikuss4.alltheores.blocks.ATOOre;
import com.rikuss4.alltheores.reference.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ATOCreativeTab {

	public static final CreativeTabs ORES_TAB = new CreativeTabs(
			Reference.MOD_ID + ".ores") {
		@Override
		public Item getTabIconItem() {
			if (Reference.ORES_LIST.getFirst() != null) {
				return Item.getItemFromBlock(Reference.ORES_LIST.getFirst());
			} else {
				return Item.getItemFromBlock(Blocks.iron_ore);
			}
		}

		@SideOnly(Side.CLIENT)
		public void displayAllReleventItems(List oresList) {
			Collections.sort(oresList, new Comparator<ATOOre>() {
				@Override
				public int compare(ATOOre o1, ATOOre o2) {
					/*
					 * if(o1.name < o2.name){ return -1; } if(o1.name >
					 * o2.name){ return 1; } return 0;
					 */
					return o1.name.compareToIgnoreCase(o2.name);
				}
			});

			Iterator iterator = Item.itemRegistry.iterator();
			while (iterator.hasNext()) {
				Item item = (Item) iterator.next();

				if (item == null) {
					continue;
				}

				for (CreativeTabs tab : item.getCreativeTabs()) {
					if (tab == this) {
						item.getSubItems(item, this, oresList);
					}
				}
			}

			if (this.func_111225_m() != null) {
				this.addEnchantmentBooksToList(oresList, this.func_111225_m());
			}
		}
	};

	/*public static final CreativeTabs ARMOR_TAB = new CreativeTabs(
			Reference.MOD_ID + ".armor") {
		@Override
		public Item getTabIconItem() {
			for (ATOOre ore : Reference.ORES_LIST) {
				if (ore.helmet != null) {
					return ore.helmet;
				}
			}
			return Items.iron_helmet;
		}
	};*/

	public static final CreativeTabs BLOCKS_TAB = new CreativeTabs(
			Reference.MOD_ID + ".blocks") {
		@Override
		public Item getTabIconItem() {
			for (ATOOre ore : Reference.ORES_LIST) {
				if (ore.block != null) {
					return Item.getItemFromBlock(ore.block);
				}
			}
			return Item.getItemFromBlock(Blocks.iron_block);
		}
	};

	public static final CreativeTabs ITEMS_TAB = new CreativeTabs(
			Reference.MOD_ID + ".items") {
		@Override
		public Item getTabIconItem() {
			for (ATOOre ore : Reference.ORES_LIST) {
				if (ore.ingot != null) {
					return ore.ingot;
				} else if (ore.nugget != null) {
					return ore.nugget;
				} else if (ore.dust != null) {
					return ore.dust;
				} else if (ore.dustTiny != null) {
					return ore.dustTiny;
				}
			}
			return Items.iron_pickaxe;
		}
	};

	/*public static final CreativeTabs TOOLS_TAB = new CreativeTabs(
			Reference.MOD_ID + ".tools") {
		@Override
		public Item getTabIconItem() {
			for (ATOOre ore : Reference.ORES_LIST) {
				if (ore.pickaxe != null) {
					return ore.pickaxe;
				}
			}
			return Items.iron_pickaxe;
		}
	};*/

}
