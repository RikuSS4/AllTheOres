package com.rikuss4.alltheores.handler;

import net.minecraft.item.ItemStack;

import com.rikuss4.alltheores.items.Resources.Dust;

import cpw.mods.fml.common.IFuelHandler;

public class fuelHandler implements IFuelHandler {
    @Override
    public int getBurnTime(ItemStack fuel) {
        if (fuel.getItem() instanceof Dust) {
        	Dust n = (Dust) fuel.getItem();
            return n.getBurnTime();
        } else {
            return 0;
        }
    }
}
