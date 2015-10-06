package com.rikuss4.alltheores.handler;

import net.minecraft.item.ItemStack;

import com.rikuss4.alltheores.blocks.ATOBlock;
import com.rikuss4.alltheores.items.Resources.ATODust;
import com.rikuss4.alltheores.items.Resources.ATODustTiny;
import com.rikuss4.alltheores.items.Resources.ATOIngot;
import com.rikuss4.alltheores.items.Resources.ATONugget;

import cpw.mods.fml.common.IFuelHandler;

public class fuelHandler implements IFuelHandler {
    @Override
    public int getBurnTime(ItemStack fuel) {
        if (fuel.getItem() instanceof ATODust) {
      	  ATODust n = (ATODust) fuel.getItem();
            return n.getBurnTime();
        } else if (fuel.getItem() instanceof ATODustTiny) {
      	  ATODustTiny n = (ATODustTiny) fuel.getItem();
           return n.getBurnTime();
        } else if (fuel.getItem() instanceof ATOIngot) {
      	  ATOIngot n = (ATOIngot) fuel.getItem();
           return n.getBurnTime();
        } else if (fuel.getItem() instanceof ATONugget) {
      	  ATONugget n = (ATONugget) fuel.getItem();
           return n.getBurnTime();
        } else {
            return 0;
        }
    }
}
