package com.rikuss4.alltheores;

import java.nio.file.Path;
import java.nio.file.Paths;

import net.minecraftforge.common.MinecraftForge;

import com.rikuss4.alltheores.handler.configHandler;
import com.rikuss4.alltheores.handler.disableVanillaOreHandler;
import com.rikuss4.alltheores.handler.fuelHandler;
import com.rikuss4.alltheores.init.ATOBlocks;
import com.rikuss4.alltheores.init.ATOItems;
import com.rikuss4.alltheores.items.ATOLogo;
import com.rikuss4.alltheores.reference.Reference;
import com.rikuss4.alltheores.utility.ImageUtils;
import com.rikuss4.alltheores.worldgen.Generator;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = Reference.DEPS)
public class AllTheOres {
	@Mod.Instance(Reference.MOD_ID)
	public static AllTheOres instance;
	//Path configDir = null;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		Path configDir = Paths.get(e.getModConfigurationDirectory().getPath()
				+ "/" + Reference.MOD_ID);
		configHandler.preInit(configDir);

		Reference.isIC2Loaded = Loader.isModLoaded("IC2");
		Reference.isEIOLoaded = Loader.isModLoaded("EnderIO");
		Reference.isUBCLoaded = Loader.isModLoaded("UndergroundBiomes");

		ImageUtils.doInit();
		// ATO Logo
		ATOLogo ATOLogo = new ATOLogo();
		// GameRegistry.registerItem(ATOLogo, ATOLogo.getUnlocalizedName().substring(5));

		ATOBlocks.preInit(e);
		ATOItems.preInit();

		MinecraftForge.ORE_GEN_BUS.register(new disableVanillaOreHandler());
		GameRegistry.registerWorldGenerator(new Generator(), 16);
		GameRegistry.registerFuelHandler(new fuelHandler());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		ATOBlocks.init();
		ATOItems.init();
	}

	@Mod.EventHandler
	public void postInit(FMLInitializationEvent e) {
		ATOBlocks.postInit();
		ATOItems.postInit();
	}
}
