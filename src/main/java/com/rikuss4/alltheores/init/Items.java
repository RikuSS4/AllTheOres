package com.rikuss4.alltheores.init;

import com.rikuss4.alltheores.blocks.ATOOre;
import com.rikuss4.alltheores.reference.Reference;

import cpw.mods.fml.common.Loader;

public class Items {
	public static void preInit() {
	}

	public static void init() {
		for (ATOOre ore : Reference.ORES_LIST) {
			if (ore.ingot != null) {
				ore.ingot.registerOreDict();
			}
			if (ore.nugget != null) {
				ore.nugget.registerOreDict();
			}
			if (ore.dust != null) {
				ore.dust.registerOreDict();
			}
			if (ore.dustTiny != null) {
				ore.dustTiny.registerOreDict();
			}
			if ((Loader.isModLoaded("IC2"))) {
				if (ore.crushed != null) {
					ore.crushed.registerOreDict();
				}
				if (ore.crushedPurified != null) {
					ore.crushedPurified.registerOreDict();
				}
			}
		}
	}

	public static void postInit() {
	}
}
