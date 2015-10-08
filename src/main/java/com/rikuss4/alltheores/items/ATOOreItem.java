package com.rikuss4.alltheores.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.rikuss4.alltheores.blocks.ATOOre;
import com.rikuss4.alltheores.utility.Utils;

public class ATOOreItem extends ItemBlock {
	private ATOOre ore;

	public ATOOreItem(Block ore) {
		super(ore);

		setOre((ATOOre) ore);
	}

	public ATOOre getOre() {
		return ore;
	}

	public void setOre(ATOOre ore) {
		this.ore = ore;
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemStack) {
		// name = getOre().fullname + ((getOre().useSuffix()) ? " " + new
		// ItemStack(getOre().underlyingBlock).getDisplayName() : "");
		// name = getOre().getPrefix() + getOre().capitalize(getOre().name) +
		// ((getOre().useSuffix()) ? " " + getOre().getSuffix() : "");
		String name = StatCollector.translateToLocal(getOre().getUnlocalizedName() + ".name");
		String baseName = StatCollector.translateToLocal("AllTheOres.ore" + Utils.capitalize(getOre().baseName));
		if (!name.equalsIgnoreCase(getOre().getUnlocalizedName() + ".name")) {
			return name;
		}
		if (baseName.equalsIgnoreCase("AllTheOres.ore" + Utils.capitalize(getOre().baseName))) {
			if (getOre() != null && getOre().ore != null) {
				baseName = StatCollector.translateToLocal(getOre().ore.getUnlocalizedName() + ".name");
				if (baseName.equalsIgnoreCase(getOre().ore.getUnlocalizedName() + ".name"))
					baseName = Utils.capitalize(getOre().baseName);
			} else {
				baseName = Utils.capitalize(getOre().baseName);
			}
		}
		String tName = "";
		String stName = "";
		if (ore.type.equalsIgnoreCase("nether") && !ore.base) {
			tName = (StatCollector.translateToLocal("AllTheOres.oreNetherSubstite").trim()).replaceAll("ORENAME", "ORENAME");
		} else if (ore.type.equalsIgnoreCase("end") && !ore.base) {
			tName = (StatCollector.translateToLocal("AllTheOres.oreEndSubstite").trim()).replaceAll("ORENAME", "ORENAME");
		} else if (ore.type.equalsIgnoreCase("gravel") && !ore.base) {
			tName = (StatCollector.translateToLocal("AllTheOres.oreGravelSubstite").trim()).replaceAll("ORENAME", "ORENAME");
		} else if (ore.type.equalsIgnoreCase("sand") && !ore.base) {
			tName = (StatCollector.translateToLocal("AllTheOres.oreSandSubstite").trim()).replaceAll("ORENAME", "ORENAME");
		}
		if (ore.subType.equalsIgnoreCase("poor")) {
			stName = (StatCollector.translateToLocal("AllTheOres.orePoorSubstite").trim()).replaceAll("ORENAME", "ORENAME");
		} else if (ore.subType.equalsIgnoreCase("dense")) {
			stName = (StatCollector.translateToLocal("AllTheOres.oreDenseSubstite").trim()).replaceAll("ORENAME", "ORENAME");
		}

		if (!tName.equalsIgnoreCase("") && !stName.equalsIgnoreCase(""))
			name = (StatCollector.translateToLocal("AllTheOres.oreSubstite").trim()).replaceAll("ORENAME", stName.replaceAll("ORENAME", tName.replaceAll("ORENAME", baseName)));
		else if (!stName.equalsIgnoreCase(""))
			name = (StatCollector.translateToLocal("AllTheOres.oreSubstite").trim()).replaceAll("ORENAME", stName.replaceAll("ORENAME", baseName));
		else if (!tName.equalsIgnoreCase(""))
			name = (StatCollector.translateToLocal("AllTheOres.oreSubstite").trim()).replaceAll("ORENAME", tName.replaceAll("ORENAME", baseName));
		else
			name = (StatCollector.translateToLocal("AllTheOres.oreSubstite").trim()).replaceAll("ORENAME", baseName);
		return name;
	}
}
