package com.rikuss4.alltheores.init;

import com.rikuss4.alltheores.blocks.ATOOre;
import com.rikuss4.alltheores.creativetab.ATOCreativeTab;
import com.rikuss4.alltheores.items.Resources.ATOCrushed;
import com.rikuss4.alltheores.items.Resources.ATOCrushedPurified;
import com.rikuss4.alltheores.items.Resources.ATODust;
import com.rikuss4.alltheores.items.Resources.ATODustTiny;
import com.rikuss4.alltheores.items.Resources.ATOIngot;
import com.rikuss4.alltheores.items.Resources.ATONugget;
import com.rikuss4.alltheores.reference.Reference;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class Items {
	public static void preInit() {
		if (!Reference.INGOT_LIST.isEmpty()) {
			for (ATOIngot ingot : Reference.INGOT_LIST) {
				GameRegistry.registerItem(ingot, ingot.getName());
				ingot.setCreativeTab(ATOCreativeTab.ITEMS_TAB);
			}
		}
		if (!Reference.NUGGET_LIST.isEmpty()) {
			for (ATONugget nugget : Reference.NUGGET_LIST) {
				GameRegistry.registerItem(nugget, nugget.getName());
				nugget.setCreativeTab(ATOCreativeTab.ITEMS_TAB);
			}
		}
		if (!Reference.DUST_LIST.isEmpty()) {
			for (ATODust dust : Reference.DUST_LIST) {
				GameRegistry.registerItem(dust, dust.getName());
				dust.setCreativeTab(ATOCreativeTab.ITEMS_TAB);
			}
		}
		if (!Reference.DUSTTINY_LIST.isEmpty()) {
			for (ATODustTiny dustTiny : Reference.DUSTTINY_LIST) {
				GameRegistry.registerItem(dustTiny, dustTiny.getName());
				dustTiny.setCreativeTab(ATOCreativeTab.ITEMS_TAB);
			}
		}
		if ((Loader.isModLoaded("IC2"))) {
			if (!Reference.CRUSHED_LIST.isEmpty()) {
				for (ATOCrushed crushed : Reference.CRUSHED_LIST) {
					GameRegistry.registerItem(crushed, crushed.getName());
					crushed.setCreativeTab(ATOCreativeTab.ITEMS_TAB);
				}
			}
			if (!Reference.CRUSHEDPURIFIED_LIST.isEmpty()) {
				for (ATOCrushedPurified crushedPurified : Reference.CRUSHEDPURIFIED_LIST) {
					GameRegistry.registerItem(crushedPurified, crushedPurified.getName());
					crushedPurified.setCreativeTab(ATOCreativeTab.ITEMS_TAB);
				}
			}
		}
	}

	public static void init() {
		if (!Reference.INGOT_LIST.isEmpty()) {
			for (ATOIngot ingot : Reference.INGOT_LIST) {
				ingot.registerOreDict();
			}
		}
		if (!Reference.NUGGET_LIST.isEmpty()) {
			for (ATONugget nugget : Reference.NUGGET_LIST) {
				nugget.registerOreDict();
			}
		}
		if (!Reference.DUST_LIST.isEmpty()) {
			for (ATODust dust : Reference.DUST_LIST) {
				dust.registerOreDict();
			}
		}
		if (!Reference.DUSTTINY_LIST.isEmpty()) {
			for (ATODustTiny dustTiny : Reference.DUSTTINY_LIST) {
				dustTiny.registerOreDict();
			}
		}
		if ((Loader.isModLoaded("IC2"))) {
			if (!Reference.CRUSHED_LIST.isEmpty()) {
				for (ATOCrushed crushed : Reference.CRUSHED_LIST) {
					crushed.registerOreDict();
				}
			}
			if (!Reference.CRUSHEDPURIFIED_LIST.isEmpty()) {
				for (ATOCrushedPurified crushedPurified : Reference.CRUSHEDPURIFIED_LIST) {
					crushedPurified.registerOreDict();
				}
			}
		}
	}

	public static void postInit() {
	}
}
