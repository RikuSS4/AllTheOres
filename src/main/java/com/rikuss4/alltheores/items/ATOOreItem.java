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
    	String name = StatCollector.translateToLocal(getOre().getUnlocalizedName() + ".name"); 
    	if(name.equals(getOre().getUnlocalizedName() + ".name")) {
        	//name = getOre().fullname + ((getOre().useSuffix()) ? " " + new ItemStack(getOre().underlyingBlock).getDisplayName() : "");
        	//name = getOre().getPrefix() + getOre().capitalize(getOre().name) + ((getOre().useSuffix()) ? " " + getOre().getSuffix() : "");
        	name = Utils.capitalize(getOre().name.substring(0, getOre().name.length() - 4));
            name = (StatCollector.translateToLocal("AllTheOres.oreSubstite").trim()).replaceFirst("ORENAME", name);
    	}
        return name;
    }
}
